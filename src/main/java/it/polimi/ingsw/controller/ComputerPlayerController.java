package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.ComputerPlayer;
import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.Marker;
import it.polimi.ingsw.model.Table;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

/**
 * Controller that handles the single player mode of the game
 */
public class ComputerPlayerController {
    Lobby lobby;
    Table table;
    ComputerPlayer computerPlayer;

    /**
     * Constructor for the Computer Player.
     * @see ComputerPlayer
     * @param lobby the lobby we are playing in
     * @param table the table of the match
     */
    public ComputerPlayerController(Lobby lobby, Table table) {
        this.lobby = lobby;
        this.table = table;
        computerPlayer = new ComputerPlayer(lobby);
    }

    /**
     * How the computer player pops the Cards depending on the color of the Marker
     * @param marker the marker that represents the action of the computerPlayer
     */
    public void popCards(Marker marker) {
        int cardsPopped = 0;
        int index = 0;
        switch (marker) {
            case GREEN:
                index = 0;
                break;
            case BLUE:
                index = 1;
                break;
            case YELLOW:
                index = 2;
                break;
            case PURPLE:
                index = 3;
                break;
        }
        while (cardsPopped < 2) {
            DevelopmentCard card = table.getCard(index);
            if (card == null) {
                index += 4; // check next level of cards if current one is empty, will go beyond 11 if a level is empty, it's fine.
            } else {
                table.removeCard(index);
                cardsPopped++;
            }
            if (index > 11) { // we've gone "beyond" level three which means that colour of cards is gone, CPU wins.
                Command command = new Command(0, Messages.ENDGAME, "0"+ " "+computerPlayer.getUsername());
                lobby.updateClients(command);
                break;
            }
        }
    }

    /**
     * Add Faith points to Computer player
     * @param amount the amount of faith points we want to add
     */
    public void addFaithPoints(int amount) {
        computerPlayer.addFaithPoints(amount);
    }

    /**
     * Actions the computer can perform during its turn depending on the marker.
     */
    public void computerTurn() {
        Marker marker = computerPlayer.popMarker();
        switch (marker) {
            case GREEN:
            case BLUE:
            case YELLOW:
            case PURPLE:
                popCards(marker);
                break;
            case FAITH:
                computerPlayer.addFaithPoints(2);
                break;
            case SHUFFLE:
                computerPlayer.addFaithPoints(1);
                computerPlayer.newMarkers();
        }
    }
}
