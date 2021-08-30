package it.polimi.ingsw.model;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BonusTest {
    @Test
    @DisplayName("new bonus attributes test")
    public void createBonus()
    {
        MarbleColour testColour = MarbleColour.GREY;
        String testStorage = "COIN";
        Resource testDiscount = Resource.COIN;
        Map<Resource,Integer> buyCost = new HashMap<>();
        EnumMap<Resource,Integer> req = new EnumMap<>(Resource.class);
        EnumMap<Resource,Integer> prod = new EnumMap<>(Resource.class);
        Production testProduction = new Production(req,prod);

        Bonus test = new Bonus(testColour);
        Bonus disc = new Bonus((testDiscount));
        Bonus stor = new Bonus(testStorage);
        Bonus produ = new Bonus(testProduction);
        assertTrue( test.getMarble().equals(testColour) );
        assertTrue( disc.getDiscount().equals(testDiscount) );
        assertTrue( stor.getStorage().equals(testStorage) );
        assertTrue( produ.getProduction().equals(testProduction) );

    }
}
