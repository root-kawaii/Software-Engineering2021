package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.model.Resource.*;
import static it.polimi.ingsw.model.CardColour.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductionTest {


    EnumMap <Resource,Integer>req  = new EnumMap<Resource, Integer>(Resource.class);
    EnumMap <Resource,Integer>prod = new EnumMap<>(Resource.class);

    Production production;

    @BeforeEach
    public void init(){

        req.put(SHIELD,2);
        prod.put(Resource.COIN,1);
        production = new Production(req,prod);
    }

    @Test
    @DisplayName("should return requirements and buy cost, plus checks if value is right")
    public void retReq(){
        Map <Resource,Integer> check;
        Map <Resource,Integer> check2;
        check = production.getRequirements();
        assertEquals(req.get(SHIELD),check.get(SHIELD));

    }

    @Test
    @DisplayName("should return products and checks if value is right")
    public void retProd(){
        Map <Resource,Integer> check;
        check = production.getProducts();

        assertEquals(prod.get(COIN),check.get(COIN));
    }




}
