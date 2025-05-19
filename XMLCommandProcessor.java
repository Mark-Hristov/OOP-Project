import java.io.*;
import java.util.*;

class XMLCommandProcessor {
    private XMLNode root;
    private XMLParser parser;

    public XMLCommandProcessor() {
        this.parser = new XMLParser();
    }

    public void processCommand(String command) {
        String[] parts = command.split(" ");
        switch (parts[0]) {
            case "open":
                try {
                    root = parser.parseXML(parts[1]);
                    System.out.println("Successfully opened " + parts[1]);
                } catch (Exception e) {
                    System.out.println("Error opening file: " + e.getMessage());
                }
                break;

            case "print":
                printXML(root, 0);
                break;

            case "select":
                selectAttribute(parts[1], parts[2]);
                break;

            case "set":
                setAttribute(parts[1], parts[2], parts[3]);
                break;

            case "children":
                listChildren(parts[1]);
                break;

            case "child":
                getChild(parts[1], Integer.parseInt(parts[2]));
                break;

            case "text":
                getText(parts[1]);
                break;

            case "delete":
                deleteAttribute(parts[1], parts[2]);
                break;

            case "newchild":
                addNewChild(parts[1]);
                break;

            case "save":
                try {
                    saveToFile(parts[1]);
                    System.out.println("Successfully saved " + parts[1]);
                } catch (IOException e) {
                    System.out.println("Error saving file: " + e.getMessage());
                }
                break;

            case "exit":
                System.out.println("Exiting...");
                System.exit(0);
                break;
            case "close":
                root = null;
                System.out.println("Successfully closed file.");
                break;

            case "saveas":
                try {
                    saveToFile(parts[1]);
                } catch (IOException e) {
                    System.out.println("Error saving file: " + e.getMessage());
                }
                break;

            case "help":
                System.out.println("The following commands are supported:");
                System.out.println("open <file>       - opens <file>");
                System.out.println("close             - closes currently opened file");
                System.out.println("save              - saves the currently open file");
                System.out.println("saveas <file>     - saves the currently open file in <file>");
                System.out.println("help              - prints this information");
                System.out.println("exit              - exits the program");
                System.out.println("print             - prints the XML tree");
                System.out.println("select <id> <key> - prints value of attribute");
                System.out.println("set <id> <key> <value> - sets an attribute");
                System.out.println("delete <id> <key> - deletes an attribute");
                System.out.println("text <id>         - prints text content of node");
                System.out.println("children <id>     - lists children of node");
                System.out.println("child <id> <index> - shows child at index");
                System.out.println("newchild <id>     - adds new empty child");
                break;

            default:
                System.out.println("Invalid command.");
        }
    }

    private void printXML(XMLNode node, int indent) {
        if (node == null)
            return;
        System.out.println(" ".repeat(indent) + "<" + node.getTagName() + ">");
        for (XMLNode child : node.getChildren()) {
            printXML(child, indent + 2);
        }
        System.out.println(" ".repeat(indent) + "</" + node.getTagName() + ">");
    }

    private void selectAttribute(String id, String key) {
        XMLNode node = findNode(root, id);
        if (node != null && node.getAttributes().containsKey(key)) {
            System.out.println(node.getAttributes().get(key));
        } else {
            System.out.println("Attribute not found.");
        }
    }

    private void setAttribute(String id, String key, String value) {
        XMLNode node = findNode(root, id);
        if (node != null) {
            node.setAttribute(key, value);
            System.out.println("Attribute set.");
        } else {
            System.out.println("Node not found.");
        }
    }

    private void listChildren(String id) {
        XMLNode node = findNode(root, id);
        if (node != null) {
            for (XMLNode child : node.getChildren()) {
                System.out.println(child.getTagName() + " (id: " + child.getId() + ")");
            }
        } else {
            System.out.println("Node not found.");
        }
    }

    private void getChild(String id, int index) {
        XMLNode node = findNode(root, id);
        if (node != null && index < node.getChildren().size()) {
            System.out.println("Child: " + node.getChildren().get(index).getTagName());
        } else {
            System.out.println("Child not found.");
        }
    }

    private void getText(String id) {
        XMLNode node = findNode(root, id);
        if (node != null) {
            System.out.println("Text: " + node.getTextContent());
        } else {
            System.out.println("Node not found.");
        }
    }

    private void deleteAttribute(String id, String key) {
        XMLNode node = findNode(root, id);
        if (node != null) {
            node.removeAttribute(key);
            System.out.println("Attribute deleted.");
        } else {
            System.out.println("Node not found.");
        }
    }

    private void addNewChild(String id) {
        XMLNode node = findNode(root, id);
        if (node != null) {
            XMLNode newChild = new XMLNode("newChild", UUID.randomUUID().toString());
            node.addChild(newChild);
            System.out.println("New child added.");
        } else {
            System.out.println("Node not found.");
        }
    }

    private XMLNode findNode(XMLNode node, String id) {
        if (node == null)
            return null;
        if (node.getId().equals(id))
            return node;
        for (XMLNode child : node.getChildren()) {
            XMLNode found = findNode(child, id);
            if (found != null)
                return found;
        }
        return null;
    }

    private void saveToFile(String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        saveXML(root, writer, 0);
        writer.close();
        System.out.println("File saved successfully.");
    }

    private void saveXML(XMLNode node, BufferedWriter writer, int indent) throws IOException {
        if (node == null)
            return;

        String indentStr = " ".repeat(indent);
        StringBuilder tag = new StringBuilder(indentStr + "<" + node.getTagName());

        for (Map.Entry<String, String> attr : node.getAttributes().entrySet()) {
            tag.append(" ").append(attr.getKey()).append("=\"").append(attr.getValue()).append("\"");
        }

        tag.append(">");

        if (!node.getTextContent().isEmpty()) {
            tag.append(node.getTextContent());
        }

        writer.write(tag.toString());
        writer.newLine();

        for (XMLNode child : node.getChildren()) {
            saveXML(child, writer, indent + 2);
        }

        writer.write(indentStr + "</" + node.getTagName() + ">\n");
    }
}
