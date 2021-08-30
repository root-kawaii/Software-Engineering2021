package it.polimi.ingsw.shared;

import java.util.Arrays;

/**
 * Represents a command executed by a player of by the server.
 */
public class Command {
    private int clientID;
    private Messages message;
    private String[] args;

    /**
     * Class constructor starting from a String.
     * The String should have in order the client's ID, a Message, and optionally arguments for it.
     * @param command The String to create a Command from.
     * @see Messages
     */
    public Command(String command) {
        String[] temp = command.split(" ");
        this.clientID = Integer.parseInt(temp[0]);
        this.message = Messages.valueOf(temp[1]);
        this.args = Arrays.copyOfRange(temp, 2, command.length());
    }

    /**
     * Class constructor starting from parameters.
     * @param clientID The client's ID
     * @param message The Message to execute
     * @param args The Message's arguments
     * @see Messages
     */
    public Command(int clientID, Messages message, String args) {
        this.clientID = clientID;
        this.message = message;
        if (args != null) {
            this.args = args.split(" ");
        } else {
            this.args = null;
        }
    }

    /**
     * Gets the client's ID.
     * Returns the ID of the player who created the Command or to which the Command is addressed. 0 is used for commands
     * from the server or that affect all players.
     * @return The client's ID
     */
    public int getClientID() {
        return this.clientID;
    }

    /**
     * Gets the Message of the Command.
     * @return The Message to be executed.
     * @see Messages
     */
    public Messages getMessage() {
        return this.message;
    }

    /**
     * Gets the Message's arguments.
     * @return The Message's arguments
     * @see Messages
     */
    public String[] getArgs(){
        return this.args;
    }
}
