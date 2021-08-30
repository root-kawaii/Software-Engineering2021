package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.util.ArrayList;
import java.util.EnumMap;

public class Player {
    private String username;
    private int clientID;
    boolean connected;
    private int faithPoints;
    private int victoryPoints;
    private Table table; // Reference to Table for convenience.
    private PlayerBoard playerBoard; // even ComputerPlayer has one for convenience in accessing faith points.
    private Storage storage;
    private Lobby lobby;
    private Match match;
    private ArrayList<Bonus> bonus;
    private ArrayList<LeaderCard> hand;
    private int counterD=0;

    /**
     * Constructor for the class, sets all the attributes and creates the storage, the playerBoard and the list
     * to keep track of the active bonuses
     * @param username
     * @param clientID
     * @param table
     * @param lobby
     */
    public Player(String username, int clientID, Table table, Lobby lobby) {
        this.username = username;
        this.clientID = clientID;
        this.connected = true;
        this.faithPoints = 0;
        this.victoryPoints = 0;
        this.bonus = new ArrayList<>();
        this.table = table;
        this.lobby = lobby;
        this.storage = new Storage(lobby, clientID);
        this.match = table.getMatch();
        this.playerBoard = new PlayerBoard(this.clientID,this.lobby,this.match);
        this.hand = new ArrayList<LeaderCard>();
    }

    /**
     * adds a leader to the hand of a player
     * @param leaderCard the leader card to be added
     */
    public void addLeader(LeaderCard leaderCard) {
        this.hand.add(leaderCard);
    }

    /**
     *
     * @param i each player has 2 leaders the index allows to choose the one to activate
     * @return leaderCard
     */
    public LeaderCard getLeader(int i) {
        return hand.get(i);
    }

    /**
     * Discards the leaderCard from the player Hand and gives the player 1 faith point
     * @param i
     */
    public void removeLeader(int i){
        if(i==1 && hand.size()>0 && counterD==0){
            hand.remove(0);
            match.getActivePlayer().addFaithPoints(1);
            counterD++;
        }
        else if(i==2 && hand.size()>1){
            hand.remove(1);
            match.getActivePlayer().addFaithPoints(1);
        }
        else if(i==2 && hand.size()==1 && counterD==1){
            hand.remove(0);
            match.getActivePlayer().addFaithPoints(1);
        }
        else if(i==1 && hand.size()==1 && match.getActivePlayer().getCounterD()==1){
        }
        else{
        }
    }


    public String getUsername() {
        return this.username;
    }

    public int getClientID() {
        return this.clientID;
    }

    public int getFaithPoints() {
        return this.faithPoints;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Adds victory points to the player
     * @param value quantity of victory points to be added
     */
    public void addVictoryPoints(int value) {
        this.victoryPoints += value;
        Command command = new Command(clientID, Messages.SETVICTORYPOINTS, String.valueOf(victoryPoints));
        lobby.updateClients(command);
    }

    public Table getTable() {
        return table;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public Storage getStorage() {
        return storage;
    }

    /**
     * adds faith points to the player
     * @param addedPoints quantity of points to be added
     * @return new total faithPoints
     */
    public int addFaithPoints(int addedPoints) {
        handleFaithPoints(addedPoints);
        Command command = new Command(clientID, Messages.SETFAITHPOINTS, String.valueOf(faithPoints));
        lobby.updateClients(command);
        return this.faithPoints;
    }

    /**
     * Adds the faithPoints one by one, checks everytime if the player has reached a PopeTile in case triggers VaticanReport
     * @param addedPoints all the faithPoints to be added
     */
    public void handleFaithPoints(int addedPoints){
        int i;
        boolean popeCheck=false;
        int x=0;
        for(i=1;i<=addedPoints;i++){
            this.faithPoints++;
            if(this.faithPoints==8||this.faithPoints==16||this.faithPoints==20){
                if (this.faithPoints==8 && !match.ismPopeTile1()){
                    popeCheck = true;
                    x = this.faithPoints;
                }
                else if (this.faithPoints==16 && !match.ismPopeTile2()){
                    popeCheck = true;
                    x = this.faithPoints;
                }
                else if(this.faithPoints==20 && !match.ismPopeTile3()) {
                    popeCheck = true;
                    x = this.faithPoints;
                }
            }
            //if player get's one one of the tiles shown below the player gets a certain quantity of victory points
            if(this.faithPoints==3||this.faithPoints==6||this.faithPoints==9||this.faithPoints==12||this.faithPoints==15||this.faithPoints==18||this.faithPoints==21||this.faithPoints==24){
                switch(this.faithPoints){
                    case 3:
                        this.addVictoryPoints(1);
                        break;
                    case 6:
                        this.addVictoryPoints(2);
                        break;
                    case 9:
                        this.addVictoryPoints(4);
                        break;
                    case 12:
                        this.addVictoryPoints(6);
                        break;
                    case 15:
                        this.addVictoryPoints(9);
                        break;
                    case 18:
                        this.addVictoryPoints(12);
                        break;
                    case 21:
                        this.addVictoryPoints(16);
                        break;
                    case 24:
                        this.addVictoryPoints(20);
                        break;
                }
            }
            if(popeCheck) {
                VaticanReport(x);
                popeCheck=false;
            }
        }
    }

    /**
     *This method adds victory points if a player triggers one of the three pope tiles, or if the players is inside
     * the respective vatican zone when the trigger happens.
     * @param x the number of faithPoints of the player that triggers VaticanReport
     */
    public void VaticanReport(int x){
        int i=0;
            switch (x){
                case 8:
                    match.setmPopeTile1(true);
                    for(i=0;i<match.getPlayers().size();i++) {
                        if ((match.getPlayers().get(i).getFaithPoints() >= 5)) {
                            match.getPlayers().get(i).addVictoryPoints(2);
                        }
                    }
                    break;
                case 16:
                    match.setmPopeTile2(true);
                    for(i=0;i<match.getPlayers().size();i++) {
                        if ((match.getPlayers().get(i).getFaithPoints() >= 12)){
                            match.getPlayers().get(i).addVictoryPoints(3);
                        }
                    }
                    break;
                case 20:
                    match.setmPopeTile3(true);
                    for(i=0;i<match.getPlayers().size();i++) {
                        if ((match.getPlayers().get(i).getFaithPoints() >= 19)) {
                            match.getPlayers().get(i).addVictoryPoints(4);
                        }
                    }
                    break;
            }

        }

    /**
     *
     * @return the player hand
     */
    public ArrayList<LeaderCard> getHand() {
        return hand;
    }

    /**
     *
     * @return the lobby
     */
    public Lobby getLobby() {
        return lobby;
    }

    /**
     *
     * @return the match
     */
    public Match getMatch() {
        return match;
    }

    /**
     *
     * @return returns the arrayList containing the players active bonuses
     */
    public ArrayList<Bonus> getBonus() {
        return bonus;
    }

    /**
     * This methods increments counterD used for discard and play leader
     * @param counterD
     */
    public void addCounterD(int counterD) {
        this.counterD += counterD;
    }
    /**
     * Returns the connection status of this player.
     * @return The status
     */
    public boolean getConnected() {
        return connected;
    }

    /**
     *
     * @return counterD
     */
    public int getCounterD() {
        return counterD;
    }

    /**
     * Sets the connection status of this player
     * @param connected Connection status
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Sets this player's client ID.
     * Used when a player reconnects to the game.
     * @param clientID The new clientID
     */
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }
}