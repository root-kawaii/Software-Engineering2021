package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.model.Resource.COIN;
import static it.polimi.ingsw.model.Resource.SHIELD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerBoardTest {

    PlayerBoard board;
    EnumMap<Resource,Integer> cost = new EnumMap(Resource.class);
    EnumMap<Resource,Integer> req = new EnumMap(Resource.class);
    EnumMap<Resource,Integer> prod = new EnumMap(Resource.class);
    Production production;
    DevelopmentCard card;
    EnumMap<Resource,Integer> cost2 = new EnumMap(Resource.class);
    EnumMap<Resource,Integer> req2 = new EnumMap(Resource.class);
    EnumMap<Resource,Integer> prod2 = new EnumMap(Resource.class);
    Production production2;
    DevelopmentCard card2,card12,card02,card1,card22;


    @BeforeEach
    @DisplayName("creating all the required resources")
    public void Create(){
        Lobby lobby = new Lobby(121,null);
        Match match = new Match(true,lobby);
        Table table = new Table(lobby,match);
        Player player = new Player("Cugola",12,table,lobby);
        match.addPlayer(player);
        board = player.getPlayerBoard();
        req.put(SHIELD,2);
        prod.put(Resource.COIN,1);
        cost.put(SHIELD,2);
        cost.put(COIN,1);
        production = new Production(req,prod);
        card = new DevelopmentCard(1,CardColour.PURPLE,2,cost,production,123);
        req2.put(SHIELD,2);
        prod2.put(Resource.COIN,1);
        cost2.put(SHIELD,2);
        cost2.put(COIN,1);
        production2 = new Production(req,prod);
        card1 = new DevelopmentCard(1,CardColour.PURPLE,2,cost2,production2,123);
        card2 = new DevelopmentCard(1,CardColour.PURPLE,2,cost2,production2,123);
        card02 = new DevelopmentCard(2,CardColour.PURPLE,2,cost2,production2,123);
        card12 = new DevelopmentCard(2,CardColour.PURPLE,2,cost2,production2,123);
        card22 = new DevelopmentCard(2,CardColour.PURPLE,2,cost2,production2,123);


    }

    @Test
    @DisplayName("adding various cards to the board and retrieving them")
    public void addAndGetCard(){
        DevelopmentCard cardq,cardz;
        board.addCard(card,0);
        board.addCard(card1,1);
        board.addCard(card2,2);
        board.addCard(card02,0);
        board.addCard(card12,1);
        board.addCard(card22,2);
        cardq = board.getCard(0,0);
        cardz = board.getCard(1,0);
        card12 = board.getCard(2,0);
        assertEquals(board.getCol0().get(1),card02);
        board.getCol1();
        board.getCol2();
        board.getCol(1);
        board.getCol(2);
        board.getCol(0);

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
        board.addCard(test,2);
        board.getLeader(0);
    }
}
