package it.polimi.ingsw.model;

public abstract class Card{
    private int victoryPoints;
    private int ID;

    /**
     * constructor for Card.
     * @param vPoints victory points that the card is worth.
     * @param ID The ID of the card.
     */
    public Card(int vPoints, int ID){
        this.victoryPoints = vPoints;
        this.ID = ID;
    }

    /**
     *
     * @return victory Points.
     */
    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    /**
     *
     * @return the id that identifies the card.
     */
    public int getID() {
        return ID;
    }
}
