package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.*;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.util.ArrayList;

/**
 * Controller that handles the general aspect of the game
 */
public class MainController{
    private Match match;
    private Lobby lobby;
    private ComputerPlayerController computerPlayerController;

    /**
     * Constructor of the Main Controller.
     * We also add the controller for Computer Player.
     * @param match the match we are playing
     * @param lobby the lobby of the match
     */
    public MainController(Match match, Lobby lobby) {
        this.match = match;
        this.lobby = lobby;
        this.computerPlayerController = new ComputerPlayerController(lobby, match.getTable());
    }

    /**
     * Getter of match
     * @return the match
     */
    public Match getMatch() {
        return match;
    }

    /**
     *Method to add Player to the match.
     * @param username The username of the player
     * @param clientId The ID of the player.
     * @return the player we just added
     */
    public Player addPlayer(String username,int clientId) {
        Player player = new Player(username, clientId, this.match.getTable(), lobby);
        match.addPlayer(player);
        return player;
    }

    /**
     * Notify the client the start of its turn.
     */
    public void startTurn() {
        Command command = new Command(0, Messages.STARTTURN, String.valueOf(match.getActivePlayer().getClientID()));
        lobby.updateClients(command);
    }

    /**
     * Notify the client the end of its turn, if last turn was triggered end the game, by calling declareWinner
     */
    public void endTurn() {
        if (match.getActivePlayer().getStorage().getTemp().size() !=  0) {
            for (Player player : match.getPlayers()) {
                if (player.getClientID() != match.getPlayerTurn()) {
                    player.addFaithPoints(match.getActivePlayer().getStorage().getTemp().size());
                }
            }
            if (match.getSize() == 1) {
                computerPlayerController.addFaithPoints(match.getActivePlayer().getStorage().getTemp().size());
            }
        }
        match.getActivePlayer().getStorage().setTemp(new ArrayList<Resource>());
        if(match.getActivePlayer().getFaithPoints()>23)match.setLastTurn();
        if (match.nextTurn()) {
            declareWinner();
            Command command = new Command(0, Messages.ENDGAME, "0"+ " "+ match.getWinner().getUsername());
            lobby.updateClients(command);
            lobby.endGame();
        }
        if (match.getSize() == 1) {
            computerPlayerController.computerTurn();
        }
        startTurn();
    }

    /**
     * When the conditions for the end of the game are met we declare the winner of the game.
     * @return Winning player.
     */
    public Player declareWinner() {
        int winningPosition=0;
        int i=0;
        int max=0;
        for(i=0;i<this.match.getPlayers().size();i++){
            if(max<this.match.getPlayers().get(i).getVictoryPoints()) {
                max = this.match.getPlayers().get(i).getVictoryPoints();
                winningPosition = i;
            }
        }
        match.setWinner(this.match.getPlayers().get(winningPosition));
        return this.match.getPlayers().get(winningPosition);
    }
}
