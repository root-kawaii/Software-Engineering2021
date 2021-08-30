package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.controller.MarketController;
import it.polimi.ingsw.controller.PlayerController;
import it.polimi.ingsw.model.BonusType;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.util.EnumMap;

/**
 * Server Command parser.
 */
public class Parser {
    private Lobby lobby;
    private Match match;
    private MainController mainController;
    private PlayerController playerController;
    private MarketController marketController;
    private BoardController boardController;
    private EnumMap<Resource,Integer> fromSto;
    private EnumMap<Resource,Integer> fromStr;
    private int first=0;

    /**
     * Class Constructor
     * @param lobby The Lobby a Parser is bound to
     */
    public Parser(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * Sets references to the game and controllers.
     * @param match The game's match
     * @param mainController The MainController
     * @param playerController The PlayerController
     * @param marketController The MarketController
     * @param boardController The BoardController
     * @see MainController
     * @see PlayerController
     * @see MarketController
     * @see BoardController
     */
    public void setGame(Match match, MainController mainController, PlayerController playerController, MarketController marketController, BoardController boardController) {
        this.match = match;
        this.mainController = mainController;
        this.playerController = playerController;
        this.marketController = marketController;
        this.boardController = boardController;
    }

    /**
     * Parses a Command.
     * @param command The Command to parse
     * @see Command
     */
    public void parse(Command command) {
        synchronized (lobby) {
            switch (command.getMessage()) {
                case DISCONNECTION:
                    playerController.disconnected(command.getClientID());
                    break;
                case ASKMAXPLAYERS:
                    lobby.askMaxPlayers(command.getClientID());
                    break;
                case SENDMAXPLAYERS:
                    lobby.sendMaxPlayers(Integer.parseInt(command.getArgs()[0]));
                    break;
                case ASKUSERNAME:
                    lobby.askUsername(command.getClientID());
                    break;
                case SENDUSERNAME:
                    String username = String.join(" ", command.getArgs());
                    lobby.sendUsername(command.getClientID(), username);
                    break;
                case SENDLEADERS:
                    playerController.chooseLeaders(command.getClientID(), Integer.parseInt(command.getArgs()[0]), Integer.parseInt(command.getArgs()[1]));
                    break;
                case CHOOSERESOURCE:
                    boardController.chooseResource(command.getClientID(), command.getArgs());
                    break;
                case DISCARDLEADER:
                    playerController.discardLeader(Integer.parseInt(command.getArgs()[0]));
                    break;
                case INSERTRESOURCE:
                    playerController.insertResource(Integer.parseInt(command.getArgs()[0]), command.getArgs()[1].equalsIgnoreCase("E"));
                    break;
                case REMOVERESOURCE:
                    playerController.removeResource(Resource.valueOf(command.getArgs()[0]), command.getArgs()[1].equalsIgnoreCase("E"));
                    break;
                case INSERTMARBLE:
                    marketController.insertMarble(Integer.parseInt(command.getArgs()[0]), command.getArgs().length > 1 ? Integer.parseInt(command.getArgs()[1]) : null);
                    break;
                case PLAYLEADER:
                    int param = playerController.getLeaderCard(Integer.parseInt(command.getArgs()[0]) - 1);
                    if (param != -1) {
                        playerController.useLeaderCard(match.getActivePlayer().getHand().get(param), param);
                    }
                    first = param;
                    break;
                case BUYCARD:
                    int x = Integer.parseInt(command.getArgs()[0]);
                    int y = Integer.parseInt(command.getArgs()[1]);
                    marketController.buyCard(match.getTable().getCard(x - 1), fromSto, fromStr, command.getClientID(), y);
                    break;
                case FROMSTORAGE:
                    fromSto = new EnumMap<Resource, Integer>(Resource.class);
                    for (int i = 0; i < command.getArgs().length; i += 2) {
                        if (!command.getArgs()[i].equalsIgnoreCase("")) {
                            fromSto.put(Resource.valueOf(command.getArgs()[i]), Integer.parseInt(command.getArgs()[i + 1]));
                        }
                    }
                    break;
                case FROMSTRONG:
                    fromStr = new EnumMap<Resource, Integer>(Resource.class);
                    for (int i = 0; i < command.getArgs().length; i += 2) {
                        if (!command.getArgs()[i].equalsIgnoreCase("")) {
                            fromStr.put(Resource.valueOf(command.getArgs()[i]), Integer.parseInt(command.getArgs()[i + 1]));
                        }
                    }
                    break;
                case ACTICVATEPRODUCTION:
                    Integer check = 10;
                    Resource check2 = null;
                    int bonus;
                    for (String k : command.getArgs()) {
                        if (k.equals("0") || k.equals("4") || k.equals("5") || k.equals("1") || k.equals("2") || k.equals("3")) {
                            check = Integer.parseInt(k);
                        } else {
                            check2 = Resource.valueOf(k);
                        }
                        if ((check == 0 || check == 4 || check == 5) && (check2 != null)) {
                            if (check == 4) {
                                bonus = match.getActivePlayer().getBonus().size();
                                if (bonus == 1 && match.getActivePlayer().getBonus().get(0).getType() == BonusType.PRODUCTION) {
                                    boardController.activateProduction(match.getActivePlayer().getBonus().get(0).getProduction(), match.getActivePlayer(), fromSto, fromStr, check2);
                                } else if (bonus == 2 && first == 0 && match.getActivePlayer().getBonus().get(0).getType() == BonusType.PRODUCTION) {
                                    boardController.activateProduction(match.getActivePlayer().getBonus().get(0).getProduction(), match.getActivePlayer(), fromSto, fromStr, check2);
                                } else if (bonus == 2 && first == 1 && match.getActivePlayer().getBonus().get(1).getType() == BonusType.PRODUCTION) {
                                    boardController.activateProduction(match.getActivePlayer().getBonus().get(1).getProduction(), match.getActivePlayer(), fromSto, fromStr, check2);
                                } else {
                                    Command command2 = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
                                    lobby.updateClients(command2);
                                }
                            } else if (check == 5) {
                                bonus = match.getActivePlayer().getBonus().size();
                                if (bonus == 1 && match.getActivePlayer().getBonus().get(0).getType() == BonusType.PRODUCTION) {
                                    boardController.activateProduction(match.getActivePlayer().getBonus().get(0).getProduction(), match.getActivePlayer(), fromSto, fromStr, check2);
                                } else if (bonus == 2 && first == 1 && match.getActivePlayer().getBonus().get(0).getType() == BonusType.PRODUCTION) {
                                    boardController.activateProduction(match.getActivePlayer().getBonus().get(0).getProduction(), match.getActivePlayer(), fromSto, fromStr, check2);
                                } else if (bonus == 2 && first == 0 && match.getActivePlayer().getBonus().get(1).getType() == BonusType.PRODUCTION) {
                                    boardController.activateProduction(match.getActivePlayer().getBonus().get(1).getProduction(), match.getActivePlayer(), fromSto, fromStr, check2);
                                } else {
                                    Command command2 = new Command(match.getActivePlayer().getClientID(), Messages.INVALIDMOVE, null);
                                    lobby.updateClients(command2);
                                }
                            } else {
                                boardController.activateBaseProduction(fromSto, fromStr, check2);
                            }
                        } else {
                            if (check == 1 || check == 2 || check == 3) {
                                if (match.getActivePlayer().getPlayerBoard().getCol(check - 1).size() > 0)
                                    boardController.activateProduction(match.getActivePlayer().getPlayerBoard().getCol(check - 1).get(match.getActivePlayer().getPlayerBoard().getCol(check - 1).size() - 1).getProduction(), match.getActivePlayer(), fromSto, fromStr, null);
                            }
                        }
                    }
                    mainController.endTurn();
                    break;
                case ENDTURN:
                    mainController.endTurn();
                    break;
                default:
                    Command command1 = new Command(match.getPlayerTurn(), Messages.INVALIDMOVE, null);
                    lobby.updateClients(command1);
                    break;
            }
        }
    }
}
