package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.util.*;
import static it.polimi.ingsw.model.MarbleColour.*;

public class Market {
    private Lobby lobby;
    private Match match;
    private MarbleColour[][] colour;
    private MarbleColour extraMarble;
    private EnumMap<MarbleColour, Integer> resourceGain;

    /**
     * Constructor of the market, which in our code represents the Marbles.
     * Creates a temp array with all the marbles and then shuffles it before the start of the game.
     * @param lobby The lobby in which the game is played
     * @param match The match itself
     */
    public Market(Lobby lobby, Match match) {
        this.lobby = lobby;
        this.match = match;
        this.resourceGain = new EnumMap<>(MarbleColour.class);
        MarbleColour[] tempArray = new MarbleColour[]{WHITE, WHITE, WHITE, WHITE, BLUE, BLUE, GREY, GREY, YELLOW, YELLOW, PURPLE, PURPLE, RED};
        Collections.shuffle(Arrays.asList(tempArray));
        this.colour = new MarbleColour[3][4];
        int i;
        int j;
        int z = 0;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 4; j++) {
                this.colour[i][j] = tempArray[z];
                z++;
            }
        }
        this.extraMarble = tempArray[z];
        updateMarbles();
    }

    /**
     *
     * @return the matrix "colour" which is our matrix with all the marbles
     */
    public MarbleColour[][] getMarbles() {
        return colour;
    }

    /**
     *
     * @return the extraMarble
     */
    public MarbleColour getExtraMarble() {
        return extraMarble;
    }

    /**
     * Inserts the extraMarble in a certain row or column of the matrix, and shifts that column or row.
     * The marbles that "falls off" the matrix is made the new extraMarble
     * @param x the idìndex representing the row or column we insert the marble
     * @return resource gain, which is an EnumMap containing the marbles of the row/column we choose.
     * @throws IllegalArgumentException if the index inserted is outside the range of (0,6)
     */
    public EnumMap<MarbleColour, Integer> insertMarble(int x) throws IllegalArgumentException {
        resourceGain = new EnumMap<>(MarbleColour.class);
        MarbleColour temp;
        temp = extraMarble;
        switch (x) {
            case 0:
                createReturny(colour, 0);
                extraMarble = colour[0][0];
                colour[0][0] = colour[1][0];
                colour[1][0] = colour[2][0];
                colour[2][0] = temp;
                break;
            case 1:
                createReturny(colour, 1);
                extraMarble = colour[0][1];
                colour[0][1] = colour[1][1];
                colour[1][1] = colour[2][1];
                colour[2][1] = temp;
                break;
            case 2:
                createReturny(colour, 2);
                extraMarble = colour[0][2];
                colour[0][2] = colour[1][2];
                colour[1][2] = colour[2][2];
                colour[2][2] = temp;
                break;
            case 3:
                createReturny(colour, 3);
                extraMarble = colour[0][3];
                colour[0][3] = colour[1][3];
                colour[1][3] = colour[2][3];
                colour[2][3] = temp;
                break;
            case 4:
                createReturnx(colour, 2);
                extraMarble = colour[2][0];
                colour[2][0] = colour[2][1];
                colour[2][1] = colour[2][2];
                colour[2][2] = colour[2][3];
                colour[2][3] = temp;
                break;
            case 5:
                createReturnx(colour, 1);
                extraMarble = colour[1][0];
                colour[1][0] = colour[1][1];
                colour[1][1] = colour[1][2];
                colour[1][2] = colour[1][3];
                colour[1][3] = temp;
                break;
            case 6:
                createReturnx(colour, 0);
                extraMarble = colour[0][0];
                colour[0][0] = colour[0][1];
                colour[0][1] = colour[0][2];
                colour[0][2] = colour[0][3];
                colour[0][3] = temp;
                break;
            default:
                throw new IllegalArgumentException();
        }
        updateMarbles();
        return resourceGain;
    }

    /**
     * Creates the resourceGain if we choose a row, since we get 4 marbles if we choose a row
     * @param colour the matrix
     * @param i the chosen row
     */
    public void createReturnx(MarbleColour[][] colour, int i) {
        for (int j = 0; j < 4; j++) {
            if (resourceGain.containsKey(colour[i][j]))
                resourceGain.replace(colour[i][j], resourceGain.get(colour[i][j]) + 1);
            else
                resourceGain.put(colour[i][j], 1);
        }
    }

    /**
     * Creates the resourceGain if we choose a column, we get 3 marbles if we choose a column.
     * @param colour the martrix
     * @param j the chosen column
     */
    public void createReturny(MarbleColour[][] colour, int j) {
        for (int i = 0; i < 3; i++) {
            if (resourceGain.containsKey(colour[i][j]))
                resourceGain.replace(colour[i][j], resourceGain.get(colour[i][j]) + 1);
            else
                resourceGain.put(colour[i][j], 1);
        }
    }

    /**
     * sends the message after an insert marble move, so that we can notify the client and update their view.
     */
    public void updateMarbles() {
        Command command;
        ArrayList<String> marbles = new ArrayList<String>();
        int i, j;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 4; j++) {
                marbles.add(colour[i][j].name());
            }
        }
        marbles.add(extraMarble.name());
        command = new Command(0, Messages.MARBLES, String.join(" ", marbles));
        lobby.updateClients(command);
    }
}