import java.io.*;
import java.util.*;
import java.util.regex.*;

class XMLParser {
    private XMLNode root;

    public XMLNode parseXML(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Stack<XMLNode> stack = new Stack<>();
        Pattern openTag = Pattern.compile("<(\\w+)(.?)>");
        Pattern closeTag = Pattern.compile("</(\\w+)>");
        Pattern attrPattern = Pattern.compile("(\\w+)=\"(.*?)\"");

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            Matcher openMatcher = openTag.matcher(line);
            Matcher closeMatcher = closeTag.matcher(line);

            if (openMatcher.find()) {
                String tagName = openMatcher.group(1);
                XMLNode node = new XMLNode(tagName, UUID.randomUUID().toString());

                Matcher attrMatcher = attrPattern.matcher(openMatcher.group(2));
                while (attrMatcher.find()) {
                    node.setAttribute(attrMatcher.group(1), attrMatcher.group(2));
                }

                if (!stack.isEmpty()) stack.peek().addChild(node);
                stack.push(node);

                if (root == null) root = node;
            } else if (closeMatcher.find() && !stack.isEmpty()) {
                stack.pop();
            }
        }
        reader.close();
        return root;
    }
}