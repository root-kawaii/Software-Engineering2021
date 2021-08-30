package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.util.EnumMap;
import java.util.Map;

/**
 * Controller that handles the board of the game
 */
public class BoardController{
    Match match;
    Lobby lobby;
    int bonus=-1;
    int bonus2=-1;

    /**
     * Class constructor.
     * Sets the match and the lobby for the controller.
     * @param match the match we are playing
     * @param lobby the lobby of the match
     */
    public BoardController(Match match,Lobby lobby) {
        this.match=match;
        this.lobby=lobby;
    }

    /**
     * Adds Resources picked by players at the start of the game to their storage.
     * @param clientID ID of the player
     * @param resources String array of resource names
     */
    public void chooseResource(int clientID, String[] resources) {
        for (String resource : resources) {
            match.getPlayer(clientID).getStorage().addResource(Resource.valueOf(resource), 1);
        }
    }

    /**
     * The method we call in order to activate Production cards.
     * If anything goes wrong we notify the Client that can perform the action again.
     * this method is very similar to the buyCard method in MarketController
     * @param production Requirements and products of a Production card
     * @see MarketController
     * @see Production
     * @param player
     * @param fromStorage Resources from the Storage the Player wants to use for this action
     * @param fromStrongbox Resources from the Strongbox the Player wants to use for this action
     * @param choice First choice for the Bonus Production granted by Leader Cards
     */
    public void activateProduction(Production production,Player player, Map<Resource,Integer> fromStorage, Map<Resource,Integer> fromStrongbox,Resource choice){
        bonus=-1;
        bonus2=-1;
        boolean pre_check=false;
        boolean check = false;
        boolean check2 = false;
        Production copy = (Production) production.clone();

        for (Map.Entry<Resource, Integer> pair : copy.getRequirements().entrySet()) {
            for (Map.Entry<Resource, Integer> storage : fromStorage.entrySet()) {
                if(storage.getKey().equals(pair.getKey())){
                    pair.setValue(pair.getValue()-storage.getValue());
                }
            }
                for (Map.Entry<Resource, Integer> strongbox : fromStrongbox.entrySet()) {
                    if (strongbox.getKey().equals(pair.getKey())) {
                        pair.setValue(pair.getValue() - strongbox.getValue());
                    }
            }
            if(pair.getValue()!=0) {
                pre_check = false;
                break;
            }
            else pre_check = true;
        }


        int x=0;
        int x1=0;
        for (Resource resource : fromStorage.keySet()) {
            x=0;
            x1=0;
            x += checkBonusStorage(0,resource);
            x1 += checkBonusStorage(1,resource);
            int i = 0;
            if (match.getActivePlayer().getStorage().getResource(resource) + x + x1 >= fromStorage.get(resource))
                check = true;
            else {
                check = false;
                break;
            }
        }

        for (Resource resource : fromStrongbox.keySet()) {
            int i = 0;
            if (match.getActivePlayer().getStorage().getStrong(resource) >= fromStrongbox.get(resource)) {
                check2 = true;
            } else {
                check2 = false;
                break;
            }
        }

        if (check && pre_check && check2) {

            int minus=0;
            int minus2=0;
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
                    }}
                        catch (Exception e){
                            Command command = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
                            lobby.updateClients(command);
                            return;
                        }
                    }
                    //remove from Strongbox
                    for(Map.Entry<Resource, Integer> strong : fromStrongbox.entrySet()){
                        try {
                            if (strong.getValue() > 0) match.getActivePlayer().getStorage().removeBox(strong.getKey(), strong.getValue());
                        }
                        catch (Exception e){
                            Command command = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
                            lobby.updateClients(command);
                            return;
                        }
                    }
            if(choice!=null){
                match.getActivePlayer().getStorage().addInStrongBox(choice,1);
            }
            for(Map.Entry<Resource, Integer> storage : production.getProducts().entrySet()){
                if(storage.getKey()!=Resource.FAITH && storage.getKey()!=Resource.CHOICE) match.getActivePlayer().getStorage().addInStrongBox(storage.getKey(), storage.getValue());
                else if(storage.getKey()==Resource.FAITH) match.getActivePlayer().addFaithPoints(storage.getValue());
            }
        }
        else{
            Command command = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
            lobby.updateClients(command);
        }
    }

    /**
     * Base production, use 2 resources to get 1 resource of choice
     * @param fromStorage
     * @param fromStrongbox
     * @param choice
     */
    public void activateBaseProduction(Map<Resource,Integer> fromStorage, Map<Resource,Integer> fromStrongbox,Resource choice){
        bonus=-1;
        bonus2=-1;
        int counter=0;
        int minus=0;
        int minus2=0;
        for(Map.Entry<Resource,Integer> check : fromStorage.entrySet()){
            counter += check.getValue();
        }
        for(Map.Entry<Resource,Integer> check2 : fromStrongbox.entrySet()){
            counter += check2.getValue();
        }
        if(counter==2) {
            for (Map.Entry<Resource, Integer> storage : fromStorage.entrySet()) {
                int x=0;
                int x1=0;
                x += checkBonusStorage(0,storage.getKey());
                x1 += checkBonusStorage(1,storage.getKey());
                minus=0;
                minus2=0;
                try {
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
                    if(storage.getValue() - minus-minus2!=0) match.getActivePlayer().getStorage().removeStorage(storage.getKey(), storage.getValue()-minus-minus2);
                } catch (Exception e) {
                    Command command = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
                    lobby.updateClients(command);
                    return;
                }
            }
            for (Map.Entry<Resource, Integer> strong : fromStrongbox.entrySet()) {
                try {
                    match.getActivePlayer().getStorage().removeBox(strong.getKey(), strong.getValue());
                } catch (Exception e) {
                    Command command = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
                    lobby.updateClients(command);
                    return;
                }
            }
            match.getActivePlayer().getStorage().addInStrongBox(choice,1);
        }
        else{
            Command command = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
            lobby.updateClients(command);
        }
    }
    /**
     * We check if the player is in a Pope Tile.
     * @param player
     * @return Whether the player is or isn't
     */
    public boolean checkPope(Player player) {
        return (player.getPlayerBoard().getFaithTrack().getTile(player.getFaithPoints()).getType().equals(TileType.POPE));
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
                if(match.getActivePlayer().getStorage().getExtra(y).get(resource)!=null) x=match.getActivePlayer().getStorage().getExtra(y).get(resource);
                if(y==0)bonus=y;
                if(y==1)bonus2=y;
            }
        }
        return x;
    }
}
