package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FaithTileTest {

    @Test
    @DisplayName("new Faith tile attributes")

    public void createFaithTile()
    {
        FaithTile one = new FaithTile(2, TileType.POPE,3);
        assertTrue(one.getPosition()==2);
        assertTrue(one.getType()== TileType.POPE);
        assertTrue(one.getZone()==3);
    }


}
