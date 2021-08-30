package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchTest {
    private Match match;
    private Lobby lobby;

    @BeforeEach
    public void init() {
        lobby = new Lobby(0,null);
        match = new Match(true, lobby);
    }

    @Test
    @DisplayName("creating match")
    public void createMatch()
    {
        Player one = new Player("red-debu",0, match.getTable(),lobby);
        Player two = new Player("dakkay",1, match.getTable(),lobby);
        match.addPlayer(one);
        match.addPlayer(two);
        assertEquals(match.getPlayer(0), one);
        assertEquals(match.getPlayer(1), two);
        assertEquals(0, match.getPlayerTurn());
        match.nextTurn();
        assertEquals(1, match.getPlayerTurn());
        assertEquals(2, match.getSize());

    }
}
