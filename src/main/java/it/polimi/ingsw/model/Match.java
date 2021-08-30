package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Lobby;

import java.util.ArrayList;

/**
 * Main model class, created by Lobby and called by controllers to act on the game state.
 * @see Lobby
 * @see it.polimi.ingsw.controller.MainController
 * @see it.polimi.ingsw.controller.BoardController
 * @see it.polimi.ingsw.controller.MarketController
 * @see it.polimi.ingsw.controller.PlayerController
 * @see it.polimi.ingsw.controller.ComputerPlayerController
 */
public class Match{
    //Adding the attribute of game to match
    //Keeping them centralized helps handling the controller
    private boolean gameStatus;
    private ArrayList<Player> players;
    private int playerTurn;
    private Table table;
    private Market market;
    protected Lobby lobby;
    protected boolean lastTurn;
    private boolean mPopeTile1=false;
    private boolean mPopeTile2=false;
    private boolean mPopeTile3=false;
    private Player winner;

    //Modified constructor to match new attributes

    /**
     * Constructor of Match, creates the players, their boards and the table.
     * @param gameStatus value to check if the game is on
     * @param lobby the lobby in which we play the match
     */
    public Match(boolean gameStatus, Lobby lobby) {
        this.gameStatus = gameStatus;
        this.players = new ArrayList<>();
        this.playerTurn = 0;
        //Insert creation of deck then create table
        //While creating table we also create market
        //Have yet to define what to pass and how to initialize(shuffle etc)
        this.table= new Table(lobby, this);
        this.market = table.getMarket();
        this.lobby = lobby;
    }

    /**
     * adds a Player to the playerList
     * @param player the player to be added
     */
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    /**
     * returns the player based on the index, after making a check with the clientID
     * @param i index of playerList
     * @return the player
     */
    public Player getPlayer(int i){
        for (Player player : players) {
            if (player.getClientID() == i) {
                return player;
            }
        }
        return null;
    }

    /**
     * returns the player
     * @return playerTurn aka clientID
     */
    public int getPlayerTurn(){
        return players.get(playerTurn).getClientID();
    }

    /**
     *
     * @return the arrayList of players
     */
    public ArrayList<Player> getPlayers(){
        return  players;
    }

    /**
     *
     * @return the active player
     */
    public Player getActivePlayer() {
        return players.get(playerTurn);
    }

    /**
     * returns the size of the arrayList players
     * @return the number of players
     */
    public int getSize(){
        return this.players.size();
    }

    /**
     *
     * @return the table associated to the match
     */
    public Table getTable() {
        return table;
    }

    /**
     *
     * @return the market associated to the match
     */
    public Market getMarket() {
        return market;
    }

    /**
     *
     * @return the next players turn
     */
    public boolean nextTurn() {
        do {
            playerTurn = (playerTurn + 1) % players.size();
        } while (!getActivePlayer().connected);
        return lastTurn && playerTurn == 0;
    }

    /**
     * sets the last turn if it is
     */
    public void setLastTurn() {
        this.lastTurn = true;
    }

    /**
     *
     * @return true if the tile is pope tile 1
     */
    public boolean ismPopeTile1() {
        return mPopeTile1;
    }

    /**
     *
     * @return true if the pope tile is pope tile 2
     */
    public boolean ismPopeTile2() {
        return mPopeTile2;
    }

    /**
     *
     * @return true if pope tile is pope 3
     */
    public boolean ismPopeTile3() {
        return mPopeTile3;
    }

    /**
     *
     * @param mPopeTile1 the pope tile 1
     */
    public void setmPopeTile1(boolean mPopeTile1) {
        this.mPopeTile1 = mPopeTile1;
    }

    /**
     *
     * @param mPopeTile2 the second pope tile
     */
    public void setmPopeTile2(boolean mPopeTile2) {
        this.mPopeTile2 = mPopeTile2;
    }

    /**
     *
     * @param mPopeTile3 the third pope tile
     */
    public void setmPopeTile3(boolean mPopeTile3) {
        this.mPopeTile3 = mPopeTile3;
    }

    /**
     * sets the winner of the game
     * @param winner the winner of the current game
     */
    public void setWinner(Player winner) {
        this.winner = winner;
    }

    /**
     *
     * @return the winner of the game
     */
    public Player getWinner() {
        return winner;
    }
}
