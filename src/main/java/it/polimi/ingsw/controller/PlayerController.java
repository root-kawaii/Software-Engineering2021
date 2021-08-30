package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;
import it.polimi.ingsw.shared.LeaderCardsGetter;

import java.util.*;

/**
 * Controller for Player actions.
 */
public class PlayerController {
    Match match;
    Lobby lobby;
    LeaderCard[] leaderCards;
    MainController mainController;
    boolean check=false;

    /**
     * Constructor of Player controller, gets Leader cards.
     * @param match the match we are playing
     * @param lobby the lobby of the match
     */
    public PlayerController(Match match, Lobby lobby, MainController mainController) {
        this.match = match;
        this.lobby = lobby;
        this.leaderCards = new LeaderCardsGetter().getCards();
        this.mainController = mainController;
    }

    /**
     * Send client Leader choice request.
     */
    public void askLeaders() {
        ArrayList<Integer> IDs = new ArrayList<Integer>();
        for (int i = 49; i < 65; i++) {
            IDs.add(i);
        }
        Collections.shuffle(IDs);
        for (Player player : match.getPlayers()) {
            Command command = new Command(player.getClientID(), Messages.ASKLEADERS, IDs.remove(0) + " " + IDs.remove(0) + " " + IDs.remove(0) + " " + IDs.remove(0));
            lobby.updateClients(command);
        }
    }

    /**
     * Choose leader cards at the start of the game.
     * @param clientID Player
     * @param leader1ID ID of the first chosen cards
     * @param leader2ID ID of the second chosen card
     */
    public void chooseLeaders(int clientID, int leader1ID, int leader2ID) {
        Player player = match.getPlayer(clientID);
        for (LeaderCard leaderCard : leaderCards) {
            if (leaderCard.getID() == leader1ID) {
                player.addLeader(leaderCard);
            }
        }
            for (LeaderCard leaderCard2 : leaderCards) {
                if (leaderCard2.getID() == leader2ID) {
                    player.addLeader(leaderCard2);
                }
        }
    }

    public void discardLeader(int i){
        match.getActivePlayer().removeLeader(i);
    }

    /**
     * Check if leader can be activated.
     * @param leaderCard the card we want to activate
     * @return True or false
     */
    public boolean checkActivateLeader(LeaderCard leaderCard){
        int i,j;
        boolean break_flag=false;
        boolean check=false;
        //checks for the requirement level, done by checking the size of the developmentCard columns.
        if(leaderCard.getReq1().getLevel()>0){
            if(leaderCard.getReq1().getLevel()<=match.getActivePlayer().getPlayerBoard().getCol0().size()){
                check=true;
            }
            if(leaderCard.getReq1().getLevel()<=match.getActivePlayer().getPlayerBoard().getCol1().size()){
                check=true;
            }
            if(leaderCard.getReq1().getLevel()<=match.getActivePlayer().getPlayerBoard().getCol2().size()){
                check=true;
            }
            if(!check) return false;
        }
        //check for requirement colour
        //done by iterating over the developmentCards column and getting the colour of every card
        if(leaderCard.getReq1().getColour()!=null && leaderCard.getReq1().getColour()!= CardColour.BLANK) {
            for (i = 0; i < 3; i++) {
                if(break_flag) break;
                    for (j = 0; j < 3; j++) {
                        if (!match.getActivePlayer().getPlayerBoard().getCol(i).isEmpty()) {
                            if (match.getActivePlayer().getPlayerBoard().getCol(i).size() <= j) break;
                            if (leaderCard.getReq1().getColour() == match.getActivePlayer().getPlayerBoard().getCard(i, j).getColour()) {
                                break_flag = true;
                                break;
                            }
                        }
                }
            }
            if(!break_flag)return false;
        }
            //check if the requirement is a number of resources
            if(leaderCard.getReq1().getResource()!=null && leaderCard.getReq1().getResource().size()!=0){
                break_flag=false;
                for(Map.Entry<Resource,Integer> req : leaderCard.getReq1().getResource().entrySet()){
                        if(match.getActivePlayer().getStorage().getResource(req.getKey())>=req.getValue()){
                            break_flag = true;
                            break;
                        }
                        if(match.getActivePlayer().getStorage().getStrong(req.getKey())>=req.getValue()){
                            break_flag = true;
                            break;
                        }
                }
                if(!break_flag)return false;
            }
        //check if a card has 2 types of requirement
        //everything inside is the same as all the checks for req1
        if (leaderCard.getReq2() != null) {
            if (leaderCard.getReq2().getLevel() > 0) {
                if (leaderCard.getReq2().getLevel() <= match.getActivePlayer().getPlayerBoard().getCol0().size()) {
                    check = true;
                }
                if (leaderCard.getReq2().getLevel() <= match.getPlayers().get(match.getPlayerTurn()).getPlayerBoard().getCol1().size()) {
                    check = true;
                }
                if (leaderCard.getReq2().getLevel() <= match.getPlayers().get(match.getPlayerTurn()).getPlayerBoard().getCol2().size()) {
                    check = true;
                }
                if (!check) return false;
            }
            if (leaderCard.getReq2().getColour()!=null && leaderCard.getReq2().getColour() != CardColour.BLANK) {
                for (i = 0; i < 3; i++) {
                    if (break_flag) break;
                    for (j = 0; j < 3; j++) {
                        if (!match.getActivePlayer().getPlayerBoard().getCol(i).isEmpty()) {
                            if (match.getActivePlayer().getPlayerBoard().getCol(i).size() <= j) break;
                            if (leaderCard.getReq2().getColour() == match.getActivePlayer().getPlayerBoard().getCard(i, j).getColour()) {
                                break_flag = true;
                                break;
                            }
                        }
                    }
                }
                if (!break_flag) return false;
            }
            if (leaderCard.getReq2().getResource()!=null && leaderCard.getReq2().getResource().size() != 0) {
                break_flag = false;
                for (Map.Entry<Resource, Integer> req : leaderCard.getReq2().getResource().entrySet()) {
                    if (match.getActivePlayer().getStorage().getResource(req.getKey()).equals(req.getValue())) {
                        break_flag = true;
                        break;
                    }
                    if (match.getActivePlayer().getStorage().getStrong(req.getKey()) == req.getValue()) {
                        break_flag = true;
                        break;
                    }
                }
                if (!break_flag) return false;
            }
        }
        return true;
    }

    /**
     * Insert resource for temporary resources handling.
     * @param tempIndex Index in temporary array of resources
     * @param extra Whether we have Extra Storage or not
     */
    public void insertResource(int tempIndex, boolean extra) {
        try {
            if (!extra) { // extra tells if the resource should go in regular or extra storage
                match.getActivePlayer().getStorage().addResource(match.getActivePlayer().getStorage().getTemp().get(tempIndex), 1);
            } else {
                match.getActivePlayer().getStorage().addInExtra(match.getActivePlayer().getStorage().getTemp().get(tempIndex), 1);
            }
            match.getActivePlayer().getStorage().removeTemp(tempIndex);
            Command command = new Command(match.getPlayerTurn(), Messages.OKRESOURCEHANDLE, null);
            lobby.updateClients(command);
        } catch (Exception e) {
            Command command = new Command(match.getPlayerTurn(), Messages.KORESOURCEHANDLE, null);
            lobby.updateClients(command);
        }
    }

    /**
     * Remove resource for temporary resources handling.
     * @param resource the resource we want to remove
     * @param extra Whether we have Extra Storage or not
     */
    public void removeResource(Resource resource, boolean extra) {
        try {
            if (!extra) {
                match.getActivePlayer().getStorage().removeStorage(resource, 1);

            } else {
                match.getActivePlayer().getStorage().removeExtra(resource, 1);

            }
            match.getActivePlayer().getStorage().addTemp(resource);
            Command command = new Command(match.getPlayerTurn(), Messages.OKRESOURCEHANDLE, null);
            lobby.updateClients(command);
        } catch (Exception e) {
            Command command = new Command(match.getPlayerTurn(), Messages.KORESOURCEHANDLE, null);
            lobby.updateClients(command);
        }
    }


    /**
     * Add victory points to the player.
     * @param player the player we add points to
     * @param points number of points we give to the player
     */
    public void addVictoryPoints(Player player,int points){
        player.addVictoryPoints(points);
    }
    /**
     *Adds leader bonus and victory points
     * @param card the leader card we just played
     */
    public void useLeaderCard(LeaderCard card,int index){
        if(this.checkActivateLeader(card)){
            match.getActivePlayer().getBonus().add(card.getBonus());
            if(card.getBonus().getType().equals(BonusType.STORAGE)){
                match.getActivePlayer().getStorage().createExtra(Resource.valueOf(card.getBonus().getStorage()));
            }
            match.getActivePlayer().getHand().remove(index);
            if(check)index++;
            match.getActivePlayer().getPlayerBoard().addCard(card,index);
            match.getActivePlayer().addVictoryPoints(card.getVictoryPoints());
            }
        else{
            Command command = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
            lobby.updateClients(command);
        }
        }

    /**
     * Getter of Leader Card
     * @param x the index which represents the card we want to add
     * @return the leader card by id of the hand
     */
    public int getLeaderCard(int x) {
        int handSize = match.getActivePlayer().getHand().size();
        if(x==0 && handSize>1 && match.getActivePlayer().getCounterD()==0){
            match.getActivePlayer().addCounterD(1);
            return 0;
        }
        else if(x==1 && handSize>1){
            return 1;
        }
        else if(x==1 && handSize==1 && match.getActivePlayer().getCounterD()==1){
            check=true;
            return 0;
        }
        else if(x==0 && handSize==1 && match.getActivePlayer().getCounterD()==1){
            Command command = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
            lobby.updateClients(command);
            return -1;
        }
        else if(x==0 && handSize==1 && match.getActivePlayer().getCounterD()==0){
            return 0;
        }
        else{
            Command command = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
            lobby.updateClients(command);
            return -1;
        }
    }

    public LeaderCard[] getLeaderCards() {
        return leaderCards;
    }

    /**
     * Handles a player disconnecting.
     * Sets their status to disconnected and ends their turn if needed.
     * @param clientID ID of the player disconnecting
     */
    public void disconnected(int clientID) {
        match.getPlayer(clientID).setConnected(false);
        if (match.getActivePlayer().getClientID() == clientID) {
            Command command = new Command(clientID, Messages.ENDTURN, null);
            lobby.addCommand(command);
        }
    }
}
