package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DevelopmentTest {
    @Test
    @DisplayName("new developmentCard attributes")
    public void createDevelopmentCard()
    {
        //one is empty
        EnumMap<Resource,Integer> one = new EnumMap<>(Resource.class);
        EnumMap<Resource,Integer> two = new EnumMap<>(Resource.class);
        EnumMap<Resource,Integer> three = new EnumMap<>(Resource.class);
        Production production = new Production(two,three);

        DevelopmentCard developmentCard = new DevelopmentCard(1, CardColour.BLUE, 2, one, production, 0);
        assertTrue(developmentCard.getColour()==CardColour.BLUE);
        assertTrue(developmentCard.getVictoryPoints()==2);
        assertTrue(developmentCard.getLevel()==1);
        //assertTrue(developmentCard.getCost().equals(null));//not sure about this
    }
}
