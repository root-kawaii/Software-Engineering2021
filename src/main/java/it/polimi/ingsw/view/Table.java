package it.polimi.ingsw.view;

import it.polimi.ingsw.model.MarbleColour;

/**
 * Class used by the CLI to draw the table.
 * Simply contains values and getter/setter methods needed to draw the table.
 */
public class Table {
    private Card[] cards;
    private Card[] decks;
    private MarbleColour[] market;
    private MarbleColour extraMarble;

    /**
     * Class constructor.
     * @param cards An array of the twelve cards initially available for purchase
     */
    public Table(Card[] cards) {
        this.cards = cards;
        this.decks = new Card[12];
        this.market = new MarbleColour[12];
    }

    /**
     * Gets all the cards available for purchase.
     * @return An array of Cards, null if a specific slot ran out of cards
     */
    public Card[] getDecks() {
        return decks;
    }

    /**
     * Gets all the marbles currently in the market.
     * @return an array of marble colours
     * @see  MarbleColour
     */
    public MarbleColour[] getMarket() {
        return market;
    }

    /**
     * Gets the marble currently outside the market.
     * @return  A marble colour
     * @see MarbleColour
     */
    public MarbleColour getExtraMarble() {
        return extraMarble;
    }

    /**
     * Sets a card in the specified slot
     * @param index The slot of the card
     * @param card The card, null if the slot is empty
     */
    public void setDeck(int index, Card card) {
        this.decks[index] = card;
    }

    /**
     * Sets all the marbles in the market and the extra marble.
     * @param marbles An array of thirteen marble colours, the thirteenth marble is the extra one
     * @see MarbleColour
     */
    public void setMarket(String[] marbles) {
        for (int i = 0; i < 12; i++) {
            market[i] = MarbleColour.valueOf(marbles[i]);
        }
        extraMarble = MarbleColour.valueOf(marbles[12]);
    }

    /**
     * Sets all the cards available for purchase.
     * @param cardIds An Array of twelve cards, null if a slot is empty
     * @see Card
     */
    public void setDecks(String[] cardIds) {
        for (int i = 0; i < 12; i++) {
            decks[i] = cards[Integer.parseInt(cardIds[i]) - 1];
        }
    }
}
