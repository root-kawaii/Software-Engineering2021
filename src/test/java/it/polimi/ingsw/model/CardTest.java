package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardTest {
    @Test
    @DisplayName("new Card attributes")
    public void createCard()
    {
        int x=3;
        EnumMap<Resource,Integer> one= new EnumMap<>(Resource.class);
        EnumMap<Resource,Integer> two= new EnumMap<>(Resource.class);
        Production testo = new Production(one,two);
        EnumMap<Resource,Integer> three = new EnumMap<>(Resource.class);
        DevelopmentCard test = new DevelopmentCard(x, CardColour.BLUE, 3, three, testo, 0);
        assertTrue( test.getVictoryPoints()==3);

    }
}
