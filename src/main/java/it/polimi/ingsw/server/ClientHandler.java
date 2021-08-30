package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Represents a player in a Lobby and on the Server.
 * @see Server
 * @see Lobby
 */
public class ClientHandler implements Runnable {
    private Server server;
    private Lobby lobby;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private int clientID;
    private String username;
    private Gson gson;

    /**
     * Class constructor.
     * @param socket The socket to this client
     * @param clientID A number identifying the client
     */
    public ClientHandler(Socket socket, int clientID, Server server){
        this.server = server;
        this.socket = socket;
        this.gson = new Gson();
        this.clientID = clientID;
        this.username = "";
        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream());
        }  catch (IOException exception) {
            System.out.println("Couldn't set up ClientHandler.");
        }
        this.out.println(String.format("{\"clientID\":0,\"message\":\"CONNECTIONOK\",\"args\":[\"%s\"]}", clientID));
        this.out.flush(); // Tell the client the connection was established, send its clientID as argument.
    }

    /**
     * Gets the ID of the client.
     * @return the ID of the client
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * Sets the Lobby in which the client is placed.
     * @param lobby The Lobby where the client is put
     */
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * Gets the client's username.
     * @return The client's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the client's username.
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sends a Command to the client.
     * @param command The Command to send
     * @see Command
     */
    public void sendMessage(Command command) {
        String message = gson.toJson(command);
        this.out.println(message);
        this.out.flush();
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println("Couldn't close socket.");
        }
    }

    /**
     * Main loop; Creates a Command when data is received.
     * @see Command
     */
    @Override
    public void run() {
        while (!socket.isClosed() && this.in.hasNextLine()) {
            String message = this.in.nextLine();
            Command command = gson.fromJson(message, Command.class);
            if (command.getMessage() == Messages.OKRECONNECT) {
                server.okreconnect();
            } else if (command.getMessage() == Messages.KORECONNECT) {
                server.koreconnect();
            } else {
                lobby.addCommand(command);
            }
        }
        Command command = new Command(clientID, Messages.DISCONNECTION, null);
        lobby.addCommand(command);
        lobby.disconnection(this);
    }
}
