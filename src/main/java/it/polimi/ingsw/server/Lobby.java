package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.controller.MarketController;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.util.ArrayList;
import java.util.logging.Logger;

// Handles the game

/**
 * Represents a game.
 * Allows to setup all components needed for a match and to start one.
 */
public class Lobby implements Runnable {
    private static final Logger logger = Logger.getGlobal();
    private Server server;
    private boolean running; // Is the game running or is it being set up?
    private boolean quit;
    private int lobbyID;
    private int playerMax;
    private ArrayList<ClientHandler>clients;
    private ArrayList<Command> commandQueue;
    private Parser parser;
    private Match match;
    private MainController mainController;
    private PlayerController playerController;
    private MarketController marketController;
    private BoardController boardController;

    /**
     * Class constructor.
     * @param lobbyID A number identifying the Lobby
     */
    public Lobby(int lobbyID, Server server) {
        this.server = server;
        this.running = false;
        this.quit = false;
        this.lobbyID = lobbyID;
        this.playerMax = 0;
        this.clients = new ArrayList<ClientHandler>();
        this.commandQueue = new ArrayList<Command>();
        this.parser = new Parser(this);
    }

    /**
     * Sends a Command to all players.
     * @param command the Command to send
     * @see Command
     * @see ClientHandler
     */
    public void updateClients(Command command) {
        for (ClientHandler client : this.clients) {
            client.sendMessage(command);
        }
    }

    private ClientHandler getClientByID(int clientID) {
        synchronized (clients) {
            return clients.stream().filter(clients -> clientID == clients.getClientID()).findFirst().orElse(null);
        }
    }

    // Add player to the lobby, set up commands for username and max players.

    /**
     * Adds a player to the Lobby.
     * @param client the ClientHandler to add to the Lobby
     */
    public void addPlayer(ClientHandler client) {
        Command command;
        if (!running) {
            synchronized (clients) {
                this.clients.add(client);
                client.setLobby(this);
                logger.info("Added player " + client.getClientID() + " to Lobby " + this.lobbyID);
                synchronized (commandQueue) {
                    if (this.clients.size() == 1) {
                        command = new Command(client.getClientID(), Messages.ASKMAXPLAYERS, null);
                        this.commandQueue.add(command);
                    }
                    command = new Command(client.getClientID(), Messages.ASKUSERNAME, null);
                    this.commandQueue.add(command);
                }
            }
        }
    }

    /**
     * Simply adds a ClientHandler to the list of clients.
     * Used when a player reconnects.
     * @param clientHandler The ClientHandler to insert
     */
    public void insertPlayer(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    /**
     * Adds a command to the Lobby's queue.
     * @param command The Command to add.
     * @see Command
     */
    public void addCommand(Command command) {
        synchronized (commandQueue) {
            commandQueue.add(command);
        }
    }

    /**
     * Checks if all players are ready.
     */
    private void checkSetup() {
        boolean sizeCheck;
        boolean nameCheck = false;
        sizeCheck = playerMax != 0 && clients.size() == playerMax;
        for (ClientHandler client : clients) {
            nameCheck = !client.getUsername().equals("");
            if (!nameCheck)  {
                break;
            }
        }
        if (sizeCheck && nameCheck) {
            startGame();
        }
    }

    /**
     * Performs initial steps needed to start the game.
     * The method creates the game's controllers and performs one time actions needed to start the game.
     */
    private void startGame() { // Set up and ask everything needed to start the match.
        match = new Match(false, this);
        mainController = new MainController(match,this);
        playerController = new PlayerController(match, this,mainController);
        marketController = new MarketController(match,this, mainController);
        boardController = new BoardController(match,this);
        if (!running) {
            Command command;
            String[] usernames = new String[playerMax * 2];
            int turn = 0;
            for (ClientHandler client : clients) {
                mainController.addPlayer(client.getUsername(),client.getClientID());
                usernames[turn] = client.getUsername();
                usernames[turn + 1] = String.valueOf(client.getClientID());
                turn += 2;
            }
            parser.setGame(match, mainController, playerController, marketController, boardController);
            playerController.askLeaders();
            command = new Command(0, Messages.STARTGAME, String.join(" ", usernames));
            updateClients(command);
            if (getCurrentPlayers() > 1) {
                command = new Command(clients.get(1).getClientID(), Messages.CHOOSERESOURCE, "1");
                clients.get(1).sendMessage(command);
            }
            if (getCurrentPlayers() > 2) {
                match.getPlayer(clients.get(2).getClientID()).addFaithPoints(1);
                command = new Command(clients.get(2).getClientID(), Messages.CHOOSERESOURCE, "1");
                clients.get(2).sendMessage(command);
            }if (getCurrentPlayers() > 3) {
                command = new Command(clients.get(3).getClientID(), Messages.CHOOSERESOURCE, "2");
                clients.get(3).sendMessage(command);
                match.getPlayer(clients.get(3).getClientID()).addFaithPoints(1);
            }
            running = true;
            logger.info("Starting Game in Lobby" + lobbyID);
            mainController.startTurn();
        }
    }

    /**
     * Ends the game and removes the lobby from the active ones.
     */
    public void endGame() {
        try {
            Thread.sleep(3000);
            for (ClientHandler client : clients) {
                client.close();
            }
            server.removeLobby(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asks a player how many peoplecan join the Lobby.
     * @param clientID The ID of the client who will be queried
     * @see ClientHandler
     */
    public void askMaxPlayers(int clientID) {
        ClientHandler client = getClientByID(clientID);
        Command command = new Command(0, Messages.ASKMAXPLAYERS, null);
        client.sendMessage(command);
    }

    /**
     * Checks and sets the number of players allowed in the Lobby.
     * If the number is invalid the player is asked to pick again.
     * @param maxPlayers The number of players.
     */
    public void sendMaxPlayers(int maxPlayers) {
        if (!running) {
            if (maxPlayers > 0 && maxPlayers <= 4) {
                this.playerMax = maxPlayers;
            } else {
                Command command = new Command(0, Messages.INVALIDMAXPLAYERS, null);
                clients.get(0).sendMessage(command); // The first player always picks the number
            }
        }
    }

    /**
     * Returns the game's main controller.
     * Called by the server to check if players are connected.
     * @return The main Controller
     * @see Server
     * @see MainController
     */
    public MainController getMainController() {
        return mainController;
    }

    /**
     * Removes a ClientHandler in case of a disconnection as it wouldn't be usable anymore.
     * @param clientHandler The ClientHandler to remove
     * @see ClientHandler
     */
    public void disconnection(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    /**
     * Gets how many clients can join the Lobby.
     * @return The maximum number of players
     */
    public int getMaxPlayers() {
        return this.playerMax;
    }

    /**
     * Gets how many players are currently in the Lobby.
     * @return The current amount of players.
     */
    public int getCurrentPlayers() {
        return this.clients.size();
    }

    /**
     * Asks a player to choose a username.
     * @param clientID The ID of the player to be queried
     * @see ClientHandler
     */
    public void askUsername(int clientID) {
        ClientHandler client = getClientByID(clientID);
        Command command = new Command(client.getClientID(), Messages.ASKUSERNAME, null);
        client.sendMessage(command);
    }

    // Check if the username is already in use then set it or ask again.

    /**
     * Checks and sets a player's username.
     * If the username is invalid (already taken) the player is asked to choose again.
     * @param clientID The ID of the player
     * @param username The chosen username
     * @see ClientHandler
     */
    public void sendUsername(int clientID, String username) {
        if (!running) {
            boolean valid = true;
            for (ClientHandler client : clients) {
                if (client.getUsername().equals(username)) {
                    valid = false;
                    break;
                }
            }
            ClientHandler client = getClientByID(clientID);
            if (valid) {
                client.setUsername(username); // After every client is completely set up check if the game is ready to start.
                checkSetup();
            } else {
                Command command = new Command(0, Messages.INVALIDUSERNAME, null);
                client.sendMessage(command);
            }
        }
    }

    /**
     * Returns if the game is running or being set up.
     * @return The game status
     */
    public boolean getRunning() {
        return running;
    }

    /**
     * Main loop.
     * Works through the queue of commands and calls the Parser.
     * @see Parser
     */
    public void run() {
        while (!quit) {
            synchronized (commandQueue) {
                if (!commandQueue.isEmpty()) {
                    parser.parse(commandQueue.remove(0));
                }
            }
        }
    }
}
