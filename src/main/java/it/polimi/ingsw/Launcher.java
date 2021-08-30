package it.polimi.ingsw;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.GUI.Display;
import it.polimi.ingsw.server.Server;
import static it.polimi.ingsw.shared.ANSI.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

/**
 * Start of the entire program.
 */
public class Launcher {
    public static final String TMPDIR = "java.io.tmpdir";
    public static final String ANSI = "enableANSI.dll";
    private static Scanner scan = new Scanner(System.in);
    private static String choice;
    private static native boolean enableANSI(); // tries to enable ANSI escape codes on Windows

    /**
     * Asks the player what they would like to start.
     * @param args Not Used
     * @throws IOException Thrown is something prevents the program to properly set up
     */
    public static void main(String[] args) throws IOException{
        if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) { // on Windows, extract DLL to Temp directory
            try {
                if (System.getProperty("sun.arch.data.model").equals("64")) {
                    Files.copy(ClassLoader.getSystemResourceAsStream("enableANSI64.dll"), Path.of(System.getProperty(TMPDIR) + ANSI), StandardCopyOption.REPLACE_EXISTING);
                } else if (System.getProperty("sun.arch.data.model").equals("32")) {
                    Files.copy(ClassLoader.getSystemResourceAsStream("enableANSI32.dll"), Path.of(System.getProperty(TMPDIR) + ANSI), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (AccessDeniedException exception) {} // file already open, nothing else to do
            System.load(System.getProperty(TMPDIR) + ANSI);
            enableANSI();
        }

        System.out.print(textWhite + BGBlack + clearScreen + placeCursor(1, 1));
        System.out.println("Masters of Renaissance");
        System.out.println("Would you like to start the Server (1), the CLI (2), or the GUI (3)?");
        try {
            choice = scan.nextLine();
            switch (Integer.parseInt(choice)) {
                case 1:
                    Server.main(null);
                    break;
                case 2:
                    if (online()) {
                        CLI.main(null);
                    } else {
                        Server.main(new String[]{"local"});
                        CLI.main(new String[]{"local"});
                    }
                    break;
                case 3:
                    if (online()) {
                        Display.main(null);
                    } else {
                        Server.main(new String[]{"local"});
                        Display.main(new String[]{"local"});
                    }
                    break;
                default:
                    System.out.println("Invalid option, exiting.");
                    System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("Invalid option, exiting.");
            System.exit(0);
        }
    }

    private static boolean online() {
        System.out.println("Would you like to connect to a server? 1-Yes 2-No");
        choice = scan.nextLine();
        if (Integer.parseInt(choice) == 1) {
            return true;
        }
        return false;
    }
}
