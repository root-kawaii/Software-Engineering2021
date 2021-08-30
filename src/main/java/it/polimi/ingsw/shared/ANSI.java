package it.polimi.ingsw.shared;

/**
 * Helps drawing the CLI.
 * Contains various static constants and methods to manipulate the cursor and text
 */
public final class ANSI {
    public static final String clearScreen = "[2J";
    public static final String clearLine = "[2K";
    public static final String textBlack = "[38;2;0;0;0m";
    public static final String textGrey = "[38;2;128;128;128m";
    public static final String textWhite = "[38;2;255;255;255m";
    public static final String textRed = "[38;2;255;0;0m";
    public static final String textGreen = "[38;2;0;255;0m";
    public static final String textBlue = "[38;2;0;0;255m";
    public static final String textYellow = "[38;2;255;255;0m";
    public static final String textPurple = "[38;2;128;0;255m";
    public static final String BGBlack = "[48;2;0;0;0m";
    public static final String BGGrey = "[48;2;128;128;128m";
    public static final String BGWhite = "[48;2;255;255;255m";
    public static final String BGRed = "[48;2;255;0;0m";
    public static final String BGGreen = "[48;2;0;255;0m";
    public static final String BGBlue = "[48;2;0;0;255m";
    public static final String BGYellow = "[48;2;255;255;0m";
    public static final String BGPurple = "[48;2;128;0;255m";

    /**
     * Places the cursor at specific coordinates.
     * @param row The row of the cursor (0 = top)
     * @param column The column of the cursor (0 = left)
     * @return A string that moves the cursor to row, column when printed
     */
    public static final String placeCursor(int row, int column) { // Place Cursor at absolute position x, y
        return "[" + row + ";" + column + "H";
    }

    /**
     * Moves the cursor by a specified amount.
     * @param rows How many rows the cursor will move (positive = down)
     * @param columns How many columns the cursor will move (positive = right)
     * @return A string that moves the cursor by rows, columns when printed
     */
    public static final String moveCursor(int rows, int columns) { // Move cursor by x, y
        String xMov;
        String yMov;
        if (rows == 0) {
            yMov = "";
        } else if (rows > 0) {
            yMov = "[" + rows + "A";
        } else {
            yMov = "[" + -rows + "B";
        }
        if (columns == 0) {
            xMov = "";
        } else if (columns > 0) {
            xMov = "[" + columns + "C";
        } else {
            xMov = "[" + -columns + "D";
        }
        return xMov + yMov;
    }
}
