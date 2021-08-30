package it.polimi.ingsw.model;

import static it.polimi.ingsw.model.TileType.*;

public class FaithTrack {
    private FaithTile[] tiles = new FaithTile[25];

    /**
     * Constructor for the class, creates the track and all tiles.
     * if the tile is in position 8,16,24 it's POPE tile, if the position is 4,9,10,11,17,18 it's a BLANK tile,else it's a POINT tyle
     */
    public FaithTrack() {
        for (int i = 0; i < 25; i++) {
            if (i == 8 || i == 16 || i == 24) {
                tiles[i] = new FaithTile(i, POPE, 2);
            } else if (i <= 4 || i == 9 || i == 10 || i == 11 || i == 17 || i == 18) {
                tiles[i] = new FaithTile(i, BLANK, 0);
            } else
                tiles[i] = new FaithTile(i, POINTS, 1);
        }

    }

    /**
     *
     * @param pos position of the tile in track
     * @return returns the tile in that position
     */
    public FaithTile getTile(int pos){
        return tiles[pos];
    }

}
