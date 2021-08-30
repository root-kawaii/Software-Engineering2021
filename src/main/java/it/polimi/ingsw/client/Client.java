package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.shared.DevelopmentCardsGetter;
import it.polimi.ingsw.shared.LeaderCardsGetter;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;
import it.polimi.ingsw.view.Board;
import it.polimi.ingsw.view.Card;
import it.polimi.ingsw.view.Table;
import it.polimi.ingsw.model.Resource;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

// Basic Client class with attributes and methods shared by CLI and GUI

/**
 * Abstract Client class, CLI and Gui expand and implement this class.
 * Methods are called by the client's Parser.
 * @see it.polimi.ingsw.client.CLI.CLI
 * @see it.polimi.ingsw.client.GUI.GUI
 * @see Parser
 */
public abstract class Client implements Runnable{
    private boolean quit;
    private ArrayList<Command> commandQueue;
    private Socket socket;
    private PrintWriter out;
    private int clientID;
    private String username;
    private Table table;
    private HashMap<Integer, Board> boards; // key is clientID
    private Card[] cards;
    private Thread listener;
    private Gson gson;
    private Parser parser;

    /**
     * Class constructor for online games.
     * Connects to the specified address and sets up objects needed to play.
     * @param host The address of the server
     * @param port The port of the server
     */
    public Client(String host, int port) throws IOException{
        this.socket = new Socket(host, port);
        this.out = new PrintWriter(this.socket.getOutputStream());
        this.username = null;
        this.clientID = 0;
        setup();
    }

    /**
     * Class constructor for local matches.
     * Connects to the local server started by the Launcher and sets up for a local game.
     * @see it.polimi.ingsw.Launcher
     */
    public Client() {
        try {
            this.socket = new Socket("localhost", 12345);
            Scanner in = new Scanner(this.socket.getInputStream());
            this.out = new PrintWriter(this.socket.getOutputStream());
            in.nextLine(); // CONNECTIONOK
            this.clientID = 1;
            in.nextLine(); // ASKMAXPLAYERS
            this.out.println("{\"clientID\":1,\"message\":\"SENDMAXPLAYERS\",\"args\":[\"1\"]}");
            this.out.flush();
        } catch (IOException exception) {
            System.out.println("Couldn't connect to local server.");
        }
        setup();
    }

    private void setup(){
        this.quit = false;
        Card[] devCards = new DevelopmentCardsGetter().getViewCards();
        Card[] leaderCards = new LeaderCardsGetter().getViewCards();
        this.cards = new Card[64];
        for (Card card : devCards) {
            this.cards[card.getID() - 1] = card;
        }
        for (Card card : leaderCards) {
            this.cards[card.getID() - 1] = card;
        }
        this.table = new Table(cards);
        this.boards = new HashMap<>();
        this.commandQueue = new ArrayList<Command>();
        this.gson = new Gson();
        this.parser = new Parser(this);
        listener = new Thread(new ServerListener(this.socket, this));
        listener.start();
    }

    /**
     * Adds a Command to the queue.
     * @param command The Command to add
     * @see Command
     */
    public void addCommand(Command command) {
        synchronized (commandQueue) {
            commandQueue.add(command);
        }
    }

    /**
     * Implementation should ask the player if they wish to reconnect as the specified player.
     * @param username The name of the player
     */
    public abstract void reconnect(String username);

    /**
     * Updates the state of the game after reconnecting.
     * @param clientID ID of the player being updated
     * @param args List of everything needed to synchronize the client with the server
     */
    public void updateStatus(int clientID, String[] args) {
        if (clientID == 0) {
            setDecks(Arrays.copyOfRange(args, 0, 12));
            setMarbles(Arrays.copyOfRange(args, 12, 25));
        } else {
            boards.put(clientID, new Board(String.join(" ", Arrays.copyOfRange(args, 27, args.length))));
            for (int i = 0; i < 9; i++) {
                if (i < 3 && !args[i].equals("null")) {
                    boards.get(clientID).setDevelopmentCards(0, cards[Integer.parseInt(args[i]) - 1]);
                } else if (i < 6 && !args[i].equals("null")) {
                    boards.get(clientID).setDevelopmentCards(1, cards[Integer.parseInt(args[i]) - 1]);
                } else if (i < 9 && !args[i].equals("null")) {
                    boards.get(clientID).setDevelopmentCards(0, cards[Integer.parseInt(args[i]) - 1]);
                }
            }
            if (!args[9].equals("null")) boards.get(clientID).setLeaderCards(0, cards[Integer.parseInt(args[9]) - 1]);
            if (!args[10].equals("null")) boards.get(clientID).setLeaderCards(1, cards[Integer.parseInt(args[10]) - 1]);
            if (clientID == getClientID() && !args[11].equals("null")) boards.get(clientID).setHandCard(0, cards[Integer.parseInt(args[11]) - 1]);
            if (clientID == getClientID() && !args[12].equals("null")) boards.get(clientID).setHandCard(1, cards[Integer.parseInt(args[12]) - 1]);
            boards.get(clientID).setVictoryPoints(Integer.parseInt(args[13]));
            boards.get(clientID).setFaith(Integer.parseInt(args[14]));
            int i = 15;
            for (Resource resource : Resource.values()) {
                if (resource == Resource.FAITH || resource == Resource.CHOICE) {}
                else {
                    if (!args[i].equals("null")) {
                        if (Integer.parseInt(args[i]) != 0) boards.get(clientID).getShelves().put(resource, Integer.parseInt(args[i]));
                    }
                    i++;
                }
            }
            for (Resource resource : Resource.values()) {
                if (resource == Resource.FAITH || resource == Resource.CHOICE) {}
                else {
                    if (!args[i].equals("null")) {
                        if (Integer.parseInt(args[i]) != 0) boards.get(clientID).getStrongBox().put(resource, Integer.parseInt(args[i]));
                    }
                    i++;
                }
            }
            if (!args[23].equals("null")) {
                boards.get(clientID).getExtra().put(Resource.valueOf(args[23]), Integer.parseInt(args[24]));
            }
            if (!args[25].equals("null")) {
                boards.get(clientID).getExtra().put(Resource.valueOf(args[23]), Integer.parseInt(args[24]));
            }
        }
    }

    /**
     * Updates the client ID of a player after they reconnect.
     * @param old The old Client ID
     * @param current The new Client ID
     */
    public void updateID(int old, int current) {
        boards.put(current, boards.get(old));
        boards.remove(old);
    }

    /**
     * Returns the game's Table.
     * @return the Table
     * @see Table
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * Returns this client's ID.
     * @return the client's ID
     */
    public int getClientID(){
        return this.clientID;
    }

    /**
     * Returns all cards available in the game.
     * @return An Array of all cards
     * @see Card
     */
    public Card[] getCards() {
        return cards;
    }

    /**
     * Returns this client's username.
     * @return the client's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the Boards of all players.
     * @return A Map of all Boards
     */
    public HashMap<Integer, Board> getBoards() {
        return boards;
    }

    /**
     * Returns a specific Board.
     * @param clientID the ID of the player
     * @return the player's Board
     * @see Board
     */
    public Board getBoard(int clientID) {
        return boards.get(clientID);
    }

    /**
     * Sets the client's username and sends it to the server.
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
        Command command = new Command(clientID, Messages.SENDUSERNAME, username);
        sendMessage(command);
    }

    /**
     * Sends a Command to the server.
     * @param command the command to send
     * @see Command
     */
    public void sendMessage(Command command) {
        String message = gson.toJson(command);
        this.out.println(message);
        this.out.flush();
    }

    /**
     * On a successful connection, sets the client's ID.
     * @param clientID The client's ID
     */
    public void connectionOk(int clientID) {
        this.clientID = clientID;
    }

    /**
     * Implementation should ask how many players can join the Lobby.
     */
    public abstract void askMaxPlayers();

    /**
     * Implementation should ask for a username and proceed to set it.
     */
    public abstract void askUsername();

    /**
     * Implementation should notify the player of the error and ask to choose again.
     */
    public abstract void invalidUsername();

    /**
     * Puts Marbles in the Market.
     * @param marbles A String Array of all the colours of the marbles and the extra marble
     */
    public void setMarbles(String[] marbles) {
        table.setMarket(marbles);
    }

    /**
     * Sets the Cards available for purchase in the market.
     * @param cardIDs A String Array of all the cards' IDs
     */
    public void setDecks(String[] cardIDs) {
        table.setDecks(cardIDs);
    }

    /**
     * Implementation should ask to pick two cards between the available ones.
     * @param cardIDs A String Array of the card IDs to choose from
     */
    public abstract void askLeader(String[] cardIDs);

    /**
     * Creates Boards for all players.
     * @param args A String Array of the players' usernames and IDs
     */
    public void startGame(String[] args){
        for (int i = 0; i < args.length; i += 2) {
            Board board = new Board(args[i]);
            boards.put(Integer.parseInt(args[i + 1]), board);
        }
    }

    /**
     * Implementation should notify the user of the winner of the game.
     * @param clientID the ID of the winner
     */
    public void endGame(int clientID,String username) {
        quit = true;
        try {
            socket.close();
        } catch (IOException exception) {
            System.out.println("Couldn't close socket.");
        }
    }

    /**
     * Implementation should let the player take an action if it's their turn.
     * @param clientID  the ID of the player starting their turn.
     */
    public abstract void startTurn(int clientID);

    /**
     * Implementation should let the player pick resources at the start of the game.
     * @param clientID The ID of the player picking resources
     * @param amount The amount of resources the player can pick
     */
    public abstract void chooseResource(int clientID, int amount);

    /**
     * Implementation should notify the active player they did something wrong and need to try again.
     * @param clientID ID of the player who made the mistake
     */
    public abstract void invalidMove(int clientID);

    /**
     * Puts Resources to be handled in the temporary storage.
     * @param clientID ID of the client that received the resources
     * @param resources An ArrayList of Resources to put in temporary storage
     */
    public void setTemp(int clientID, ArrayList<Resource> resources) {
        boards.get(clientID).setTemp(resources);
    }

    /**
     * Signals that a  user received resources and needs to handle them.
     * @param clientID ID of the client that should handle the resources
     */
    public abstract void resourceGain(int clientID);

    /**
     * Means a resource has been successfully moved to Storage.
     * Implementation should proceed handling resources.
     * @param clientID ID of the client adding the resources.
     */
    public abstract void okAddResource(int clientID);

    /**
     * Means a resource couldn't be added to storage.
     * Implementation should add the resource at the start of the temporary storage.
     * @param clientID ID of the client handling resources
     */
    public abstract void koAddResource(int clientID);

    /**
     * Sets the specified client's faith points.
     * @param clientID ID of the client receiving points
     * @param points the total amount of points
     */
    public void setFaithPoints(int clientID, int points) {
        if (clientID == 0) {
            boards.get(this.clientID).setCPUFaith(points);
        } else {
            boards.get(clientID).setFaith(points);
        }
    }

    /**
     * Sets the specified client's vitcory points.
     * @param clientID ID of the client
     * @param points total amount of points
     */
    public void setVictoryPoints(int clientID, int points) {
        boards.get(clientID).setVictoryPoints(points);
    }

    /**
     * Adds a card to a certain column on the player's board.
     * @param clientID ID of the player
     * @param cardID ID of the card
     * @param column column to add the card to
     */
    public void addCard(int clientID, int cardID, int column) {
        boards.get(clientID).setDevelopmentCards(column, cards[cardID-1]);
    }

    /**
     * Adds a leader to a certain column on the player's board.
     * @param clientID ID of the player
     * @param cardID ID of the card
     * @param column column to add the card to
     */
    public void addLeader(int clientID, int cardID, int column) {
        boards.get(clientID).setLeaderCards(column, cards[cardID-1]);
    }

    /**
     * Puts an amount of resource in the storage, removes it if empty.
     * @param clientID the ID of the affected player
     * @param resource the resource to set
     * @param amount the amount to set
     */
    public void setResource(int clientID, Resource resource, int amount) {
        if (amount != 0) {
            boards.get(clientID).getShelves().put(resource, amount);
        } else {
            boards.get(clientID).getShelves().remove(resource);
        }
    }

    /**
     * Puts a certain resource in a player's strongbox.
     * @param clientID the ID of the affected player
     * @param resource the resource to set
     * @param amount the amount to set
     */
    public void setStrongbox(int clientID, Resource resource, int amount) {
        boards.get(clientID).getStrongBox().put(resource, amount);
    }

    /**
     * Puts a certain resource in a player's extra storage.
     * @param clientID the ID of the affected player
     * @param resource the resource to set
     * @param amount the amount to set
     */
    public void setExtra(int clientID, Resource resource, int amount) {
        boards.get(clientID).getExtra().put(resource, amount);
    }

    /**
     * Sets a card available for purchase.
     * @param index Index of the deck
     * @param cardID ID of the card, -1 means no card
     */
    public void setMarketCard(int index, int cardID) {
        if (cardID != -1) {
            getTable().setDeck(index, cards[cardID-1]);
        } else {
            getTable().setDeck(index, null);
        }
    }

    /**
     * Called when a command isn't implemented yet.
     * Shouldn't appear during normal play.
     * Implementation should show the Command
     * @param command The command that couldn't be handled
     * @see Command
     */
    public abstract void generic(Command command);

    /**
     * Used by the Server Listener to decide if it should quit on socket close.
     * @return should it quit
     */
    public boolean getQuit() {
        return !quit;
    }

    // Keeping the call to parse outside the synchronized block to release the lock as soon as possible

    /**
     * Main loop, gets a Command from the queue and calls the Parser.
     * @see Command
     * @see Parser
     */
    @Override
    public void run() {
        Command command = null;
        while (!quit) {
            synchronized (commandQueue) {
                if (!commandQueue.isEmpty()) {
                    command = commandQueue.remove(0);
                }
            }
            if (command != null) {
                parser.parse(command);
            }
            command = null;
        }
    }
}
