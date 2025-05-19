public class Main {
    public static void main(String[] args) {
        XMLCommandProcessor processor = new XMLCommandProcessor();
        processor.processCommand("open test.xml");
        processor.processCommand("print");
        
    }
}
