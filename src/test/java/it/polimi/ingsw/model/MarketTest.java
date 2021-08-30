package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.model.MarbleColour.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MarketTest {
    Market market;
    Match match;
    Lobby lobby;

    @BeforeEach
    public void init(){
        lobby = new Lobby(0,null);
        match = new Match(false, lobby);
        market = match.getMarket();
        Player player = new Player("Test", 0, match.getTable(), lobby);
        match.addPlayer(player);
    }

    @Test
    @DisplayName("print random matrix then add marble and print again")
    public void print() {
        MarbleColour[][] colour = market.getMarbles();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print("  " + colour[i][j]);
            }
            System.out.println();
        }
        System.out.println("----------------------");
        try {
             market.insertMarble(1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        MarbleColour[][] colour2 = market.getMarbles();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print("  " + colour2[i][j]);
            }
            System.out.println();
        }
        System.out.println("----------------------");
        try {
            market.insertMarble(4);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        MarbleColour[][] colour3 = market.getMarbles();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print("  " + colour3[i][j]);
            }
            System.out.println();
        }
    }
    @Test
    @DisplayName("insert marble should throw exception if x not beetween 0 and 6")
    public void shouldThrowException(){
        assertThrows(IllegalArgumentException.class,() -> {
            market.insertMarble(7);
        });
            assertThrows(IllegalArgumentException.class,() -> {
                market.insertMarble(-1);
            });
    }

    @Test
    @DisplayName("check if resources retured correctly")
    public void checkRet(){
        Map res = new HashMap();
        MarbleColour[][] colour = market.getMarbles();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print("  " + colour[i][j]);
            }
            System.out.println();
        }
        System.out.println("----------------------");

        try {
           res = market.insertMarble(1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        res.forEach((key, value) -> System.out.println(key + ":" + value));
    }


    }



