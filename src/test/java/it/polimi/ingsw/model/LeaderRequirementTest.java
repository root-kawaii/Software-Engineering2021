package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LeaderRequirementTest {

    LeaderRequirement req;
    EnumMap<Resource,Integer> res;
    @BeforeEach
    public void createReq(){
        res = new EnumMap<Resource, Integer>(Resource.class);
        res.put(Resource.COIN,1);
        res.put(Resource.STONE,2);
        req = new LeaderRequirement(1,CardColour.PURPLE,res);
    }
    @Test
    @DisplayName("returns")
    public void check(){
        assertEquals(1,req.getLevel());
        assertEquals(CardColour.PURPLE,req.getColour());
        assertEquals(res,req.getResource());
    }
}
