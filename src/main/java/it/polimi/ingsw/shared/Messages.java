package it.polimi.ingsw.shared;

/**
 * Enum of all the messages that a Client or Lobby might send.
 */
public enum Messages {
    PING,
    PONG,
    DISCONNECTION,
    STATUS,
    RECONNECT, // Ask client if they wish to reconnect to a match
    OKRECONNECT, // Accept reconnection
    KORECONNECT, // Refuse reconnection
    UPDATEID,
    CONNECTIONOK, // Argument is the client's ID.
    ASKMAXPLAYERS,
    SENDMAXPLAYERS, // Argument is the number of players.
    INVALIDMAXPLAYERS,
    ASKUSERNAME,
    SENDUSERNAME,
    INVALIDUSERNAME,
    STARTGAME, // Arguments are clientID and usernames of all players.
    CHOOSERESOURCE,
    SETRESOURCE,
    SETTEMP,
    SETSTRONG,
    SETEXTRA,
    SETMARKETCARDS, // Arguments are IDs of the cards on top of each deck, used at the start of the match
    SETMARKETCARD, // Arguments are deck number and ID of the card, -1 means empty stack
    BUYCARD,
    ADDCARD,
    ASKLEADERS,
    SENDLEADERS,
    ADDLEADER,
    MARBLES, // Arguments are all marbles, left to right, top to bottom and the extra marble at the end
    INSERTMARBLE,
    RESOURCEGAIN,
    SETFAITHPOINTS,
    SETVICTORYPOINTS,
    STARTTURN,
    ACTICVATEPRODUCTION,
    PLAYLEADER,
    DISCARDLEADER,
    INVALIDMOVE,
    INSERTRESOURCE,
    REMOVERESOURCE,
    OKRESOURCEHANDLE,
    KORESOURCEHANDLE,
    FROMSTORAGE,
    FROMSTRONG,
    ENDTURN,
    ENDGAME // Argument is ID of the winner, 0 for ComputerPlayer
}
