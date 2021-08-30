package it.polimi.ingsw.view;

import it.polimi.ingsw.model.CardColour;

import java.util.EnumMap;

// Only holds info to draw the card; top, left, middle and right are places to draw stuff.

/**
 * Class used by the CLI to represent a card.
 */
public class Card {
    private int ID;
    private int victoryPoints;
    private int level;
    private CardColour colour;
    private EnumMap<CardResources, Integer> top;
    private EnumMap<CardResources, Integer> left;
    private CardResources middle;
    private EnumMap<CardResources, Integer> right;

    /**
     * Class constructor.
     * @param ID The ID of the card
     * @param victoryPoints The amount of victory points this card is worth
     * @param level The level of this card, null if not applicable
     * @param colour The colour of this card, null if not applicable
     * @param top A map of card resources to be drawn at the top of the card, represents the requirements to buy or activate the card
     * @param left A map of card resources to be drawn on the left of the card
     * @param middle A map of card resources to be drawn in the middle of the card
     * @param right A map of card resources to be drawn on the right of the card
     */
    public Card(int ID, int victoryPoints, int level, CardColour colour, EnumMap<CardResources, Integer> top, EnumMap<CardResources, Integer> left, CardResources middle, EnumMap<CardResources, Integer> right) {
        this.ID = ID;
        this.victoryPoints = victoryPoints;
        this.level = level;
        this.colour = colour;
        this.top = top;
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    /**
     * Gets the card's ID.
     * @return The card's ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Gets the card's victory points.
     * @return The card's victory points
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Gets the card level.
     * @return The card's level, null if not applicable
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the colour of the card.
     * @return The card's colour, null if not applicable
     */
    public CardColour getColour() {
        return colour;
    }

    /**
     * Gets the card's top map.
     * @return A map of card resources
     * @see CardResources
     */
    public EnumMap<CardResources, Integer> getTop() {
        return top;
    }

    /**
     * Gets the card's left map.
     * @return A map of card resources
     * @see CardResources
     */
    public EnumMap<CardResources, Integer> getLeft() {
        return left;
    }

    /**
     * Gets the card's middle map.
     * @return A map of card resources
     * @see CardResources
     */
    public CardResources getMiddle() {
        return middle;
    }

    /**
     * Gets the card's right map.
     * @return A map of card resources
     * @see CardResources
     */
    public EnumMap<CardResources, Integer> getRight() {
        return right;
    }
}
