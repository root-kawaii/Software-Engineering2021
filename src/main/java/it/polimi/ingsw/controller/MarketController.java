package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * Controller that handles the market of the game.
 */
public class MarketController {
    MainController mainController;
    Match match;
    Lobby lobby;
    int bonus=-1;
    int bonus2=-1;


    /**
     * The constructor for Market Controller.
     *
     * @param match the match we are playing
     * @param lobby the lobby of the match
     * @param mainController the main controller of the match
     */
    public MarketController(Match match, Lobby lobby, MainController mainController) {
        this.mainController = mainController;
        this.match = match;
        this.lobby = lobby;
    }

    /**
     * Method that handles the insertion of the marble and the resources we obtain.
     *
     * @param x      The position of the marble we insert, goes from 0 to 6 (bottom left to top right)
     * @param choice Choice for blank marbles in case of Bonus
     * @see Market
     */
    public void insertMarble(int x, Integer choice) {
        Resource replace = null;
        if (match.getActivePlayer().getBonus().size() == 2 && match.getActivePlayer().getBonus().get(0).getType() == BonusType.MARBLE && match.getActivePlayer().getBonus().get(1).getType() == BonusType.MARBLE && choice != null) {
            replace = convertColorToResource(match.getActivePlayer().getBonus().get(choice).getMarble());
        } else if (match.getActivePlayer().getBonus().size() == 2 && (match.getActivePlayer().getBonus().get(0).getType() == BonusType.MARBLE || match.getActivePlayer().getBonus().get(1).getType() == BonusType.MARBLE)) {
            replace = convertColorToResource(match.getActivePlayer().getBonus().get(0).getType() == BonusType.MARBLE ? match.getActivePlayer().getBonus().get(0).getMarble() : match.getActivePlayer().getBonus().get(1).getMarble());
        } else if (match.getActivePlayer().getBonus().size() == 1 && match.getActivePlayer().getBonus().get(0).getType() == BonusType.MARBLE) {
            replace = convertColorToResource(match.getActivePlayer().getBonus().get(0).getMarble());
        } else {
        }
        EnumMap<MarbleColour, Integer> marbles = match.getMarket().insertMarble(x);
        ArrayList<Resource> temp = new ArrayList<>();
        for (MarbleColour colour : marbles.keySet()) {
            for (int i = 0; i < marbles.get(colour); i++) {
                if (colour == MarbleColour.RED) {
                    match.getActivePlayer().addFaithPoints(marbles.get(colour));
                    break;
                } else if (colour == MarbleColour.WHITE) {
                    if (replace != null) {
                        temp.add(replace);
                    }
                } else {
                    temp.add(convertColorToResource(colour));
                }
            }
        }
        match.getActivePlayer().getStorage().setTemp(temp);
        Command command = new Command(match.getPlayerTurn(), Messages.RESOURCEGAIN, null);
        lobby.updateClients(command);
    }

    /**
     * Conversion from colour to Resource.
     *
     * @param color
     * @return the resource we converted
     * @see Resource
     */
    public Resource convertColorToResource(MarbleColour color) {
        switch (color) {
            case RED:
                return Resource.FAITH;
            case BLUE:
                return Resource.SHIELD;
            case PURPLE:
                return Resource.SERVANT;
            case YELLOW:
                return Resource.COIN;
            case GREY:
                return Resource.STONE;
        }
        return Resource.CHOICE;
    }

    /**
     * Method that allows the Player to buy a Card and checks if the action is possible.
     *
     * @param developmentCard Card to buy
     * @param clientID        Client of the Player
     * @param position        Position to insert the card in the board
     * @param fromStorage     Resources from the Storage the Player wants to use for this action
     * @param fromStrong      Resources from the Strongbox the Player wants to use for this action
     * @return the development card we just bought
     */
    public DevelopmentCard buyCard(DevelopmentCard developmentCard, Map<Resource, Integer> fromStorage, Map<Resource, Integer> fromStrong, int clientID, int position) {
        boolean pre_check = false;
        boolean check = false;
        boolean check2 = false;
        DevelopmentCard copy =(DevelopmentCard) developmentCard.clone();

        //checks if the player has an active bonus type discount, of one of the resource we need to buy the card.
        //if yes decreases the cost of the discounted resource by one
        for (Map.Entry<Resource, Integer> pair : copy.getCost().entrySet()) {
            if (match.getActivePlayer().getBonus().size() > 0 && match.getActivePlayer().getBonus().get(0).getType() == BonusType.DISCOUNT) {
                if (match.getActivePlayer().getBonus().get(0).getDiscount() == pair.getKey())
                    pair.setValue(pair.getValue() - 1);
            }
            if (match.getActivePlayer().getBonus().size() > 1 && match.getActivePlayer().getBonus().get(1).getType() == BonusType.DISCOUNT) {
                if (match.getActivePlayer().getBonus().get(1).getDiscount() == pair.getKey())
                    pair.setValue(pair.getValue() - 1);
            }
        }
        //checks if we have enough resource to buy the card by subtracting the resources present in storage from the one
        //needed to buy the card
        for (Map.Entry<Resource, Integer> pair : copy.getCost().entrySet()) {
            //checks in storage
            for (Map.Entry<Resource, Integer> storage : fromStorage.entrySet()) {
                if (storage.getKey() == pair.getKey()) {
                    pair.setValue(pair.getValue() - storage.getValue());
                }
            }
            //checks in strongBox
            for (Map.Entry<Resource, Integer> strongbox : fromStrong.entrySet()) {
                if (strongbox.getKey() == pair.getKey()) {
                    pair.setValue(pair.getValue() - strongbox.getValue());
                }
            }
            //if we we don't have enough resources pre_check is false
            if (pair.getValue() != 0) {
                pre_check = false;
                break;
            } else pre_check = true;
        }

        //checks if we have enough resource in storage + the extra storage if present
        int x=0;
        int x1=0;
        for (Resource resource : fromStorage.keySet()) {
            x=0;
            x1=0;
            x += checkBonusStorage(0,resource);
            x1 += checkBonusStorage(1,resource);
            int i = 0;
            if (match.getActivePlayer().getStorage().getResource(resource) + x + x1 >= fromStorage.get(resource)) {
                check = true;
            } else {
                check = false;
                break;
            }
        }
        //checks if we have enough resource in strongbox
        for (Resource resource : fromStrong.keySet()) {
            int i = 0;
            if (match.getActivePlayer().getStorage().getStrong(resource) >= fromStrong.get(resource)) {
                check2 = true;
            } else {
                check2 = false;
                break;
            }
        }
        //if all the checks are true we can buy the card
        if (check && pre_check && check2) {
            if (match.getActivePlayer().getPlayerBoard().addCard(developmentCard, position - 1)) {
                match.getTable().removeCard(developmentCard, clientID);

                    //just a print to check resources needed
                    //System.out.printf("Key (name) is: %s, Value (quantity) is : %s%n", pair.getKey(), pair.getValue());
                    //remove from shelves
                    int minus=0;
                    int minus2=0;
                    //we remove from storage the requested resources,
                    //we make a last test on the extra storage resources
                    for (Map.Entry<Resource, Integer> storage : fromStorage.entrySet()) {
                        x=0;
                        x1=0;
                        x += checkBonusStorage(0,storage.getKey());
                        x1 += checkBonusStorage(1,storage.getKey());
                        minus=0;
                        minus2=0;
                        try {
                            if (storage.getValue() > 0) {
                                if (x != 0 && bonus != -1) {
                                    while (match.getActivePlayer().getStorage().getExtra(bonus).get(storage.getKey()) > 0) {
                                        match.getActivePlayer().getStorage().removeExtra(storage.getKey(), 1);
                                        minus++;
                                    }
                                }
                                if (x1 != 0 && bonus2 != -1) {
                                    while (match.getActivePlayer().getStorage().getExtra(bonus2).get(storage.getKey()) > 0) {
                                        match.getActivePlayer().getStorage().removeExtra(storage.getKey(), 1);
                                        minus2++;
                                    }
                                }
                                if(storage.getValue() - minus-minus2!=0) match.getActivePlayer().getStorage().removeStorage(storage.getKey(), storage.getValue() - minus-minus2);
                            }
                        } catch (Exception e) {
                            Command command = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
                            lobby.updateClients(command);
                            return developmentCard;
                        }
                    }
                    //remove from Strongbox
                    for (Map.Entry<Resource, Integer> strong : fromStrong.entrySet()) {
                        try {
                            if (strong.getValue() > 0) {
                                match.getActivePlayer().getStorage().removeBox(strong.getKey(), strong.getValue());
                            }
                        } catch (Exception e) {
                            Command command = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
                            lobby.updateClients(command);
                            return developmentCard;
                        }
                    }

            } else {
                Command command1 = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
                lobby.updateClients(command1);
                return developmentCard;
            }
        } else {
            Command command2 = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
            lobby.updateClients(command2);
            return developmentCard;
        }
        mainController.endTurn();
        return developmentCard;
    }

    /**
     * checks if the extra storage resource matches the resource
     * @param y an index that represent the to possible extras
     * @param resource the resource we want to check
     * @return the quantity of resource we have in extra storage if the check is true
     */
    public int checkBonusStorage(int y,Resource resource){
        int x=0;
        if (match.getActivePlayer().getBonus().size() > y && match.getActivePlayer().getBonus().get(y).getType() == BonusType.STORAGE) {
            if (Resource.valueOf(match.getActivePlayer().getBonus().get(y).getStorage()) == resource) {
                x=match.getActivePlayer().getStorage().getExtra(y).get(resource);
                if(y==0)bonus=y;
                if(y==1)bonus2=y;
            }
        }
        return x;
    }




}
