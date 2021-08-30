package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.MarbleColour;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;

import java.util.ArrayList;
import java.util.logging.Logger;

// Server class sets up the Server attributes, starts the ClientListener and proceeds to handle lobbies.
// Includes some methods to be called by Lobby or ClientHandler.

/**
 * Main Server class to handle games.
 * The class creates a listener to receive connections and Lobbies to hold clients.
 * When a Player connects it is added to a Lobby.
 * @see ClientListener
 * @see Lobby
 */
public class Server implements Runnable{
    private static final Logger logger = Logger.getGlobal();
    private static boolean quit;
    private static Thread clientListener;
    private static ArrayList<Lobby> lobbies;
    private static int lobbyID;
    private static ArrayList<ClientHandler> waitingList;
    private static Lobby lobbyReconnecting;
    private static ClientHandler clientReconnecting;
    private static Player playerReconnecting;

    private Server(){
        quit = false;
        lobbyID = 0;
        lobbies = new ArrayList<Lobby>();
        waitingList = new ArrayList<ClientHandler>();
        clientListener = new Thread(new ClientListener(this));
    }

    /**
     * Creates a Server.
     * @param args Main method arguments String
     * @see Server
     */
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");
        if (args != null && args[0].equalsIgnoreCase("local")) {
            logger.setUseParentHandlers(false);
        }
        logger.info("Server starting.");
        Thread server = new Thread(new Server());
        clientListener.start();
        server.start();
        logger.info("Server Started.");
    }

    /**
     * Adds a client to the waiting list.
     * Clients are first added to a waiting list before being assigned to a Lobby.
     * @param client The ClientHandler to add to the waiting list.
     * @see ClientHandler
     */
    public static void addWaiting(ClientHandler client){
        synchronized (lobbies) {
            if (clientReconnecting == null) {
                for (Lobby lobby : lobbies) {
                    if (lobby.getRunning()) {
                        for (Player player : lobby.getMainController().getMatch().getPlayers()) {
                            if (!player.getConnected()) {
                                clientReconnecting = client;
                                playerReconnecting = player;
                                lobbyReconnecting = lobby;
                                Command command = new Command(0, Messages.RECONNECT, player.getUsername());
                                client.sendMessage(command);
                                logger.info("Asking client " + client.getClientID() + " for reconnection.");
                                return;
                            }
                        }
                    }
                }
            }
        }
        synchronized (waitingList) {
            waitingList.add(client);
            logger.info("Client added to waiting list.");
        }
    }

    /**
     * A player has accepted to reconnect.
     * Update their connection status and send the game status.
     */
    public void okreconnect() {
        synchronized (lobbyReconnecting) {
            Command command;
            command = new Command(playerReconnecting.getClientID(), Messages.UPDATEID, Integer.toString(clientReconnecting.getClientID()));
            lobbyReconnecting.updateClients(command);
            clientReconnecting.setLobby(lobbyReconnecting);
            playerReconnecting.setClientID(clientReconnecting.getClientID());
            playerReconnecting.setConnected(true);
            lobbyReconnecting.insertPlayer(clientReconnecting);
            command = new Command(0, Messages.STATUS, String.join(" ", tableStatus()));
            clientReconnecting.sendMessage(command);
            for (Player player : lobbyReconnecting.getMainController().getMatch().getPlayers()) {
                command = new Command(player.getClientID(), Messages.STATUS, String.join(" ", playerStatus(player)));
                clientReconnecting.sendMessage(command);
            }
            playerReconnecting = null;
            lobbyReconnecting = null;
            clientReconnecting = null;
        }
    }

    //  Prepares a string of card IDs and Marbles to be sent to the reconnecting client
    private String[] tableStatus() {
        int i = 0;
        String[] status = new String[25];
        for (Card card : lobbyReconnecting.getMainController().getMatch().getTable().getDecks()) {
            status[i] = Integer.toString(card.getID());
            i++;
        }
        for (MarbleColour[] marbleRow : lobbyReconnecting.getMainController().getMatch().getMarket().getMarbles()) {
            for (MarbleColour marble : marbleRow) {
                status[i] = marble.name();
                i++;
            }
        }
        status[24] = lobbyReconnecting.getMainController().getMatch().getMarket().getExtraMarble().name();
        return status;
    }

    // Prepares a string to update the status of a player
    // In order send: 9 development card, 2 leader cards, 2 hand cards, victory points, faith points, 4 storage resources, 4 strongbox resources, 4 extra resource
    private String[] playerStatus(Player player) {
        String[] status = new String[28];
        int i = 0;
        for (Card card : player.getPlayerBoard().getCol0()) {
            status[i] = Integer.toString(card.getID());
            i++;
        }
        i = 3;
        for (Card card : player.getPlayerBoard().getCol1()) {
            status[i] = Integer.toString(card.getID());
            i++;
        }
        i = 6;
        for (Card card : player.getPlayerBoard().getCol1()) {
            status[i] = Integer.toString(card.getID());
            i++;
        }
        status[9]  = player.getPlayerBoard().getLeaders().size() > 0 ? Integer.toString(player.getPlayerBoard().getLeader(0).getID()) : null;
        status[10] = player.getPlayerBoard().getLeaders().size() > 1 ? Integer.toString(player.getPlayerBoard().getLeader(1).getID()) : null;
        status[11] = player.getHand().size() > 0 ? Integer.toString(player.getHand().get(0).getID()) : null;
        status[12] = player.getHand().size() > 1 ? Integer.toString(player.getHand().get(1).getID()) : null;
        status[13] = Integer.toString(player.getVictoryPoints());
        status[14] = Integer.toString(player.getFaithPoints());
        i = 15;
        for (Resource resource : Resource.values()) {
            if (resource == Resource.FAITH || resource == Resource.CHOICE) {}
            else {
                status[i] = Integer.toString(player.getStorage().getResource(resource));
                i++;
            }
        }
        for (Resource resource : Resource.values()) {
            if (resource == Resource.FAITH || resource == Resource.CHOICE) {}
            else {
                status[i] = Integer.toString(player.getStorage().getStrong(resource));
                i++;
            }
        }
        for (Resource resource : player.getStorage().getExtra(0).keySet()) {
        status[23] = resource.name();
        status[24] = Integer.toString(player.getStorage().getExtra(0).get(resource));
        }
        for (Resource resource : player.getStorage().getExtra(1).keySet()) {
            status[25] = resource.name();
            status[26] = Integer.toString(player.getStorage().getExtra(0).get(resource));
        }
        status[27] = player.getUsername();
        return status;
    }

    /**
     * A player has refused to reconnect.
     * Add them to the waiting list as normal.
     */
    public void koreconnect() {
        waitingList.add(clientReconnecting);
        clientReconnecting = null;
        lobbyReconnecting = null;
        logger.info("Client refused reconnection, added to waiting list.");
    }

    /**
     * Returns how many clients are waiting to be assigned to a Lobby.
     * @return The number of clients in the queue
     * @see ClientHandler
     */
    public static int getWaitingSize() {
        synchronized (waitingList) {
            return waitingList.size();
        }
    }

    /**
     * Removes a Lobby from the Server.
     * Once a game ends the Lobby removes itself from the list of ongoing games.
     * @param lobby the Lobby to be removed.
     * @see Lobby
     */
    public static void removeLobby(Lobby lobby) {
        lobbies.remove(lobbies.indexOf(lobby));
    }

    // if a Lobby's max players is set to 0 it means it's being set up

    /**
     * The Server's main loop.
     * It checks if there are clients in the waiting lists and assigns them to the first Lobby with space available; if
     * no Lobbies are available, one is created.
     * @see Lobby
     * @see ClientHandler
     */
    @Override
    public void run() {
        while (!quit) {
            synchronized (waitingList) {
                if (!waitingList.isEmpty()) {
                    synchronized (lobbies) { // Check if no lobbies exist, check if latest lobby isn't being set up and if it's full
                        if (lobbies.isEmpty() || lobbies.get(lobbies.size() - 1).getMaxPlayers() != 0 && lobbies.get(lobbies.size() - 1).getCurrentPlayers() == lobbies.get(lobbies.size() - 1).getMaxPlayers()) {
                            Lobby lobby = new Lobby(lobbyID, this);
                            Thread lobbyThread = new Thread(lobby);
                            lobbies.add(lobby);
                            lobbyID++;
                            lobbyThread.start();
                            logger.info("Created Lobby " + lobbyID);
                            lobbies.get(lobbies.size() - 1).addPlayer(waitingList.remove(0));
                            logger.info("Player added.");
                        } else if (lobbies.get(lobbies.size() - 1).getMaxPlayers() != 0 && lobbies.get(lobbies.size() - 1).getCurrentPlayers() < lobbies.get(lobbies.size() - 1).getMaxPlayers()) {
                            lobbies.get(lobbies.size() - 1).addPlayer(waitingList.remove(0));
                        } // Do nothing if a lobby is available but not set up
                    }
                }
            }
        }
    }
}

