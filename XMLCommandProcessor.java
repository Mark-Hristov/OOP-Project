import java.util.*;

public class XMLCommandProcessor {
    private XMLNode root;
    private XMLParser parser;
    private Map<String, XMLNode> idMap = new HashMap<>();

    public XMLCommandProcessor() {
        this.parser = new XMLParser();
    }

    public void processCommand(String command) {
        try {
            String[] parts = command.trim().split("\\s+");
            switch (parts[0]) {
                case "open":
                    root = parser.parseXML(parts[1]);
                    indexIds(root);
                    System.out.println("File loaded.");
                    break;
                case "print":
                    printXML(root, 0);
                    break;
                case "select":
                    System.out.println(getNode(parts[1]).getAttribute(parts[2]));
                    break;
                case "set":
                    getNode(parts[1]).setAttribute(parts[2], parts[3]);
                    break;
                case "children":
                    for (XMLNode child : getNode(parts[1]).getChildren()) {
                        System.out.println(child.getId());
                    }
                    break;
                case "child":
                    System.out.println(getNode(parts[1]).getChildren().get(Integer.parseInt(parts[2])).getId());
                    break;
                case "text":
                    System.out.println(getNode(parts[1]).getText());
                    break;
                case "delete":
                    getNode(parts[1]).deleteAttribute(parts[2]);
                    break;
                case "newchild":
                    XMLNode parent = getNode(parts[1]);
                    XMLNode newChild = new XMLNode("newchild");
                    String newId = "gen" + System.currentTimeMillis();
                    newChild.setId(newId);
                    parent.addChild(newChild);
                    idMap.put(newId, newChild);
                    System.out.println("New child added with ID: " + newId);
                    break;
                case "xpath":
                    handleXPath(parts[1], parts[2]);
                    break;
                default:
                    System.out.println("Unknown command.");
            }
        } catch (Exception e) {
            System.out.println("Command error: " + e.getMessage());
        }
    }

    private void handleXPath(String id, String query) {
        XMLNode node = getNode(id);
        if (query.contains("/")) {
            String[] steps = query.split("/");
            List<XMLNode> current = List.of(node);
            for (String step : steps) {
                if (step.isEmpty()) continue;
                List<XMLNode> next = new ArrayList<>();
                for (XMLNode n : current) {
                    for (XMLNode c : n.getChildren()) {
                        if (c.getName().equals(step)) {
                            next.add(c);
                        }
                    }
                }
                current = next;
            }
            for (XMLNode result : current) {
                System.out.println(result.getId());
            }
        } else if (query.startsWith("@")) {
            System.out.println(node.getAttribute(query.substring(1)));
        } else {
            System.out.println("Unsupported XPath format.");
        }
    }

    private void indexIds(XMLNode node) {
        if (node != null) {
            idMap.put(node.getId(), node);
            for (XMLNode child : node.getChildren()) {
                indexIds(child);
            }
        }
    }

    private XMLNode getNode(String id) {
        if (!idMap.containsKey(id)) throw new IllegalArgumentException("No element with ID: " + id);
        return idMap.get(id);
    }

    private void printXML(XMLNode node, int indent) {
        String pad = "  ".repeat(indent);
        System.out.print(pad + "<" + node.getName());
        for (var attr : node.getAttributes().entrySet()) {
            System.out.print(" " + attr.getKey() + "=\"" + attr.getValue() + "\"");
        }
        if (node.getChildren().isEmpty() && node.getText().isEmpty()) {
            System.out.println("/>");
            return;
        }
        System.out.print(">");
        if (!node.getText().isEmpty()) {
            System.out.print(node.getText());
        }
        System.out.println();
        for (XMLNode child : node.getChildren()) {
            printXML(child, indent + 1);
        }
        System.out.println(pad + "</" + node.getName() + ">");
    }
}

