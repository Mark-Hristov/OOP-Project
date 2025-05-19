import java.util.*;

public class XMLNode {
    private String name;
    private String id;
    private String text;
    private Map<String, String> attributes;
    private List<XMLNode> children;

    public XMLNode(String name) {
        this.name = name;
        this.attributes = new LinkedHashMap<>();
        this.children = new ArrayList<>();
        this.text = "";
    }

    public void setId(String id) {
        this.id = id;
        attributes.put("id", id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setText(String text) {
        this.text = text.trim();
    }

    public String getText() {
        return text;
    }

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public void deleteAttribute(String key) {
        attributes.remove(key);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public List<XMLNode> getChildren() {
        return children;
    }

    public void addChild(XMLNode child) {
        children.add(child);
    }
}
