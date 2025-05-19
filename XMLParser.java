import java.io.*;
import java.util.*;
import java.util.regex.*;

public class XMLParser {
    private Set<String> usedIds = new HashSet<>();
    private int generatedId = 1;

    public XMLNode parseXML(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Stack<XMLNode> stack = new Stack<>();
        XMLNode root = null;
        String line;

        Pattern openTagPattern = Pattern.compile("<(\\w+)([^>]*)>");
        Pattern attrPattern = Pattern.compile("(\\w+)=\"([^\"]+)\"");
        Pattern closeTagPattern = Pattern.compile("</(\\w+)>");
        Pattern textPattern = Pattern.compile(">([^<>]+)<");

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("</")) {
                stack.pop();
            } else if (line.startsWith("<")) {
                Matcher openTag = openTagPattern.matcher(line);
                if (openTag.find()) {
                    String tag = openTag.group(1);
                    String attrString = openTag.group(2);
                    XMLNode node = new XMLNode(tag);

                    Matcher attrMatcher = attrPattern.matcher(attrString);
                    String id = null;
                    while (attrMatcher.find()) {
                        String key = attrMatcher.group(1);
                        String value = attrMatcher.group(2);
                        if (key.equals("id")) id = value;
                        node.setAttribute(key, value);
                    }

                    // Unique ID logic
                    if (id == null || usedIds.contains(id)) {
                        while (usedIds.contains("gen" + generatedId)) generatedId++;
                        id = (id != null) ? id + "_" + generatedId : "gen" + generatedId++;
                    }
                    node.setId(id);
                    usedIds.add(id);

                    if (!stack.isEmpty()) {
                        stack.peek().addChild(node);
                    } else {
                        root = node;
                    }

                    stack.push(node);

                    
                    Matcher textMatcher = textPattern.matcher(line);
                    if (textMatcher.find()) {
                        node.setText(textMatcher.group(1));
                    }
                }
            }
        }
        reader.close();
        return root;
    }
}

