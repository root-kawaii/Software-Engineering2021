package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import static it.polimi.ingsw.shared.ANSI.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Command Line implementation of Client.
 * @see Client
 */
public class CLI extends Client {
    private static Scanner userInput;
    private int[] leaders;
    private int view;
    private Turn turn;

    /**
     * Class constructor for online games.
     * Connects to the specified server, sets up basic attributes.
     * @param host Address of the server
     * @param port Port of the server
     */
    public CLI(String host, int port) throws IOException {
        super(host, port);
        leaders = new int[2];
        view = 0;
        turn = new Turn(this);
    }

    /**
     * Class constructor for local games.
     */
    public CLI() throws IOException {
        super();
        leaders = new int[2];
        view = 0;
        turn = new Turn(this);
    }

    /**
     * Returns the Scanner used to get the user's input.
     * @return The user input Scanner
     */
    public Scanner getUserInput() {
        return userInput;
    }

    /**
     * Asks how many players can join the lobby.
     */
    @Override
    public void askMaxPlayers() {
        int maxPlayers;
        Command command;
        System.out.println("Lobby size?");
        maxPlayers = Integer.parseInt(userInput.nextLine()); // Using nextLine instead of parseInt as it doesn't read the newline & causes problems.
        command = new Command(getClientID(), Messages.SENDMAXPLAYERS, Integer.toString(maxPlayers));
        sendMessage(command);
    }

    /**
     * Asks the player to choose a username and grabs the input.
     */
    @Override
    public void askUsername() {
        System.out.println("Choose a username");
        setUsername(userInput.nextLine());
    }

    /**
     * Notifies the player the chosen username is not valid then asks for a new one.
     */
    @Override
    public void invalidUsername() {
        System.out.println("Invalid Username");
        askUsername();
    }

    /**
     * Asks the player to choose from the available cards.
     * @param cardIDs A String Array of the card IDs to choose from
     */
    @Override
    public void askLeader(String[] cardIDs) {
        String[] input;
        int[] IDs = new int[2];
        boolean valid = false;
        System.out.print(clearScreen);
        int i = 1;
        for (String ID : cardIDs) {
            Draw.drawCard(1, i, getCards()[Integer.parseInt(ID) - 1]);
            i += 14;
        }
        System.out.println(placeCursor(8, 1) + "Type two numbers (1-4) to choose your leader cards.");
        while (!valid) {
            System.out.print(clearLine);
            input = userInput.nextLine().split(" ");
            try {
                IDs[0] = Integer.parseInt(input[0]);
                IDs[1] = Integer.parseInt(input[1]);
                if (IDs[0] > 0 && IDs[0] < 5 && IDs[1] > 0 && IDs [1] < 5 && IDs[0] != IDs[1]) {
                    leaders[0] = Integer.parseInt(cardIDs[IDs[0]-1]) ;
                    leaders[1] = Integer.parseInt(cardIDs[IDs[1]-1]) ;
                    valid = true;
                } else {
                    System.out.println(placeCursor(8, 1) + clearLine + "Invalid choice, type two numbers (1-4)");
                }
            } catch (Exception e) {
                System.out.println(placeCursor(8, 1) + clearLine + "Invalid choice, type two numbers (1-4)");
            }
        }
        Command command = new Command(getClientID(), Messages.SENDLEADERS, cardIDs[IDs[0] - 1] + " " + cardIDs[IDs[1] - 1]);
        sendMessage(command);
    }

    /**
     * Creates Boards for every player and sets your leader Cards.
     * @param args A String Array of the players' usernames and IDs
     * @see it.polimi.ingsw.view.Board
     */
    @Override
    public void startGame(String[] args) {
        super.startGame(args);
        getBoards().get(getClientID()).setHandCard(0, getCards()[leaders[0]-1]);
        getBoards().get(getClientID()).setHandCard(1, getCards()[leaders[1]-1]);
    }

    /**
     * Notifies the user of the winner of the game.
     * @param clientID the ID of the winner
     */
    @Override
    public void endGame(int clientID,String username) {
        super.endGame(clientID,username);
        System.out.print(BGWhite + textBlack + placeCursor(11, 1) + clearLine + placeCursor(12, 1) + clearLine + placeCursor(13, 1) + clearLine);
        System.out.print(placeCursor(12, 10) + username + " won the game! Press Enter to quit.");
        userInput.nextLine();
        System.exit(0);
    }

    @Override
    public void reconnect(String username) {
        Command command;
        System.out.println("Would you like to reconnect as " + username + " (Y/N)");
        String[] choice = userInput.nextLine().split(" ");
        if (choice[0].equalsIgnoreCase("Y")) {
            command = new Command(getClientID(), Messages.OKRECONNECT, null);
            setUsername(username);
        } else {
            command = new Command(getClientID(), Messages.KORECONNECT, null);
        }
        sendMessage(command);
    }

    /**
     * Updates the state of the game after reconnecting.
     * @param clientID ID of the player being updated
     * @param args List of everything needed to synchronize the client with the server
     */
    @Override
    public void updateStatus(int clientID, String[] args) {
        super.updateStatus(clientID, args);
        switchView(clientID);
    }

    /**
     * Shows the active player's board and  if it's their turn it prompts to take an action.
     * @param clientID  the ID of the player starting their turn.
     */
    @Override
    public void startTurn(int clientID) {
        switchView(clientID);
        if (clientID == getClientID()) {
            Draw.drawInfo(14, 42);
            boolean finish = false;
            while (!finish) {
                finish = turn.takeTurn();
            }
        }
    }

    /**
     * Lets the player pick resources at the start of the game.
     * @param clientID The ID of the player picking resources
     * @param amount The amount of resources the player can pick
     */
    @Override
    public void chooseResource(int clientID, int amount) {
        ArrayList<String> choices = new ArrayList<>();
        while (choices.size() < amount) {
            try {
                System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Choose " + amount + " resource(s) to get: 1-Coin 2-Shield 3-Servant 4-Stone" + BGBlack + textWhite);
                choices.add(new Turn(this).getChoiceResource().name());
                System.out.print(placeCursor(22, 1) + clearLine);
            } catch (IllegalArgumentException e) {
                System.out.print(placeCursor(22, 1) + "Invalid move, Try again");
            }
        }
        Command command = new Command(getClientID(), Messages.CHOOSERESOURCE, String.join(" ", choices));
        sendMessage(command);
    }

    /**
     * Informs the active user of making a mistake and lets them try again.
     * @param clientID ID of the player who made the mistake
     */
    @Override
    public void invalidMove(int clientID) {
        switchView(clientID);
        if (clientID == getClientID()) {
            Draw.drawInfo(14, 42);
            System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again" + textWhite + BGBlack);
            boolean finish = false;
            while (!finish) {
                finish = turn.takeTurn();
            }
        }
    }

    /**
     * Adds resources to temporary storage and starts asking to handle them.
     * @param clientID ID of the client that received the resources
     * @param resources An ArrayList of Resources to put in temporary storage
     */
    @Override
    public void setTemp(int clientID, ArrayList<Resource> resources) {
        super.setTemp(clientID, resources);
        updateView();
    }

    /**
     * Starts handling resources.
     * @param clientID ID of the client that should handle the resources
     */
    @Override
    public void resourceGain(int clientID) {
        switchView(clientID);
        if (clientID == getClientID()) {
            System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine);
            System.out.print(BGBlack + textWhite + "I" + BGWhite + textBlack + "nsert resource <index> <S>torage/<E>xtra " + BGBlack + textWhite + "R" + BGWhite + textBlack + "emove resource <1-5>" + BGBlack + textWhite + "E" + BGWhite + textBlack + "nd turn" + BGBlack + textWhite);
            turn.handleResource();
        }
    }

    /**
     * Confirms a Resource has been added to storage and proceeds with handling.
     * @param clientID ID of the client adding the resources.
     */
    @Override
    public void okAddResource(int clientID) {
        updateView();
        if (getClientID() == clientID) {
            System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Done, enter next action." + BGBlack + textWhite);
            turn.handleResource();
        }
    }

    /**
     * Notifies the player a resource couldn't be added to storage and proceeds with handling.
     * @param clientID ID of the client handling resources
     */
    @Override
    public void koAddResource(int clientID) {
        updateView();
        if (getClientID() == clientID) {
            System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + BGBlack + textWhite);
            turn.handleResource();
        }
    }

    /**
     * Sets the specified client's faith points and updates the screen.
     * @param clientID ID of the client receiving points
     * @param points the total amount of points
     */
    @Override
    public void setFaithPoints(int clientID, int points) {
        super.setFaithPoints(clientID, points);
        updateView();
    }

    /**
     * Adds a card to a player's board and updates the screen.
     * @param clientID ID of the player
     * @param cardID ID of the card
     * @param column column to add the card to
     */
    @Override
    public void addCard(int clientID, int cardID, int column) {
        super.addCard(clientID, cardID, column);
        updateView();
    }

    /**
     * Adds a leader to a player's board and updates the screen.
     * @param clientID ID of the player
     * @param cardID ID of the card
     * @param column column to add the card to
     */
    @Override
    public void addLeader(int clientID, int cardID, int column) {
        super.addLeader(clientID, cardID, column);
        updateView();
        if (clientID == getClientID()) startTurn(clientID);
    }

    /**
     * Puts a resource in the player's storage, removes it if empty and updates the screen.
     * @param clientID the ID of the affected player
     * @param resource the resource to set
     * @param amount the amount to set
     */
    @Override
    public void setResource(int clientID, Resource resource, int amount) {
        super.setResource(clientID, resource, amount);
        updateView();
    }

    /**
     * Puts a certain resource in a player's strongbox and updates the screen.
     * @param clientID the ID of the affected player
     * @param resource the resource to set
     * @param amount the amount to set
     */
    @Override
    public void setStrongbox(int clientID, Resource resource, int amount) {
        super.setStrongbox(clientID, resource, amount);
        updateView();
    }

    /**
     * Puts a certain resource in a player's extra storage and updates the screen.
     * @param clientID the ID of the affected player
     * @param resource the resource to set
     * @param amount the amount to set
     */
    @Override
    public void setExtra(int clientID, Resource resource, int amount) {
        super.setExtra(clientID, resource, amount);
        updateView();
    }

    /**
     * Sets a card available for purchase and updates the screen.
     * @param index Index of the deck
     * @param cardID ID of the card, -1 means no card
     */
    @Override
    public void setMarketCard(int index, int cardID) {
        super.setMarketCard(index, cardID);
        updateView();
    }

    /**
     * Prints a command that couldn't be handled to screen.
     * Shouldn't appear during normal play.
     * @param command The command that couldn't be handled
     */
    @Override
    public void generic(Command command) {
        System.out.println(command.getClientID() + " " + command.getMessage() /*+ " " + String.join(" ", command.getArgs())*/);
        String[] response = userInput.nextLine().split(" ");
        Command answer = new Command(getClientID(), Messages.valueOf(response[0]), String.join(" ", Arrays.copyOfRange(response, 1, response.length)));
        sendMessage(answer);
    }

    /**
     * shows the specified screen.
     * 0 shows the market, 1-4 shows that player's board.
     * @param ID The ID of the screen to view
     */
    public void switchView(int ID) {
        view = ID;
        if (view == 0) {
            Draw.drawTable(getTable());
        } else {
            Draw.drawBoard(getBoards().get(view));
        }
    }

    /**
     * Updates the current screen.
     */
    public void updateView() {
        if (view == 0) {
            Draw.drawTable(getTable());
        } else {
            Draw.drawBoard(getBoards().get(view));
        }
    }

    /**
     * Main method that starts the CLI.
     * Asks for the server's address and port and proceeds to set up the client.
     * @param args String arguments
     */
    public static void main(String[] args) {
        String host;
        int port;
        try {
            userInput = new Scanner(System.in);
            if (args == null) {
                System.out.println("Type the Server's IP:");
                host = userInput.nextLine();
                System.out.println("Type the Server's port:");
                port = Integer.parseInt(userInput.nextLine()); // Using nextLine instead of parseInt as it doesn't read the newline & causes problems.
                Thread cli = new Thread(new CLI(host, port));
                cli.start();
            } else {
                Thread cli = new Thread(new CLI());
                cli.start();
            }
        } catch (IOException e) {
            System.out.println("Couldn't connect to server.");
        }
    }
}
