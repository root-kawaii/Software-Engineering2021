package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.model.MarbleColour;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.Board;
import it.polimi.ingsw.view.Card;
import it.polimi.ingsw.view.CardResources;
import it.polimi.ingsw.view.Table;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import static it.polimi.ingsw.shared.ANSI.*;

/**
 * Static class to aid in drawing various parts of the CLI.
 */
public class Draw {
    private static final String[] cardBase = {
            "┌───────────┐",
            "├───────────┤",
            "│           │",
            "│           │",
            "│           │",
            "└───────────┘"
    };
    private static final String[] info = {
            "It's your turn!",
            BGWhite + textBlack + "I" + BGBlack + textWhite + "nsert marble <1-7> + <1-2> for leader",
            BGWhite + textBlack + "B" + BGBlack + textWhite + "uy card <1-12> slot <1-3>",
            BGWhite + textBlack + "A" + BGBlack + textWhite + "ctivate Production <1-5>",
            BGWhite + textBlack + "P" + BGBlack + textWhite + "lay leader <1-2>",
            BGWhite + textBlack + "D" + BGBlack + textWhite + "iscard leader <1-2>",
            BGWhite + textBlack + "V" + BGBlack + textWhite + "iew <0-4>"
    };

    /**
     * Draws a player's board.
     * @param board The board to draw
     * @see Board
     */
    public static void drawBoard(Board board) {
        System.out.print(clearScreen);
        drawFaith(1, 1, board.getFaith(), board.getCPUFaith());
        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < board.getDevelopmentCards()[i].size(); j++) {
                if (board.getDevelopmentCard(i, j) != null) {
                    drawCard(7 - 1 * j, 1 + 14 * i, board.getDevelopmentCard(i, j));
                }
            }
        }
        if (board.getLeaderCard(0) != null) {
            drawCard(5, 43, board.getLeaderCard(0));
        } else if (board.getHandCard(0) != null) {
            drawCard(5, 43, board.getHandCard(0));
            System.out.print(placeCursor(6, 46) + "INACTIVE");
        }
        if (board.getLeaderCard(1) != null) {
            drawCard(5, 57, board.getLeaderCard(1));
        } else if (board.getHandCard(1) != null) {
            drawCard(5, 57, board.getHandCard(1));
            System.out.print(placeCursor(6, 60) + "INACTIVE");
        }
        drawStorage(14, 1, board.getShelves());
        System.out.println(placeCursor(17, 1) + "───────────");
        drawStorage(18, 1, board.getStrongBox());
        drawStorage(14, 7, board.getExtra());
        drawTemp(18, 7, board.getTemp());
        System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Viewing:" + board.getUsername() + BGBlack + textWhite);
    }

    /**
     * Prints the action you can take in a turn starting at the specified position.
     * @param row The starting row
     * @param column The starting column
     */
    public static void drawInfo(int row, int column) {
        System.out.print(placeCursor(row, column));
        int i = 0;
        for (String string : info) {
            System.out.print(string + placeCursor(row + i++, column));
        }
    }

    /**
     * Draws the table
     * @param table The Table to draw
     * @see Table
     */
    public static void drawTable(Table table) {
        System.out.print(clearScreen);
        int i = 0;
        int j = 0;
        for (Card card : table.getDecks()) {
            drawCard(1 + i * 7, 1 + j * 14, card);
            j++;
            if (j == 4)  {
                i++;
                j = 0;
            }
        }
        i = 0;
        j = 0;
        for (MarbleColour marble : table.getMarket()) {
            System.out.print(placeCursor(4 + i * 2, 61 + j * 2));
            switch (marble) {
                case BLUE:
                    System.out.print(BGBlue);
                    break;
                case YELLOW:
                    System.out.print(BGYellow);
                    break;
                case RED:
                    System.out.print(BGRed);
                    break;
                case PURPLE:
                    System.out.print(BGPurple);
                    break;
                case GREY:
                    System.out.print(BGGrey);
                    break;
                case WHITE:
                    System.out.print(BGWhite);
                    break;
            }
            System.out.print("  " + moveCursor(-1, -2) + "  ");
            j++;
            if (j == 4) {
                i++;
                j = 0;
            }
        }
        System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Viewing: Table" + BGBlack + textWhite);
    }

    /**
     * Prints the specified Storage at the specified location.
     * @param row The starting row
     * @param column The starting column
     * @param store The storage to print
     */
    public static void drawStorage(int row, int column, HashMap<Resource, Integer> store) {
        System.out.print(placeCursor(row, column));
        for (Resource resource : store.keySet()) {
            switch (resource) {
                case COIN:
                    System.out.print(BGYellow + "Co" + BGBlack + " " + store.get(resource) + moveCursor(-1, 0));
                    break;
                case SERVANT:
                    System.out.print(BGPurple + "Se" + BGBlack + " " + store.get(resource) + moveCursor(-1, 0));
                    break;
                case SHIELD:
                    System.out.print(BGBlue + "Sh" + BGBlack + " " + store.get(resource) + moveCursor(-1, 0));
                    break;
                case STONE:
                    System.out.print(BGGrey + "St" + BGBlack + " " + store.get(resource) + moveCursor(-1, 0));
                    break;
                default:
                    System.out.print(textRed + "ERR");
            }
        }
    }

    /**
     * Draws the temporary storage at the specified location.
     * @param row The starting row
     * @param column The starting column
     * @param temp The temporary storage to print
     */
    public static void drawTemp(int row, int column, ArrayList<Resource> temp) {
        System.out.print(placeCursor(row, column));
        for (Resource resource : temp) {
            switch (resource) {
                case COIN:
                    System.out.print(BGYellow + textBlack + "Co" + BGBlack + textWhite +  " ");
                    break;
                case SHIELD:
                    System.out.print(BGBlue + "Sh" + BGBlack + " ");
                    break;
                case SERVANT:
                    System.out.print(BGPurple + "Se" + BGBlack + " ");
                    break;
                case STONE:
                    System.out.print(BGGrey + "St" + BGBlack + " ");
                    break;
                default:
                    System.out.print(textRed + "ERR" + textWhite);
            }
        }
    }

    /**
     * Draws the Faith Track at the specified position
     * @param row The starting row
     * @param column The starting column
     * @param position The player's position on the track
     */
    public static void drawFaith(int row, int column, int position, int CPUPosition) {
        System.out.print(placeCursor(row, column) + BGWhite + textRed);
        for (int i = 0; i < 25; i ++) {
            if (i == 3) {
                System.out.print(BGYellow + "1┐" + BGWhite);
            } else if (i == 6) {
                System.out.print(BGYellow + "2┐" + BGWhite);
            } else if (i == 9) {
                System.out.print(BGYellow + "4┐" + BGWhite);
            } else if (i == 12) {
                System.out.print(BGYellow + "6┐" + BGWhite);
            } else if (i == 15) {
                System.out.print(BGYellow + "9┐" + BGWhite);
            } else if (i == 18) {
                System.out.print(BGYellow + "12" + BGWhite);
            } else if (i == 21) {
                System.out.print(BGYellow + "16" + BGWhite);
            } else if (i == 24) {
                System.out.print(BGYellow + "20" + BGWhite);
            } else if (i == 8 || i == 16) {
                System.out.print(BGRed + textWhite + "┌┐" + BGWhite + textRed);
            } else {
                System.out.print("┌┐");
            }
        }
        System.out.print(placeCursor(row + 1, column));
        for (int i = 0; i < 25; i ++) {
            if (i != 0 && i != 24 && i % 3 == 0) {
                System.out.print(BGYellow + "└┘" + BGWhite);
            } else if (i == 8 || i == 16 || i == 24) {
                System.out.print(BGRed + textWhite + "└┘" + BGWhite + textRed);
            } else {
                System.out.print("└┘");
            }
        }
        if (position < 25) {
            System.out.print(placeCursor(row + 1, column + position * 2) + BGBlue + textWhite + "└┘");
        } else { // if beyond 24 points just place it at the end.
            System.out.print(placeCursor(row + 1, column + 48) + BGBlue + textWhite + "└┘");
        }
        if (CPUPosition != 0) {
            if (CPUPosition < 25) {
                System.out.print(placeCursor(row, column + CPUPosition * 2) + BGBlack + textWhite + "┌┐");
            } else { // if beyond 24 points just place it at the end.
                System.out.print(placeCursor(row, column + 48) + BGBlack + textWhite + "┌┐");
            }
        }
        System.out.print(placeCursor(row + 2, column) + BGBlack + textWhite);
        System.out.print(moveCursor(0, 10) + "└──────┘" + moveCursor(0, 6) + "└────────┘" + moveCursor(0, 4) + "└──────────┘");
    }

    /**
     * Draws a card at the specified location.
     * @param row The starting row
     * @param column The starting column
     * @param card The card to draw
     * @see Card
     */
    public static void drawCard(int row, int column, Card card) {
        System.out.print(textWhite + BGBlack + placeCursor(row, column));
        for (String line : cardBase) {
            System.out.print(line + moveCursor(-1, -13));
        }
        if (card != null) {
            System.out.print(placeCursor(row + 5, column + 6)); // Print VPoints.
            System.out.print(BGYellow + card.getVictoryPoints());

            // Print colour and level at the top corners of the car if applicable.
            if (card.getColour() != null) {
                switch (card.getColour()) {
                    case BLUE:
                        System.out.print(BGBlue);
                        break;
                    case GREEN:
                        System.out.print(BGGreen);
                        break;
                    case YELLOW:
                        System.out.print(BGYellow);
                        break;
                    case PURPLE:
                        System.out.print(BGPurple);
                        break;
                    default:
                        break;
                }
                System.out.print(placeCursor(row, column));
                printLevel(card.getLevel());
                System.out.print(placeCursor(row, column + 12));
                printLevel(card.getLevel());
            }
            System.out.print(BGBlack);
            System.out.print(placeCursor(row, column + 1));
            printTop(card.getTop());
            System.out.print(placeCursor(row + 2, column + 2));
            printColumn(card.getLeft());
            System.out.print(placeCursor(row + 2, column + 8));
            printColumn(card.getRight());
            System.out.print(placeCursor(row + 2, column + 5));
            switch (card.getMiddle()) {
                case EMPTY:
                    break;
                case BRACKET:
                    System.out.print("─┐" + moveCursor(-1, -1) + "├─" + moveCursor(-1, -3) + "─┘");
                    break;
                case ARROW:
                    System.out.print(moveCursor(-1, 0) + "==>");
                    break;
                default:
                    System.out.print(BGWhite + textRed + "ERR");
                    break;
            }
        }
    }

    private static void printLevel(int level) {
        for (int i = 0; i < 3; i++) {
            if (i < level) {
                System.out.print("*" + moveCursor(-1, -1));
            } else {
                System.out.print(" " + moveCursor(-1, -1));
            }
        }
    }
    
    private static void printTop(EnumMap<CardResources, Integer> top) {
        if (top.containsKey(CardResources.LEVEL)) {
            for (CardResources resource : top.keySet()) {
                switch (resource) {
                    case BLUE:
                        System.out.print(1 + BGBlue + "**");
                        break;
                    case GREEN:
                        System.out.print(1 + BGGreen + "**");
                        break;
                    case YELLOW:
                        System.out.print(1 + BGYellow + "**");
                        break;
                    case PURPLE:
                        System.out.print(1 + BGPurple + "**");
                        break;
                    case LEVEL:
                        break;
                    default:
                        System.out.print(BGWhite + textRed + "ERR"); // If another resource appears here something went wrong.
                        break;
                }
                break;
            }
            System.out.print(BGBlack);
        } else {
            for (CardResources resource : top.keySet()) {
                switch (resource) {
                    case BLUE:
                        System.out.print(top.get(resource) + BGBlue + "  " + moveCursor(0, 1));
                        break;
                    case GREEN:
                        System.out.print(top.get(resource) + BGGreen + "  " + moveCursor(0, 1));
                        break;
                    case YELLOW:
                        System.out.print(top.get(resource) + BGYellow + "  " + moveCursor(0, 1));
                        break;
                    case PURPLE:
                        System.out.print(top.get(resource) + BGPurple + "  " + moveCursor(0, 1));
                        break;
                    case COIN:
                        System.out.print(top.get(resource) + BGYellow + "Co" + moveCursor(0, 1));
                        break;
                    case SERVANT:
                        System.out.print(top.get(resource) + BGPurple + "Se" + moveCursor(0, 1));
                        break;
                    case SHIELD:
                        System.out.print(top.get(resource) + BGBlue + "Sh" + moveCursor(0, 1));
                        break;
                    case STONE:
                        System.out.print(top.get(resource) + BGGrey + "St" + moveCursor(0, 1));
                        break;
                    default:
                        System.out.print(textRed + "ERR");
                }
                System.out.print(BGBlack);
            }
        }

    }

    private static void printColumn(EnumMap<CardResources, Integer> column) {
        for (CardResources resource : column.keySet()) {
            switch (resource) {
                case COIN:
                    System.out.print(column.get(resource) + BGYellow + "Co" + moveCursor(-1, -3));
                    break;
                case SERVANT:
                    System.out.print(column.get(resource) + BGPurple + "Se" + moveCursor(-1, -3));
                    break;
                case SHIELD:
                    System.out.print(column.get(resource) + BGBlue + "Sh" + moveCursor(-1, -3));
                    break;
                case STONE:
                    System.out.print(column.get(resource) + BGGrey + "St" + moveCursor(-1, -3));
                    break;
                case FAITH:
                    System.out.print(column.get(resource) + BGRed + "Fa" + moveCursor(-1, -3));
                    break;
                case CHOICE:
                    System.out.print(column.get(resource) + BGWhite + textBlack + "??" + moveCursor(-1, -3));
                    break;
                case DISCOUNT: // should always appear alone so we move it to the center
                    System.out.print(moveCursor(-1, 0) + "-1");
                    break;
                case MARBLE: // should always appear alone
                    System.out.print(BGWhite + "  " + moveCursor(-1, -2) + "  ");
                    break;
                case RED:
                    System.out.print(BGRed + "  " + moveCursor(-1, -2) + "  ");
                    break;
                case GREY:
                    System.out.print(BGGrey + "  " + moveCursor(-1, -2) + "  ");
                    break;
                case BLUE:
                    System.out.print(BGBlue + "  " + moveCursor(-1, -2) + "  ");
                    break;
                case YELLOW:
                    System.out.print(BGYellow + "  " + moveCursor(-1, -2) + "  ");
                    break;
                case PURPLE:System.out.print(BGPurple + "  " + moveCursor(-1, -2) + "  ");
                    break;
                default:
                    System.out.print(BGWhite + textRed + "ERR");
                    break;
            }
            System.out.print(BGBlack + textWhite);
        }
    }
}
