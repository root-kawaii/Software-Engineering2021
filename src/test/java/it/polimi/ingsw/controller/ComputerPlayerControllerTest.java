package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Table;
import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

public class ComputerPlayerControllerTest {
    private static ComputerPlayerController computerPlayerController;
    private static Lobby lobby;
    private static Table table;

    @BeforeAll
    public static void init() {
        lobby = new Lobby(0,null);
        table = new Match(false, lobby).getTable();
        computerPlayerController = new ComputerPlayerController(lobby, table);
    }

    @RepeatedTest(50)
    @DisplayName("testing the turn of Lorenzo")
    public void turnTest() { // let the ComputerPlayer play a bunch of turns to make sure every type of move is tested
        computerPlayerController.addFaithPoints(1);
        computerPlayerController.computerTurn();
    }
}
