package it.polimi.ingsw.view;


import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class used by the CLI to represent a player and draw their board.
 * Simply stores values needed to draw and provides getter/setter methods.
 * @see it.polimi.ingsw.client.CLI.CLI
 */
public class Board {
    private String username;
    private int faith;
    private int CPUFaith;
    private int victoryPoints;
    private Card[] leaderCards;
    private Card[] hand;
    private ArrayList<Card>[] developmentCards;
    private HashMap<Resource, Integer> shelves;
    private HashMap<Resource, Integer> strongBox;
    private HashMap<Resource, Integer> extra;
    private ArrayList<Resource> temp;

    /**
     * Class constructor.
     * @param username Username of the player
     */
    public Board(String username) {
        this.username = username;
        this.faith = 0;
        this.CPUFaith = 0;
        this.victoryPoints = 0;
        this.leaderCards = new Card[2];
        this.hand = new Card[2];
        this.developmentCards = new ArrayList[] {new ArrayList(), new ArrayList(), new ArrayList()};
        this.shelves = new HashMap<Resource, Integer>();
        this.strongBox = new HashMap<Resource, Integer>();
        this.extra = new HashMap<Resource, Integer>();
        this.temp = new ArrayList<Resource>();
    }

    /**
     * Gets the player's faith points.
     * @return The player's faith points.
     */
    public int getFaith() {
        return faith;
    }

    /**
     * Gets the computer player's faith points.
     * Needed for single player games.
     * @return The computer player's faith points.
     */
    public int getCPUFaith() {
        return CPUFaith;
    }

    /**
     * Gets the player's victory points.
     * @return The player's victory points.
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Gets the leader card in the specified slot.
     * @param index The slot of the leader card
     * @return the leader card, null if there's none
     * @see Card
     */
    public Card getLeaderCard(int index) {
        return leaderCards[index];
    }

    /**
     * Gets a card in the player's hand.
     * Cards stored here are leader cards that haven't been played yet, only the client's unplayed cards are visible.
     * @param index The slot of the leader card
     * @return the leader card
     * @see Card
     */
    public Card getHandCard(int index) {
        return hand[index];
    }

    /**
     * Gets a specific development card from a certain slot.
     * @param col The column of the card
     * @param row The row of the card
     * @return The card
     * @see Card
     */
    public Card getDevelopmentCard(int col, int row) {
        return developmentCards[col].get(row);
    }

    /**
     * Gets the topmost development card from a certain column.
     * @param col The column
     * @return The development card
     * @see Card
     */
    public Card getDevelopmentCard(int col) {
        return developmentCards[col].get(developmentCards[col].size() - 1);
    }

    /**
     * Gets the player's username.
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets all the development cards on the board.
     * @return An array of lists containing the cards in each column
     * @see Card
     */
    public ArrayList<Card>[] getDevelopmentCards() {
        return developmentCards;
    }

    public ArrayList<Card> getDevelopmentColumn(int index) {
        switch (index) {
            case 0:
                return developmentCards[0];
            case 1:
                return developmentCards[1];
            case 2:
                return developmentCards[2];
            default:
                return null;
        }
    }

    /**
     * Gets a map of the resources in the shelves.
     * @return A map with resources as key and integers as values
     * @see Resource
     */
    public HashMap<Resource, Integer> getShelves() {
        return shelves;
    }

    /**
     * Gets a map of the resources in the strongbox.
     * @return A map with resources as key and integers as values
     * @see Resource
     */
    public HashMap<Resource, Integer> getStrongBox() {
        return strongBox;
    }

    /**
     * Gets a map of the resources in the extra storage granted by leader cards.
     * @return A map with resources as key and integers as values
     * @see Resource
     */
    public HashMap<Resource, Integer> getExtra() {
        return extra;
    }

    /**
     * Gets a List of resources in temporary storage.
     * @return A list of resources
     */
    public ArrayList<Resource> getTemp() {
        return temp;
    }

    /**
     * Sets the faith points for the player
     * @param faith The total amount of faith points
     */
    public void setFaith(int faith) {
        this.faith = faith;
    }

    /**
     * Sets the computer player's faith points.
     * Needed only for single player games.
     * @param CPUFaith The total amount of faith points
     */
    public void setCPUFaith(int CPUFaith) {
        this.CPUFaith = CPUFaith;
    }

    /**
     * Sets the player's victory points.
     * @param victoryPoints The total amount of victory points
     */
    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    /**
     * Sets a leader card in a slot.
     * Used for cards that have been played
     * @param index The slot of the card
     * @param card The card
     * @see Card
     */
    public void setLeaderCards(int index, Card card) {
        this.leaderCards[index] = card;
    }

    /**
     * Sets a leader card in the specified slot in the player's hand.
     * Used to set cards while they're not played.
     * @param index The slot
     * @param card The card
     * @see Card
     */
    public void setHandCard(int index, Card card) {
        this.hand[index] = card;
    }

    /**
     * Adds a card at the top of a column.
     * @param col The column
     * @param card The card
     * @see Card
     */
    public void setDevelopmentCards(int col, Card card) {
        this.developmentCards[col].add(card);
    }

    /**
     * Sets temporary storage.
     * @param temp An Arraylist of resources contained in temporary storage
     * @see Resource
     */
    public void setTemp(ArrayList<Resource> temp) {
        this.temp = temp;
    }
}
