package it.polimi.ingsw.model;

import java.util.EnumMap;

public class DevelopmentCard extends Card implements Cloneable {
    private int level;
    private CardColour colour;
    private EnumMap<Resource, Integer> cost;
    private Production production;

    /**
     * Constructor for the class
     *
     * @param lvl        the level of the card(1,2,3)
     * @param colour     the colour of the card(green,yellow,blue,yellow,purple)
     * @param vPoints    the victory points a card is worth
     * @param cost       the resources needed to buy the card
     * @param production requirements and products of the card production
     * @param ID         the card ID
     */
    public DevelopmentCard(int lvl, CardColour colour, int vPoints, EnumMap<Resource, Integer> cost, Production production, int ID) {
        super(vPoints, ID);
        this.level = lvl;
        this.colour = colour;
        this.cost = cost;
        this.production = production;
    }

    /**
     * returns the card level
     *
     * @return level
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * returns the card cost
     *
     * @return cost
     */
    public EnumMap<Resource, Integer> getCost() {
        return cost;
    }

    /**
     * returns the production
     *
     * @return production
     */
    public Production getProduction() {
        return production;
    }

    /**
     * returns the card colour
     *
     * @return colour
     */
    public CardColour getColour() {
        return this.colour;
    }

    /**
     * makes a copy of the development card, used in MarketController.
     * @see it.polimi.ingsw.controller.MarketController
     * @return
     */
    @Override
    public Object clone() {
        DevelopmentCard copy = null;
        try {
            copy = (DevelopmentCard) super.clone();
        } catch (CloneNotSupportedException e) {
            copy = new DevelopmentCard(this.level, this.colour, this.getVictoryPoints(), this.cost, this.production, this.getID());
        }
        copy.cost =(EnumMap<Resource,Integer>)this.cost.clone();
        return copy;
    }

}

