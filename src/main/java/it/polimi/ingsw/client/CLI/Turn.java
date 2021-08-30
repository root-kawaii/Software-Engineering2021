package it.polimi.ingsw.client.CLI;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;
import it.polimi.ingsw.view.Card;
import it.polimi.ingsw.view.CardResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;

import static it.polimi.ingsw.shared.ANSI.*;

public class Turn {
    CLI CLI;
    String[] input;

    public Turn(CLI CLI) {
        this.CLI = CLI;
    }

    public boolean takeTurn() {
        System.out.print(placeCursor(23, 1) + clearLine);
        input = CLI.getUserInput().nextLine().split(" ");
        switch (input[0].toUpperCase()) {
            case "I":
                return insertMarble();
            case "B":
                return buyCard();
            case "A":
                return activateProduction();
            case "P":
                return playLeader();
            case "D":
                return discardLeader();
            case "V":
                return view();
            default:
                return false;
        }
    }

    private boolean insertMarble() {
        try {
            if (Integer.parseInt(input[1]) > 0 && Integer.parseInt(input[1]) < 8) {
                if (input.length > 2 && Integer.parseInt(input[2]) > 0 && Integer.parseInt(input[2]) < 3) {
                    Command command = new Command(CLI.getClientID(), Messages.INSERTMARBLE, (Integer.parseInt(input[1]) - 1) + " " + (Integer.parseInt(input[2]) - 1));
                    CLI.sendMessage(command);
                    return true;
                }
                Command command = new Command(CLI.getClientID(), Messages.INSERTMARBLE, Integer.toString(Integer.parseInt(input[1]) - 1));
                CLI.sendMessage(command);
                return true;
            } else {
                System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + textWhite + BGBlack);
                return false;
            }
        } catch (IndexOutOfBoundsException | NumberFormatException exception) {
            System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + textWhite + BGBlack);
            return false;
        }
    }

    private boolean buyCard() {
        try {
            if (Integer.parseInt(input[1]) > 0 && Integer.parseInt(input[1]) < 13) {
                int index = Integer.parseInt(input[1]);
                int column =  Integer.parseInt(input[2]);
                EnumMap<Resource, Integer>[] amounts = getAmounts(CLI.getTable().getDecks()[Integer.parseInt(input[1]) - 1], true);
                ArrayList<String> storageArgs = new ArrayList<>();
                ArrayList<String> strongArgs = new ArrayList<>();
                for (Resource resource : amounts[0].keySet()) {
                    storageArgs.add(resource.name());
                    storageArgs.add(amounts[0].get(resource).toString());
                }
                for (Resource resource : amounts[1].keySet()) {
                    strongArgs.add(resource.name());
                    strongArgs.add(amounts[1].get(resource).toString());
                }
                Command command;
                command = new Command(CLI.getClientID(), Messages.FROMSTORAGE, String.join(" ", storageArgs));
                CLI.sendMessage(command);
                command = new Command(CLI.getClientID(), Messages.FROMSTRONG, String.join(" ", strongArgs));
                CLI.sendMessage(command);
                command = new Command(CLI.getClientID(), Messages.BUYCARD, index + " " + column);
                CLI.sendMessage(command);
                return true;
            } else {
                System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + textWhite + BGBlack);
                return false;
            }
        } catch (IndexOutOfBoundsException | NumberFormatException exception) {
            System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + textWhite + BGBlack);
            return false;
        }
    }

    private boolean activateProduction() {
        EnumMap<Resource, Integer>[] totals = new EnumMap[] {new EnumMap(Resource.class), new EnumMap(Resource.class)};
        ArrayList<Integer> productions = new ArrayList<>();
        ArrayList<String> args = new ArrayList<>();
        try {
            for (int i = 1; i < input.length; i++) {
                if (Integer.parseInt(input[i]) >= 0 && Integer.parseInt(input[i]) < 6) {
                    productions.add(Integer.parseInt(input[i]));
                    args.add(input[i]);
                } else {
                    System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + textWhite + BGBlack);
                    return false;
                }
            }
            Collections.sort(productions);
            for (Integer integer : productions) {
                System.out.print(clearLine + placeCursor(22, 1) + "Prod. " + integer);
                if (integer == 0) {
                    System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Basic Prod. - Choose the first resource: 1-Coin 2-Shield 3-Servant 4-Stone" + BGBlack + textWhite);
                    Resource resource1 = getChoiceResource();
                    System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Basic Prod. - Choose the second resource: 1-Coin 2-Shield 3-Servant 4-Stone" + BGBlack + textWhite);
                    Resource resource2 = getChoiceResource();
                    System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Type 2 numbers; 1-take from shelves 2-take from strongbox" + BGBlack + textWhite);
                    System.out.print(placeCursor(23, 1));
                    String[] from = CLI.getUserInput().nextLine().split(" ");
                    if (Integer.parseInt(from[0]) == 1) {
                        totals[0].put(resource1, 1);
                    } else {
                        totals[1].put(resource1, 1);
                    }
                    if (Integer.parseInt(from[1]) == 1) {
                        totals[0].put(resource2, 1);
                    } else {
                        totals[1].put(resource2, 1);
                    }
                    System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Basic Prod. - Choose resource to make: 1-Coin 2-Shield 3-Servant 4-Stone" + BGBlack + textWhite);
                    Resource choice = getChoiceResource();
                    args.add(args.indexOf(integer.toString()) + 1, choice.name());
                } else if(integer < 4 && integer > 0){
                    EnumMap<Resource, Integer>[] amounts = getAmounts(CLI.getBoard(CLI.getClientID()).getDevelopmentCard(integer - 1), false);
                    for (Resource resource : amounts[0].keySet()) {
                        if (totals[0].putIfAbsent(resource, amounts[0].get(resource)) != null) {
                            totals[0].replace(resource, amounts[0].get(resource) + amounts[0].get(resource));
                        }
                        if (totals[1].putIfAbsent(resource, amounts[1].get(resource)) != null) {
                            totals[1].replace(resource, amounts[1].get(resource) + amounts[1].get(resource));
                        }
                    }
                }
                 else {
                    EnumMap<Resource, Integer>[] amounts = getAmounts(CLI.getBoard(CLI.getClientID()).getLeaderCard(integer - 4), false);
                    for (Resource resource : amounts[0].keySet()) {
                        if (totals[0].putIfAbsent(resource, amounts[0].get(resource)) != null) {
                            totals[0].replace(resource, amounts[0].get(resource) + amounts[0].get(resource));
                        }
                        if (totals[1].putIfAbsent(resource, amounts[1].get(resource)) != null) {
                            totals[1].replace(resource, amounts[1].get(resource) + amounts[1].get(resource));
                        }
                    }
                }
                if (integer > 3)  {
                    System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Choose resource to make: 1-Coin 2-Shield 3-Servant 4-Stone" + BGBlack + textWhite);
                    Resource choice = getChoiceResource();
                    args.add(args.indexOf(integer.toString()) + 1, choice.name());
                }
            }
            ArrayList<String> storageArgs = new ArrayList<>();
            ArrayList<String> strongArgs = new ArrayList<>();
            for (Resource resource : totals[0].keySet()) {
                storageArgs.add(resource.name());
                storageArgs.add(totals[0].get(resource).toString());
            }
            for (Resource resource : totals[1].keySet()) {
                strongArgs.add(resource.name());
                strongArgs.add(totals[1].get(resource).toString());
            }
            Command command;
            command = new Command(CLI.getClientID(), Messages.FROMSTORAGE, String.join(" ", storageArgs));
            CLI.sendMessage(command);
            command = new Command(CLI.getClientID(), Messages.FROMSTRONG, String.join(" ", strongArgs));
            CLI.sendMessage(command);
            command = new Command(CLI.getClientID(), Messages.ACTICVATEPRODUCTION, String.join(" ", args));
            CLI.sendMessage(command);
            return true;
        } catch (IndexOutOfBoundsException | IllegalArgumentException exception) {
            return false;
        }
    }

    private boolean playLeader() {
        try {
            if (Integer.parseInt(input[1]) > 0 && Integer.parseInt(input[1]) < 3) {
                Command command = new Command(CLI.getClientID(), Messages.PLAYLEADER, input[1]);
                CLI.sendMessage(command);
                return true;
            } else {
                System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + textWhite + BGBlack);
                return false;
            }
        } catch (IndexOutOfBoundsException | NumberFormatException exception) {
            System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + textWhite + BGBlack);
            return false;
        }
    }

    private boolean discardLeader() {
        try {
            if (Integer.parseInt(input[1]) > 0 && Integer.parseInt(input[1]) < 3) {
                CLI.getBoard(CLI.getClientID()).setHandCard(Integer.parseInt(input[1])-1,null);
                CLI.updateView();
                Command command = new Command(CLI.getClientID(), Messages.DISCARDLEADER, input[1]);
                CLI.sendMessage(command);
                return false;
            } else {
                System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + textWhite + BGBlack);
                return false;
            }
        } catch (IndexOutOfBoundsException | NumberFormatException exception) {
            System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + textWhite + BGBlack);
            return false;
        }
    }

    private boolean view() {
        try {
            if (Integer.parseInt(input[1]) == 0) {
                CLI.switchView(0);
                return false;
            } else if (Integer.parseInt(input[1]) > 0 && Integer.parseInt(input[1]) <= CLI.getBoards().size()) {
                int i = 1;
                for (int j : CLI.getBoards().keySet()) {
                    if (i == Integer.parseInt(input[1])) {
                        CLI.switchView(j);
                        return false;
                    } else {
                        i++;
                    }
                }
            } else {
                System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + textWhite + BGBlack);
                return false;
            }
        } catch (IndexOutOfBoundsException | NumberFormatException exception) {
            System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + textWhite + BGBlack);
            return false;
        }
        System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid move, try again." + textWhite + BGBlack);
        return false;
    }

    private EnumMap[] getAmounts(Card card, boolean top) {
        EnumMap<Resource, Integer> fromStorage = new EnumMap(Resource.class);
        EnumMap<Resource, Integer> fromStrong = new EnumMap(Resource.class);

        for (CardResources resource : top ? card.getTop().keySet() : card.getLeft().keySet()) {
            System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + (top ? card.getTop().get(resource) : card.getLeft().get(resource)) + " " + resource.name() + ": Take how many from storage and strongbox? type 2 numbers" + BGBlack + textWhite);
            System.out.print(placeCursor(23, 1) + clearLine);
            String[] amounts = CLI.getUserInput().nextLine().split(" ");
            fromStorage.put(Resource.valueOf(resource.name()), Integer.parseInt(amounts[0])); // store as Integer and then go back to String to make sure the user passed numbers
            fromStrong.put(Resource.valueOf(resource.name()), Integer.parseInt(amounts[1]));
        }
        return new EnumMap[] {fromStorage, fromStrong};
    }

    private ArrayList<String> intsToArgs(ArrayList<Integer> ints) {
        ArrayList<String> args = new ArrayList<String>();
        for (int i = 0; i < ints.size(); i++) {
            args.add(ints.get(i).toString());
        }
        return args;
    }

    /**
     * Lets the player pick a resource.
     * @return The resource picked by the player
     */
    public Resource getChoiceResource() {
        System.out.print(placeCursor(23, 1));
        int choice = Integer.parseInt(CLI.getUserInput().nextLine());
        switch (choice) {
            case 1:
                return Resource.COIN;
            case 2:
                return Resource.SHIELD;
            case 3:
                return Resource.SERVANT;
            case 4:
                return Resource.STONE;
            default:
                throw  new IllegalArgumentException();
        }
    }

    /**
     * Allows a player to handle resources placed in temporary storage or end the turn.
     */
    public void handleResource() {
        boolean valid = false;
        Command command = null;
        while (!valid) {
            System.out.print(placeCursor(23, 1) + clearLine);
            String[] input = CLI.getUserInput().nextLine().split(" ");
            try {
                switch (input[0].toUpperCase()) {
                    case "I":
                        if (Integer.parseInt(input[1]) > 0 && Integer.parseInt(input[1]) < CLI.getBoards().get(CLI.getClientID()).getTemp().size() + 1); {
                        if (input[2].equalsIgnoreCase("S") || input[2].equalsIgnoreCase("E")) {
                            valid = true;
                            command = new Command(CLI.getClientID(), Messages.INSERTRESOURCE, (Integer.parseInt(input[1]) - 1) + " " + input[2]);
                        }
                    }
                    break;
                    case "R":
                        if (Integer.parseInt(input[1]) > 0 && (Integer.parseInt(input[1]) < CLI.getBoards().get(CLI.getClientID()).getShelves().size() + 1)) {
                            int i = 1;
                            for (Resource resource : CLI.getBoard(CLI.getClientID()).getShelves().keySet()) {
                                if (i == Integer.parseInt(input[1])) {
                                    valid = true;
                                    command = new Command(CLI.getClientID(), Messages.REMOVERESOURCE, resource.name() + " S");
                                } else {
                                    i++;
                                }
                            }
                        } else if (Integer.parseInt(input[1]) > 3 && Integer.parseInt(input[1]) < 6) {
                            int i = 1;
                            for (Resource resource : CLI.getBoard(CLI.getClientID()).getExtra().keySet()) {
                                if (i == Integer.parseInt(input[1])) {
                                    valid = true;
                                    command = new Command(CLI.getClientID(), Messages.REMOVERESOURCE, resource.name() + " E");
                                } else {
                                    i++;
                                }
                            }
                        }
                        break;
                    case "E":
                        valid = true;
                        command = new Command(CLI.getClientID(), Messages.ENDTURN, null);
                        break;
                    default:
                        System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid input, try again" + BGBlack + textWhite);
                        break;
                }
            } catch (NumberFormatException | IndexOutOfBoundsException exception) {
                System.out.print(placeCursor(24, 1) + BGWhite + textBlack + clearLine + "Invalid input, try again" + BGBlack + textWhite);
            }
        }
        CLI.sendMessage(command);
    }
}
