package it.polimi.ingsw.model;
import static it.polimi.ingsw.model.TileType.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FaithTrackTest{

    FaithTrack track;
    int id = 1;
    @BeforeEach
    @DisplayName("should create faithTrack")
    void createFaithTrack()
    {
        track = new FaithTrack();
    }
    @Test
    @DisplayName("Should return Tile Type and vaticanZone correctly")
    void typeCheck(){
        FaithTile tile1,tile2,tile3;

        tile1 = track.getTile(8);
        tile2 = track.getTile(1);
        tile3 = track.getTile(6);

        assertEquals(POPE,tile1.getType());
        assertEquals(2,tile1.getZone());

        assertEquals(BLANK,tile2.getType());
        assertEquals(0, tile2.getZone());

        assertEquals(POINTS,tile3.getType());
        assertEquals(1,tile3.getZone());
    }



}
