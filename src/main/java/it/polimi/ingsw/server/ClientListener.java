package it.polimi.ingsw.server;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

// Simple listener created at server startup, simply waits for a connection, creates a ClientHandler and adds it to the waiting list

/**
 * Client Listener started by the Server.
 * Awaits connections, creates ClientHandlers and adds them to the Server.
 * @see ClientHandler
 * @see Server
 */
public class ClientListener implements Runnable{
    private static Server server;
    private static final Logger logger = Logger.getGlobal();
    private static ServerSocket mainSocket;
    private static int clientID;

    /**
     * Class constructor.
     */
    public ClientListener(Server server){
        this.server = server;
        clientID = 1; // 0 will be used by the server when sending commands.
        try {
            mainSocket = new ServerSocket(12345);
            logger.info("Listening on port 12345.");
        } catch (IOException exception) {
            logger.info("Couldn't open socket.");
            System.exit(0);
        }
    }

    /**
     * Main loop that creates a ClientHandler upon connection.
     */
    @Override
    public void run() {
        while (!mainSocket.isClosed()) {
            try {
                Socket socket = mainSocket.accept();
                ClientHandler client = new ClientHandler(socket, clientID, server);
                logger.info("Client Connected, ID: " + clientID);
                server.addWaiting(client);
                Thread clientHandlerThread = new Thread(client);
                clientHandlerThread.start();
                clientID++;
            } catch (IOException exception) {
                logger.info("Socket error.");
                System.exit(0);
            }
        }
    }
}
