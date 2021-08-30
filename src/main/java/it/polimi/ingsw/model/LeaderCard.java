package it.polimi.ingsw.model;

public class LeaderCard extends Card {
    private LeaderRequirement req1;
    private LeaderRequirement req2;
    private Bonus bonus;

    /**
     * Constructor for LeaderCard if the card has 2 requirements
     * @param vPoints The victory point the card is worth
     * @param req1 first requirement needed to activate the card
     * @param req2 second requirement needed to activate the card
     * @param bonus bonus of the card
     * @param ID The id of the card
     */
    public LeaderCard(int vPoints,LeaderRequirement req1,LeaderRequirement req2,Bonus bonus, int ID){
        super(vPoints, ID);
        this.req1=req1;
        this.req2=req2;
        this.bonus=bonus;
    }

    /**
     * Constructor for LeaderCard if the card has only 1 requirements
     * @param vPoints The victory point the card is worth
     * @param req1  requirement needed to activate the card
     * @param bonus bonus of the card
     * @param ID The id of the card
     */
    public LeaderCard(int vPoints,LeaderRequirement req1,Bonus bonus,int ID){
        super(vPoints,ID);
        this.req1=req1;
        this.req2 = null;
        this.bonus=bonus;
    }

    /**
     *
     * @return the first requirement
     */
    public LeaderRequirement getReq1() {
        return req1;
    }

    /**
     *
     * @return the second requirement
     */
    public LeaderRequirement getReq2() {
        return req2;
    }

    /**
     *
     * @return bonus
     */
    public Bonus getBonus() {
        return bonus;
    }
}
