package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TableTest {
    Table table;
    Match match;

    @BeforeEach
    //initialization
    public void test(){
        Lobby lobby = new Lobby(0,null);
        match = new Match(false, lobby);
        table = match.getTable();
    }

    @Test
    public void createTable()
    {
        assertTrue( true );
    }

    @Test
    @DisplayName("should return card")
    public void getCard(){
       DevelopmentCard card = table.getCard(1);
        System.out.println("vPoint" +card.getVictoryPoints());
    }
    @Test
    @DisplayName("should remove card")
    public void removeCard(){
        DevelopmentCard card = table.getCard(1);
        table.removeCard(card,0);
        DevelopmentCard card1 = table.getCard(1);
        assertNotEquals(card,card1);
        assertEquals(card.getLevel(),card1.getLevel());



    }


}
