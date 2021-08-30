package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
    Lobby lobby;
    Match match;
    Table table;

    @BeforeEach
    public void init() {
        lobby = new Lobby(0,null);
        match = new Match(false, lobby);
        table = match.getTable();
    }

    @Test
    @DisplayName("testing adding faith,victory points and other attributes")
    public void testAttributes() {
        Player player = new Player("NomeInteressante", 1, table, lobby);
        Player player2 = new Player("NomeNoNInteressante", 2, table, lobby);
        Player player3 = new Player("NomeQuasiInteressante", 3, table, lobby);
        Player player4 = new Player("NomeForseInteressante", 4, table, lobby);
        match.addPlayer(player);
        match.addPlayer(player2);
        match.addPlayer(player3);
        match.addPlayer(player4);
        player2.addFaithPoints(6);
        player.addFaithPoints(8);
        assertEquals(player.getUsername(), "NomeInteressante");
        assertEquals(player.getClientID(), 1);
        assertEquals(player.getFaithPoints(), 8);
        assertEquals(player.getVictoryPoints(), 5);//5 because of vatican zones
    }

    @Test
    @DisplayName("testing the adding of the leader")
    public void  testAddLeader() {
        LeaderCard leaderCard = new LeaderCard(1, null, null, 1);
        Player player = new Player("Test", 1, table, lobby);
        player.addLeader(leaderCard);
        assertEquals(player.getLeader(0), leaderCard);
    }
}



