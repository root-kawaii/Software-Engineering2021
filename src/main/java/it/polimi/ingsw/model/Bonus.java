package it.polimi.ingsw.model;

public class Bonus {
    private  MarbleColour marble;
    private  String storage;
    private  Resource discount;
    private  Production production;
    private BonusType type;

    /**
     * constructor for a card with bonus type marble
     * @param marble the marble that the player is getting instead of the white ones
     */
    public Bonus(MarbleColour marble) {
        this.marble = marble;
        this.storage = null;
        this.discount = null;
        this.production = null;
        this.type = BonusType.MARBLE;
    }

    /**
     * constructor for a card with bonus type extra storage
     * @param storage String representing the resource of extra storage
     */
    public Bonus(String storage){
        this.storage = storage;
        this.discount = null;
        this.production = null;
        this.marble = null;
        this.type = BonusType.STORAGE;
    }

    /**
     * constructor for a card with bonus discount
     * @param discount the resource the card is giving a discount of
     */
    public Bonus(Resource discount){
        this.discount = discount;
        this.storage = null;
        this.production = null;
        this.marble = null;
        this.type = BonusType.DISCOUNT;
    }

    /**
     * constructor for a card with bonus production
     * @param production the production the card offers
     */
    public Bonus(Production production){
        this.production = production;
        this.storage = null;
        this.discount = null;
        this.marble = null;
        this.type = BonusType.PRODUCTION;
    }

    /**
     *
     * @return marble
     */
    public MarbleColour getMarble(){ return marble; }

    /**
     *
     * @return resource to be discounted
     */
    public Resource getDiscount(){
        return discount;
    }

    /**
     *
     * @return string of the resource that is going to have extra storage
     */
    public String getStorage(){
        return storage;
    }

    /**
     *
     * @return production
     */
    public Production getProduction(){
        return production;
    }

    /**
     *
     * @return the bonus type of the leaderCard
     */
    public BonusType getType() {
        return type;
    }
}
