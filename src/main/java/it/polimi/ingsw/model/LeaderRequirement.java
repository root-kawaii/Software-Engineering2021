package it.polimi.ingsw.model;

import java.util.EnumMap;

public class LeaderRequirement {
    private int level = 0;
    private CardColour colour = null;
    private int amount = 0;
    private  EnumMap<Resource,Integer> resource = null;

    /**
     * Constructor for leader requirements
     * @param level the level of the card needed to activate the leader
     * @param colour the colour of the develpoment card needed to activate the leader
     * @param resource the resources needed to activate the leader
     */
    public LeaderRequirement(int level,CardColour colour, EnumMap<Resource,Integer> resource){
        this.level=level;
        this.colour=colour;
        this.resource=resource;
    }

    /**
     * Constructor if the requirement is only card colour and and amount of said cards
     * @param colour Development card colour needed
     * @param amount Number of development card with said colour needed
     */
    public LeaderRequirement(CardColour colour,int amount){
        this.colour=colour;
        this.amount = amount;
    }

    /**
     * Constructor if the requirement is a card colour with an amount and a level
     * @param level The level of the card needed
     * @param colour Development card colour needed
     * @param amount Number of development card with said colour needed
     */
    public LeaderRequirement(int level,CardColour colour,int amount){
        this.level=level;
        this.colour=colour;
        this.amount = amount;
    }

    /**
     * Constructor if the requirement is a certain amount of resources
     * @param resource Map containing the needed resources
     */
    public LeaderRequirement(EnumMap<Resource,Integer> resource){
        this.resource=resource;
    }

    /**
     *
     * @return the level
     */
    public int getLevel(){
        return level;
    }

    /**
     *
     * @return the colour
     */
    public CardColour getColour(){
        return colour;
    }

    /**
     *
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     *
     * @return the resource map
     */
    public EnumMap<Resource,Integer> getResource(){return resource;}
}
