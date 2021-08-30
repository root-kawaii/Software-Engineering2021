package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;

import static it.polimi.ingsw.model.Resource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerControllerTest{

    MainController mainController;
    ArrayList<Player> players = new ArrayList<Player>();
    Match match;
    Lobby lobby;

    @BeforeEach
    public void init() {
        lobby = new Lobby(0,null);
        match = new Match(true, lobby);
        mainController = new MainController(match,lobby);
    }

    @Test
    @DisplayName("test for get of paternoster")
    public void getPlayerController() {
        mainController.addPlayer("root-kawaii",1);
        PlayerController test = new PlayerController(match, lobby,mainController);
        mainController.endTurn();
        mainController.addPlayer("root-kawaii2",2);
        PlayerController test1 = new PlayerController(match, lobby,mainController);
        mainController.endTurn();
        mainController.addPlayer("root-kawaii3",3);
        PlayerController test2 = new PlayerController(match, lobby,mainController);
        mainController.endTurn();
        assertTrue(match.getActivePlayer().getClientID() == match.getPlayerTurn());
    }

    @Test
    @DisplayName("testing for adding resources")
    public void insertResource(){
        mainController.addPlayer("root-kawaii",1);
        PlayerController test1 = new PlayerController(match, lobby,mainController);
        test1.insertResource(0, false);
        ArrayList<Resource> temp = new ArrayList<Resource>();
        temp.add(SERVANT);
        match.getPlayer(1).getStorage().setTemp(temp);
    }

    @Test
    @DisplayName("test for discarding resources")
    public void discardResource(){
        mainController.addPlayer("root-kawaii",1);
        mainController.addPlayer("root-kawaii2",2);
        mainController.addPlayer("root-kawaii3",3);
        mainController.addPlayer("root-kawaii4",4);
        match.getPlayers().get(0).addFaithPoints(1);
        PlayerController test1 = new PlayerController(match, lobby,mainController);
        match.nextTurn();
        ArrayList<Resource> temp = new ArrayList<Resource>();
        temp.add(SERVANT);
        match.getPlayer(1).getStorage().setTemp(temp);
        test1.insertResource(0,false);
        test1.removeResource(Resource.SERVANT,false);
        assertTrue(match.getPlayers().get(0).getFaithPoints()==1);
        test1.addVictoryPoints(match.getPlayer(2), 1);
        assertTrue(match.getActivePlayer().getVictoryPoints()==1);
    }

    @Test
    @DisplayName("we check if we can use leaders and then we use them")
    public void checkActivateandUse(){
        mainController.addPlayer("root-kawaii",0);
        PlayerController playerController = new PlayerController(match, lobby,mainController);
        EnumMap<Resource,Integer> resource = new EnumMap<>(Resource.class);
        EnumMap<Resource,Integer> resource2 = new EnumMap<>(Resource.class);
        //Bonus
        MarbleColour testColour = MarbleColour.BLUE;
        Bonus bonus = new Bonus(testColour);
        match.getActivePlayer().getStorage().addResource(COIN,2);
        match.getActivePlayer().getStorage().addInStrongBox(SHIELD,2);
        resource.put(Resource.COIN,2);
        resource2.put(Resource.SHIELD,2);
        //card
        EnumMap<Resource,Integer> uno= new EnumMap<>(Resource.class);
        EnumMap<Resource,Integer> due= new EnumMap<>(Resource.class);
        Production testo = new Production(uno,due);
        EnumMap<Resource,Integer> three = new EnumMap<>(Resource.class);
        DevelopmentCard card = new DevelopmentCard(1, CardColour.GREEN, 3, three, testo, 0);
        DevelopmentCard card2 = new DevelopmentCard(1, CardColour.PURPLE, 3, three, testo, 0);
        //card
        match.getActivePlayer().getPlayerBoard().addCard(card,1);
        match.getActivePlayer().getPlayerBoard().addCard(card2,2);
        LeaderRequirement one = new LeaderRequirement(1,CardColour.GREEN,resource);
        LeaderRequirement two = new LeaderRequirement(1,CardColour.PURPLE,resource2);
        LeaderCard Lcard = new LeaderCard(3,one,two,bonus, 0);
        playerController.checkActivateLeader(Lcard);
        match.getActivePlayer().getHand().add(Lcard);
        playerController.getLeaderCard(0);
        playerController.getLeaderCard(1);
        match.getActivePlayer().getHand().add(Lcard);
        playerController.getLeaderCard(0);
        playerController.getLeaderCard(1);
        playerController.getLeaderCard(7);
        playerController.useLeaderCard(Lcard,0);
        playerController.discardLeader(0);
        playerController.getLeaderCards();
        playerController.askLeaders();
        playerController.chooseLeaders(0,50,51);
    }

    @Test
    @DisplayName("test for disconnection")
    public void disconnectedTest() {
        PlayerController playerController = new PlayerController(match, lobby, mainController);
        mainController.addPlayer("Test", 1);
        playerController.disconnected(1);
        assertEquals(false, match.getPlayer(1).getConnected());
    }
}
