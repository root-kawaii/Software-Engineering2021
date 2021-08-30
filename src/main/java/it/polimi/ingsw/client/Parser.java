package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.shared.Command;

import java.util.ArrayList;

/**
 * Client-side Parser.
 * @see Client
 */
public class Parser {
    Client client;

    /**
     * Class constructor.
     * @param client Reference to the Client this is bound to
     * @see Client
     */
    public Parser(Client client) {
        this.client = client;
    }

    /**
     * Parses a Command.
     * Parses commands and calls the appropriate Client functions
     * @param command The Command to parse
     * @see Command
     * @see Client
     */
    public void parse(Command command) {
        switch (command.getMessage()) {
            case CONNECTIONOK:
                client.connectionOk(Integer.parseInt(command.getArgs()[0]));
                break;
            case RECONNECT:
                client.reconnect(String.join(" ", command.getArgs()));
                break;
            case STATUS:
                client.updateStatus(command.getClientID(), command.getArgs());
                break;
            case UPDATEID:
                client.updateID(command.getClientID(), Integer.parseInt(command.getArgs()[0]));
                break;
            case ASKMAXPLAYERS:
                client.askMaxPlayers();
                break;
            case ASKUSERNAME:
                client.askUsername();
                break;
            case INVALIDUSERNAME:
                client.invalidUsername();
                break;
            case MARBLES:
                client.setMarbles(command.getArgs());
                break;
            case SETMARKETCARDS:
                client.setDecks(command.getArgs());
                break;
            case SETMARKETCARD:
                client.setMarketCard(Integer.parseInt(command.getArgs()[0]), Integer.parseInt(command.getArgs()[1]));
                break;
            case ASKLEADERS:
                if (command.getClientID() == client.getClientID()) {
                    client.askLeader(command.getArgs());
                }
                break;
            case STARTGAME:
                client.startGame(command.getArgs());
                break;
            case ENDGAME:
                client.endGame(command.getClientID(),command.getArgs()[1]);
                break;
            case CHOOSERESOURCE:
                if (command.getClientID() == client.getClientID()) {
                    client.chooseResource(command.getClientID(), Integer.parseInt(command.getArgs()[0]));
                }
                break;
            case STARTTURN:
                client.startTurn(Integer.parseInt(command.getArgs()[0]));
                break;
            case SETFAITHPOINTS:
                client.setFaithPoints(command.getClientID(), Integer.parseInt(command.getArgs()[0]));
                break;
            case SETVICTORYPOINTS:
                client.setVictoryPoints(command.getClientID(), Integer.parseInt(command.getArgs()[0]));
                break;
            case SETRESOURCE:
                client.setResource(command.getClientID(), Resource.valueOf(command.getArgs()[0]), Integer.parseInt(command.getArgs()[1]));
                break;
            case SETSTRONG:
                client.setStrongbox(command.getClientID(), Resource.valueOf(command.getArgs()[0]), Integer.parseInt(command.getArgs()[1]));
                break;
            case SETEXTRA:
                client.setExtra(command.getClientID(), Resource.valueOf(command.getArgs()[0]), Integer.parseInt(command.getArgs()[1]));
                break;
            case SETTEMP:
                ArrayList<Resource> resources = new ArrayList<Resource>();
                for (int i = 0; i < command.getArgs().length; i++) {
                    if (!command.getArgs()[i].equalsIgnoreCase("")) {
                        resources.add(Resource.valueOf(command.getArgs()[i]));
                    }
                }
                client.setTemp(command.getClientID(), resources);
                break;
            case ADDCARD:
                client.addCard(command.getClientID(), Integer.parseInt(command.getArgs()[0]), Integer.parseInt(command.getArgs()[1]));
                break;
            case ADDLEADER:
                client.addLeader(command.getClientID(), Integer.parseInt(command.getArgs()[0]), Integer.parseInt(command.getArgs()[1]));
                break;
            case RESOURCEGAIN:
                client.resourceGain(command.getClientID());
                break;
            case OKRESOURCEHANDLE:
                client.okAddResource(command.getClientID());
                break;
            case KORESOURCEHANDLE:
                client.koAddResource(command.getClientID());
                break;
            case INVALIDMOVE:
                client.invalidMove(command.getClientID());
                break;
            default:
                client.generic(command);
                break;
        }
    }
}
