package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.shared.Command;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client's Listener, should be run in its own Thread.
 * When receiving messages from the server it creates a command and adds it to the queue.
 */
public class ServerListener implements Runnable{
    private Socket socket;
    private Scanner in;
    private Client client;
    private boolean quit;
    private Gson gson;

    /**
     * Class constructor.
     * @param socket Socket to liston from
     * @param client Reference to the client the listener is bound to
     * @see Client
     */
    public ServerListener(Socket socket, Client client) {
        this.quit = true;
        this.client = client;
        this.gson = new Gson();
        this.socket = socket;
        try {
            this.in = new Scanner(socket.getInputStream());
        } catch (IOException exception) {
            System.out.println("Error setting up listener.");
            System.exit(0);
        }
    }


    /**
     * Main loop.
     * Waits for messages, creates a Command from them and adds them to the Client's queue.
     * @see Command
     */
    public void run() {
        while (!socket.isClosed() && in.hasNextLine()) {
            String message = in.nextLine();
            client.addCommand(gson.fromJson(message, Command.class));
        }
        if (client.getQuit()) {
            System.out.print("You have been disconnected.");
            System.exit(0);
        }
    }
}
