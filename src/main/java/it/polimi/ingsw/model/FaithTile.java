package it.polimi.ingsw.model;

public class FaithTile {

    private int position;
    private TileType type;
    private int VaticanZone;

    /**
     * Constructor of the class
     * @param position the position of the tile in the faith track
     * @param type the type of the tile(blank, points, pope)
     * @param vaticanZone the zone which the tile is part of
     */
    public FaithTile(int position,TileType type,int vaticanZone){
        this.position=position;
        this.type=type;
        this.VaticanZone=vaticanZone;
    }

    /**
     * returns the postiion
     * @return position
     */
    public int getPosition(){
        return position;
}

    /**
     * returns the type
     * @return type
     */
    public TileType getType(){
        return type;
}

    /**
     *
     * @return returns vatican zone
     */
    public int getZone(){
        return VaticanZone;
}


}
