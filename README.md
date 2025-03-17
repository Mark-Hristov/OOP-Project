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
private void handleCommand(String input) {
        String[] args = input.split(" ");
        String command = args[0].toLowerCase();
        switch (command) {
            case "open":
                if (args.length > 1) openFile(args[1]);
                else System.out.println("No file specified.");
                break;
            case "close":
                closeFile();
                break;
            case "save":
                saveFile();
                break;
            case "saveas":
                if (args.length > 1) saveFileAs(args[1]);
                else System.out.println("No file name specified.");
                break;
            case "print":
                printXML();
                break;
            case "select":
                if (args.length > 2) selectAttribute(args[1], args[2]);
                break;
            case "set":
                if (args.length > 3) setAttribute(args[1], args[2], args[3]);
                break;
            case "children":
                if (args.length > 1) showChildren(args[1]);
                break;
            case "child":
                if (args.length > 2) showChild(args[1], Integer.parseInt(args[2]));
                break;
            case "text":
                if (args.length > 1) showText(args[1]);
                break;
            case "delete":
                if (args.length > 2) deleteAttribute(args[1], args[2]);
                break;
            case "newchild":
                if (args.length > 1) addNewChild(args[1]);
                break;
            case "xpath":
                if (args.length > 2) executeXPath(args[1], args[2]);
                break;
            case "help":
                showHelp();
                break;
            default:
                System.out.println("Unknown command. Type 'help' for available commands.");
        }
    }
