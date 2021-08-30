package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.util.Collections;
import java.util.Stack;

/**
 * class the handles the Turns of "Lorenzo il magnifico" when playing single player
 */
public class ComputerPlayer {
    private Stack<Marker> markers;
    private String username;
    private int faithPoints;
    private Lobby lobby;

    /**
     * Constructor of the class
     * @param lobby lobby in which the game will be played
     */
    public ComputerPlayer(Lobby lobby) {
        this.username = "Lorenzo il Magnifico";
        this.faithPoints = 0;
        this.lobby = lobby;
        newMarkers();
    }

    /**
     * creates the marker Stack and shuffles it, used by the computer when playing singleplayer
     */
    public void newMarkers() {
        markers = new Stack<Marker>();
        for (Marker marker : Marker.values()) {
            markers.push(marker);
        }
        markers.push(Marker.FAITH); // 7 total markers, 2 Faith ones and one of all the others
        Collections.shuffle(markers);
    }

    /**
     * pops the top stacker
     * @return marker
     */
    public Marker popMarker() {
        return markers.pop();
    }

    /**
     *
     * @return markers
     */
    public Stack<Marker> getMarkers(){
        return markers;
    }

    /**
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * returns the faith points of the computer player
     * @return faith points
     */
    public int getFaithPoints() {
        return faithPoints;
    }

    /**
     * Adds faith points to the player if the stack pops a marker that increases them
     * @param points the number of faith points we want to add
     */
    public void addFaithPoints(int points) {
        Command command;
        this.faithPoints += points;
        command = new Command(0, Messages.SETFAITHPOINTS, String.valueOf(faithPoints));
        lobby.updateClients(command);
        if (faithPoints > 23) { // Computer has reached the end of the faith track
            command = new Command(0, Messages.ENDGAME, "0"+ " "+ username);
            lobby.updateClients(command);
        }
    }
}
