import java.io.*;
import java.util.*;
import java.util.regex.*;

class XMLParser {
    private XMLNode root;

    public XMLNode parseXML(String filePath) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    Stack<XMLNode> stack = new Stack<>();
    Pattern openTag = Pattern.compile("<(\\w+)([^>]*)>");
    Pattern closeTag = Pattern.compile("</(\\w+)>");
    Pattern fullTagWithText = Pattern.compile("<(\\w+)([^>]*)>([^<]+)</\\1>");
    Pattern attrPattern = Pattern.compile("(\\w+)=\"(.*?)\"");

    String line;
    while ((line = reader.readLine()) != null) {
        line = line.trim();

        Matcher fullMatcher = fullTagWithText.matcher(line);
        Matcher openMatcher = openTag.matcher(line);
        Matcher closeMatcher = closeTag.matcher(line);

        if (fullMatcher.matches()) {
            String tag = fullMatcher.group(1);
            String attrPart = fullMatcher.group(2);
            String text = fullMatcher.group(3).trim();

            XMLNode node = new XMLNode(tag, UUID.randomUUID().toString());
            node.setTextContent(text);

            Matcher attrMatcher = attrPattern.matcher(attrPart);
            while (attrMatcher.find()) {
                node.setAttribute(attrMatcher.group(1), attrMatcher.group(2));
            }

            if (!stack.isEmpty()) stack.peek().addChild(node);
            else root = node;
        } else if (openMatcher.matches()) {
            String tag = openMatcher.group(1);
            String attrPart = openMatcher.group(2);

            XMLNode node = new XMLNode(tag, UUID.randomUUID().toString());

            Matcher attrMatcher = attrPattern.matcher(attrPart);
            while (attrMatcher.find()) {
                node.setAttribute(attrMatcher.group(1), attrMatcher.group(2));
            }

            if (!stack.isEmpty()) stack.peek().addChild(node);
            stack.push(node);
            if (root == null) root = node;
        } else if (closeMatcher.matches() && !stack.isEmpty()) {
            stack.pop();
        } else if (!stack.isEmpty()) {
            stack.peek().setTextContent(line);
        }
    }
    reader.close();
    return root;
}
}
