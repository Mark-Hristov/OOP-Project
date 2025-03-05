import java.util.*;
import java.io.*;

public class XMLParser {
    private XMLDocument document;
    private boolean isFileOpen = false;
    private String currentFilePath = "";

    public static void main(String[] args) {
        XMLParser parser = new XMLParser();
        Scanner scanner = new Scanner(System.in);
        System.out.println("XML Parser initialized. Enter a command (type 'help' for a list of commands):");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting XML Parser.");
                break;
            }
            parser.handleCommand(input);
        }
        scanner.close();
    }
