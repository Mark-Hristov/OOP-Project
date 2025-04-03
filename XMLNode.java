import java.util.*;

class XMLNode {
    private String id;
    private String tagName;
    private Map<String, String> attributes;
    private List<XMLNode> children;
    private String textContent;

    public XMLNode(String tagName, String id) {
        this.tagName = tagName;
        this.id = id;
        this.attributes = new HashMap<>();
        this.children = new ArrayList<>();
        this.textContent = "";
    }

    public String getId() { return id; }
    public String getTagName() { return tagName; }
    public Map<String, String> getAttributes() { return attributes; }
    public List<XMLNode> getChildren() { return children; }
    public String getTextContent() { return textContent; }

    public void setAttribute(String key, String value) { attributes.put(key, value); }
    public void setTextContent(String text) { this.textContent = text; }
    public void addChild(XMLNode child) { children.add(child); }
    public void removeAttribute(String key) { attributes.remove(key); }
}