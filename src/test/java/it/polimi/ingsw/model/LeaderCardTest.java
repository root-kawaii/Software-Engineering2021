package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LeaderCardTest {
    @Test
    @DisplayName("new leaderCard attributes")

    public void createLeaderCard()
    {
        //requirements
        EnumMap<Resource,Integer> resource = new EnumMap<>(Resource.class);
        EnumMap<Resource,Integer> resource2 = new EnumMap<>(Resource.class);
        LeaderRequirement one = new LeaderRequirement(3,CardColour.BLUE,resource);
        LeaderRequirement two = new LeaderRequirement(2,CardColour.YELLOW,resource2);
        //Bonus
        MarbleColour testColour = MarbleColour.GREY;
        Map<Resource,Integer> testStorage = new HashMap<Resource,Integer>();
        Map<Resource,Integer> testDiscount = new HashMap<Resource,Integer>();
        Map<Resource,Integer> buyCost = new HashMap<>();
        EnumMap<Resource,Integer> req = new EnumMap<>(Resource.class);
        EnumMap<Resource,Integer> prod = new EnumMap<>(Resource.class);
        Production testProduction = new Production(req,prod);
        Bonus bonus = new Bonus(testColour);

        LeaderCard test = new LeaderCard(3,one,two,bonus, 0);


        assertTrue( test.getReq1()==one );
        assertTrue( test.getReq2()==two );
        assertTrue( test.getVictoryPoints()==3);
        assertTrue( test.getBonus()==bonus);
    }
}
