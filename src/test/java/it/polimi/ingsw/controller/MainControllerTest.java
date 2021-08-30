package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;

public class MainControllerTest {

    MainController mainController;
    Lobby lobby;
    Match match;
    ArrayList<Player> players = new ArrayList<Player>();

    @BeforeEach
    @DisplayName("create controller and sets match")
    public void createController() {
        lobby = new Lobby(0,null);
        match = new Match(true, lobby);
        mainController = new MainController(match, lobby);
    }

    @Test
    @DisplayName("should add player")
    public void addPlayer() {
        mainController.addPlayer("root-kawaii", 0);
        assertTrue(match.getSize() == 1);
        mainController.addPlayer("red-debu", 1);
        mainController.addPlayer("aero-lalo", 2);
        assertTrue(match.getSize() == 3);
        System.out.println("-----------------------");
        players.stream()
                .map(x -> x.getUsername())
                .forEach(System.out::println);
        ArrayList<Resource> temp = new ArrayList<>();
        match.getActivePlayer().getStorage().setTemp(temp);
        match.getActivePlayer().getStorage().getTemp().add(0, Resource.COIN);
        mainController.endTurn();
        mainController.declareWinner();
        mainController.getMatch();

    }
}