
package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.util.ArrayList;
import java.util.Stack;

public class PlayerBoard {
    private ArrayList<DevelopmentCard> col0;
    private ArrayList<DevelopmentCard> col1;
    private ArrayList<DevelopmentCard> col2;
    private ArrayList<LeaderCard> leaders;
    //Added Faith Track
    private FaithTrack faithTrack;
    private int clientID;
    private Lobby lobby;
    private int numberCards;
    private Match match;

    /**
     * Constructor for the class
     * @param clientID id of the client owner of the playerBoard
     * @param lobby the lobby in which the match is played
     * @param match the match itself
     */
    public PlayerBoard(int clientID,Lobby lobby,Match match) {
        this.col1 = new ArrayList<>();
        this.col2 = new ArrayList<>();
        this.col0 = new ArrayList<>();
        this.leaders = new ArrayList<>();
        this.faithTrack = new FaithTrack();
        this.clientID = clientID;
        this.lobby = lobby;
        numberCards = 0;
        this.match = match;
    }

    /**
     * Returns the card
     * @param x the column in that contains the card
     * @param y the position of the card in said column
     * @return card
     */
    public DevelopmentCard getCard(int x, int y){
        if(x==0){
            return col0.get(y);
        }
        else if(x==1) {
            return col1.get(y);
        }
        else {
            return col2.get(y);
        }
    }

    /**
     *
     * @param x chooses which leader
     * @return leaderCard
     */
    public LeaderCard getLeader(int x){
        return leaders.get(x);
    }

    /**
     *
     * @return faith track
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }
    //improve handling of error

    /**
     * adds a card to the playerBoard after a player buys one.
     * Checks if in the column there already is a card with a level of -1 from the card we want to add,
     * If after adding the card we have 7 cards in possession of a player we trigger last turn.
     * @param cards the bought card
     * @param x the column where the players wants to add the card
     * @return true if all the checks are successful, false otherwise
     */
    public boolean addCard(DevelopmentCard cards,int x){
        if(x==0) {
            if (!this.col0.isEmpty()) {
                if (this.col0.get(this.col0.size() - 1).getLevel() == cards.getLevel() - 1) {
                    this.col0.add(cards);
                    numberCards++;
                    match.getPlayer(clientID).addVictoryPoints(cards.getVictoryPoints());
                } else return false;
            } else {
                if (cards.getLevel() == 1) {
                    this.col0.add(cards);
                    numberCards++;
                    match.getPlayer(clientID).addVictoryPoints(cards.getVictoryPoints());
                } else return false;
            }
        }
            else if (x == 1) {
                if (!this.col1.isEmpty()) {
                    if (this.col1.get(this.col1.size() - 1).getLevel() == cards.getLevel() - 1) {
                        this.col1.add(cards);
                        numberCards++;
                        match.getPlayer(clientID).addVictoryPoints(cards.getVictoryPoints());
                    } else return false;
                } else {
                    if (cards.getLevel() == 1) {
                        this.col1.add(cards);
                        numberCards++;
                        match.getPlayer(clientID).addVictoryPoints(cards.getVictoryPoints());
                    } else return false;
                }
            }
            else if (x == 2) {
                if (!this.col2.isEmpty()) {
                    if (this.col2.get(this.col2.size() - 1).getLevel() == cards.getLevel() - 1) {
                        this.col2.add(cards);
                        numberCards++;
                        match.getPlayer(clientID).addVictoryPoints(cards.getVictoryPoints());
                    } else return false;
                } else {
                    if (cards.getLevel() == 1) {
                        this.col2.add(cards);
                        numberCards++;
                        match.getPlayer(clientID).addVictoryPoints(cards.getVictoryPoints());
                    } else return false;
                }

            }
            else{
            return false;
        }
        if(this.numberCards==7) match.setLastTurn();
        Command command = new Command(clientID, Messages.ADDCARD,cards.getID() + " " + x);
        lobby.updateClients(command);
        return true;
    }

    /**
     * adds a card to the leaders array
     * @param card the chosen leader card
     * @return
     */
    public LeaderCard addCard(LeaderCard card,int index){
        this.leaders.add(card);
        Command command = new Command(clientID, Messages.ADDLEADER, card.getID() + " " + index);
        lobby.updateClients(command);
        return card;
    }
    public ArrayList<LeaderCard> getLeaders() {
        return leaders;
    }

    /**
     *
     * @return the first column of the playerBoard
     */
    public ArrayList<DevelopmentCard> getCol0() {
        return col0;
    }

    /**
     *
     * @return the second column of the playerBoard
     */
    public ArrayList<DevelopmentCard> getCol1() {
        return col1;
    }

    /**
     *
     * @return the third column of the playerBoard
     */

    public ArrayList<DevelopmentCard> getCol2() {
        return col2;
    }

    /**
     * returns the column based on the index
     * @param x index representing the chosen column
     * @return colx
     */
    public ArrayList<DevelopmentCard> getCol(int x) {
        switch (x) {
            case 1: {
                return col1;
            }
            case 2: {
                return col2;
            }
            default:{
                return col0;
            }
        }
    }






}
