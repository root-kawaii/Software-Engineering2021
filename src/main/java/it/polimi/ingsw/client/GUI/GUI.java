package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.view.Card;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Implementation of Client to be used with the rest of the GUI.
 * @see Client
 */
public class GUI extends Client {
    private String username;
    private Display display;
    private HashMap<Integer, PlayerBoardController> sceneControllers;
    private TableController tableController;
    private Card[] hand = new Card[2];

    /**
     * Class constructor, connects to a server.
     * @param host The server address
     * @param port The server host
     * @param display The Display that created this client
     * @throws IOException If it fails to connect
     */
    public GUI(String host, int port, Display display) throws IOException {
        super(host, port);
        this.display = display;
        display.setGui(this);
        display.setup();
    }

    /**
     * Class constructor, connects to a local server for single player games.
     * @param display The display that created this client
     * @throws IOException If it fails to connect
     */
    public GUI(Display display) throws IOException {
        super();
        this.display = display;
        display.setGui(this);
        display.setup();
    }

    /**
     * Returns the player's hand.
     * @return The player's hand
     */
    public Card[] getHand() {
        return hand;
    }

    /**
     * Shows the max players screen.
     */
    @Override
    public synchronized void askMaxPlayers() {
        Platform.runLater(() -> {
            display.askMaxPlayers();
        });
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the username input screen.
     */
    @Override
    public synchronized void askUsername() {
        Platform.runLater(() -> {
            display.askUsername(false);
        });
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Notifies the player their username is not valid and lets them choose again.
     */
    @Override
    public synchronized void invalidUsername() {
        Platform.runLater(() -> {
            display.askUsername(true);
        });
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the player the leader choice screen.
     * @param cardIDs A String Array of the card IDs to choose from
     */
    @Override
    public synchronized void askLeader(String[] cardIDs) {
        int[] IDs = new int[4];
        for (int i = 0; i < 4; i++) {
            IDs[i] = Integer.parseInt(cardIDs[i]);
        }
        Platform.runLater(() -> {
            display.askLeaders(IDs);
        });
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lets the player start their turn.
     * @param clientID  the ID of the player starting their turn.
     */
    @Override
    public void startTurn(int clientID) {
        if (clientID == getClientID()) {
            Platform.runLater(() -> {
                tableController.enableMoves();
                sceneControllers.get(getClientID()).enableMoves();
            });
        }
    }

    /**
     * Sets username.
     * @param username The username
     * @param reconnect Set if reconnecting
     */
    public void setUsername(String username, boolean reconnect) {
        if (reconnect)
            this.username = username;
        else {
            this.username = username;
            super.setUsername(username);
        }
    }

    /**
     * Updates the game status for the specified player.
     * @param clientID ID of the player being updated
     * @param args List of everything needed to synchronize the client with the server
     */
    @Override
    public void updateStatus(int clientID, String[] args) {
        Platform.runLater(() -> {
            super.updateStatus(clientID, args);
            display.startGame();
            if (clientID == 0) {
                tableController.setDecks(Arrays.copyOfRange(args, 0, 12));
                tableController.setMarbleImages(Arrays.copyOfRange(args, 12, 25));
            } else {
                for (int i = 0; i < 9; i++) {
                    if (i < 3 && !args[i].equals("null")) {
                        sceneControllers.get(clientID).addCard(clientID, getCards()[Integer.parseInt(args[i]) - 1].getID(), 0);
                    } else if (i < 6 && !args[i].equals("null")) {
                        sceneControllers.get(clientID).addCard(clientID, getCards()[Integer.parseInt(args[i]) - 1].getID(), 1);
                    } else if (i < 9 && !args[i].equals("null")) {
                        sceneControllers.get(clientID).addCard(clientID, getCards()[Integer.parseInt(args[i]) - 1].getID(), 2);
                    }
                }
                if (!args[9].equals("null")) sceneControllers.get(clientID).addLeader(getCards()[Integer.parseInt(args[9]) - 1].getID(), 0);
                if (!args[10].equals("null")) sceneControllers.get(clientID).addLeader(getCards()[Integer.parseInt(args[10]) - 1].getID(), 1);
                sceneControllers.get(clientID).setFaith(Integer.parseInt(args[14]));
                sceneControllers.get(clientID).updateResources(clientID);
                sceneControllers.get(clientID).updateStrong(clientID);
                sceneControllers.get(clientID).updateExtra(clientID);
                sceneControllers.get(clientID).setUsername(String.join(" ", Arrays.copyOfRange(args, 27, args.length)));
            }
        });
    }

    /**
     * Updates a client ID after a player reconnects.
     * @param old The old Client ID
     * @param current The new Client ID
     */
    @Override
    public void updateID(int old, int current) {
        super.updateID(old, current);
        Platform.runLater(() -> {
            display.updateID(old, current);
            sceneControllers.put(current, sceneControllers.get(old));
            sceneControllers.remove(old);
        });
    }

    /**
     * Notifies the player the move they made is invalid and lets them try again.
     * @param clientID ID of the player who made the mistake
     */
    @Override
    public void invalidMove(int clientID) {
        if (clientID == getClientID()) {
            Platform.runLater(() -> {
                tableController.enableMoves();
                sceneControllers.get(getClientID()).enableMoves();
                sceneControllers.get(getClientID()).invalidMove();
            });
        }
    }

    /**
     * Sets the player's faith points.
     * @param clientID ID of the client receiving points
     * @param points the total amount of points
     */
    @Override
    public void setFaithPoints(int clientID, int points) {
        super.setFaithPoints(clientID, points);
        Platform.runLater(() -> {
            if (clientID == 0) {
                sceneControllers.get(getClientID()).setCPUFaith(points);
            } else {
                sceneControllers.get(clientID).setFaith(points);
            }
        });
    }

    /**
     * Lets the player start handling resource after inserting a marble.
     * @param clientID ID of the client that should handle the resources
     */
    @Override
    public void resourceGain(int clientID) {
        if (clientID == getClientID()) {
            Platform.runLater(() -> {
                sceneControllers.get(clientID).enableTemp();
            });
        }
    }

    /**
     * Notifies the player they should continue handling resources.
     * @param clientID ID of the client adding the resources.
     */
    @Override
    public void okAddResource(int clientID) {
        if (clientID == getClientID()) {
            Platform.runLater(() -> {
                sceneControllers.get(getClientID()).enableTemp();
            });
        }
    }

    /**
     * Notifies the player their move was not valid and lets them try again.
     * @param clientID ID of the client handling resources
     */
    @Override
    public void koAddResource(int clientID) {
        if (clientID == getClientID()) {
            Platform.runLater(() -> {
                sceneControllers.get(getClientID()).enableTemp();
                sceneControllers.get(getClientID()).invalidMove();
            });
        }
    }

    /**
     * Sets up scenes and controllers then shows the game screen.
     * @param args A String Array of the players' usernames and IDs
     */
    @Override
    public void startGame(String[] args) {
        super.startGame(args);
        Platform.runLater(() -> {
            display.startGame();
        });
    }

    @Override
    public synchronized void reconnect(String username) {
        try {
            Platform.runLater(() -> {
                display.reconnect(username);
            });
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lets the player pick resources at the start of the game.
     * @param clientID The ID of the player picking resources
     * @param amount The amount of resources the player can pick
     */
    @Override
    public void chooseResource(int clientID, int amount) {
        if (clientID ==  getClientID()) {
            Platform.runLater(() -> {
                sceneControllers.get(getClientID()).chooseResources(amount);
            });
        }
    }

    /**
     * Sets marbles in the market.
     * @param marbles A String Array of all the colours of the marbles and the extra marble
     */
    @Override
    public void setMarbles(String[] marbles) {
        super.setMarbles(marbles);
        Platform.runLater(()->{
            tableController.setMarbleImages(marbles);
        });
    }

    /**
     * Sets cards available for purchase
     * @param cardIDs A String Array of all the cards' IDs
     */
    @Override
    public void setDecks(String[] cardIDs) {
        super.setDecks(cardIDs);
        Platform.runLater(() -> {
            tableController.setDecks(cardIDs);
        });
    }

    /**
     * Sets a specific card available for purchase.
     * @param index Index of the deck
     * @param cardID ID of the card, -1 means no card
     */
    @Override
    public void setMarketCard(int index, int cardID) {
        super.setMarketCard(index, cardID);
        Platform.runLater(() -> {
            tableController.setDeck(index, cardID);
        });
    }

    /**
     * Sets a resource in storage.
     * @param clientID the ID of the affected player
     * @param resource the resource to set
     * @param amount the amount to set
     */
    @Override
    public void setResource(int clientID, Resource resource, int amount) {
        super.setResource(clientID, resource, amount);
        Platform.runLater(() -> {
            sceneControllers.get(clientID).updateResources(clientID);
        });
    }

    /**
     * Sets a resource in the strongbox
     * @param clientID the ID of the affected player
     * @param resource the resource to set
     * @param amount the amount to set
     */
    @Override
    public void setStrongbox(int clientID, Resource resource, int amount) {
        super.setStrongbox(clientID, resource, amount);
        Platform.runLater(() -> {
            sceneControllers.get(clientID).updateStrong(clientID);
        });
    }

    /**
     * Sets resources in temporary storage.
     * @param clientID ID of the client that received the resources
     * @param resources An ArrayList of Resources to put in temporary storage
     */
    @Override
    public void setTemp(int clientID, ArrayList<Resource> resources) {
        super.setTemp(clientID, resources);
        Platform.runLater(() -> {
            sceneControllers.get(clientID).updateTemp(clientID);
        });
    }

    /**
     * Sets a resource in the extra storage provided by leader cards.
     * @param clientID the ID of the affected player
     * @param resource the resource to set
     * @param amount the amount to set
     */
    @Override
    public void setExtra(int clientID, Resource resource, int amount) {
        super.setExtra(clientID, resource, amount);
        Platform.runLater(() -> {
            sceneControllers.get(clientID).updateExtra(clientID);
        });
    }

    /**
     * Adds a development card to the player's board.
     * @param clientID ID of the player
     * @param cardID ID of the card
     * @param column column to add the card to
     * @see Card
     */
    @Override
    public void addCard(int clientID, int cardID, int column) {
        super.addCard(clientID, cardID, column);
        Platform.runLater(() -> {
            sceneControllers.get(clientID).addCard(clientID, cardID, column);
        });
    }

    /**
     * Adds a leader card to the player's board.
     * @param clientID ID of the player
     * @param cardID ID of the card
     * @param column column to add the card to
     */
    @Override
    public void addLeader(int clientID, int cardID, int column) {
        super.addLeader(clientID, cardID, column);
        Platform.runLater(() -> {
            sceneControllers.get(clientID).addLeader(cardID, column);
        });
        if (clientID == getClientID()) Platform.runLater(() -> {
            hand[column] = null;
            startTurn(clientID);
        });
    }

    /**
     * Called by the table controller, it lets the player pick resources to buy the card.
     * @param index Index of the card on the table
     */
    public void buyCard(int index) {
        Platform.runLater(() -> {
            sceneControllers.get(getClientID()).buyCard(index);
        });
    }

    /**
     * Shows the winner of the game and ends the program.
     * @param clientID The ID of the winner
     * @param username The username of the winner
     */
    @Override
    public void endGame(int clientID, String username) {
        super.endGame(clientID, username);
        Platform.runLater(() -> {
            display.endGame(username);
        });
    }

    /**
     * Prints the message that couldn't be handled by the parser.
     * Should not appear during normal play.
     * @param command The command that couldn't be handled
     */
    @Override
    public void generic(Command command) {
        System.out.println(command.getClientID() + command.getMessage().name());
    }

    /**
     * Makes the player unable to take actions except switching views.
     */
    public void disableMoves() {
            tableController.disableMoves();
            sceneControllers.get(getClientID()).disableMoves();
    }

    /**
     * Sets the player board controllers.
     * @param sceneControllers Hashmap where clientID is the key and the controller is the entry
     * @see PlayerBoardController
     */
    public void setSceneControllers(HashMap<Integer, PlayerBoardController> sceneControllers) {
        this.sceneControllers = sceneControllers;
    }

    /**
     * Sets the table controller.
     * @param tableController The table controller
     * @see TableController
     */
    public void setTableController(TableController tableController) {
        this.tableController = tableController;
    }

    /**
     * Notifies this class to continue execution of commands.
     */
    public synchronized void wake() {
        notify();
    }
}
