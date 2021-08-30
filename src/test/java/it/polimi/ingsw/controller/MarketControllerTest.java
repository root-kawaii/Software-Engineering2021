package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.*;
import static it.polimi.ingsw.model.CardColour.*;
import static it.polimi.ingsw.model.Resource.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


public class MarketControllerTest {
    MarketController marketController;
    Match match;
    Lobby lobby;
    ArrayList<Player> players = new ArrayList<>();
    Player player;
    @BeforeEach
    @DisplayName("create market controller")
    public void create(){
        lobby = new Lobby(0,null);
        match = new Match(true, lobby);
        this.marketController = new MarketController(match,lobby, new MainController(match, lobby));
        player = new Player("Test", 0, match.getTable(), lobby);
        match.addPlayer(player);
    }
    @Test
    @DisplayName("check insert marble and return value")
    public void insertMarble(){
        int i;
        Map<MarbleColour,Integer> gain;
        for(i=0;i<200;i++){
            marketController.insertMarble(i%7,null);
        }

    }
    @Test
    @DisplayName("buy card should catch exception and print failed")
    public void buyCard(){
        Table table = new Table(lobby, match);
        EnumMap<Resource,Integer> req = new EnumMap<>(Resource.class);
        EnumMap<Resource,Integer> prod = new EnumMap<>(Resource.class);
        Production production = new Production(req,prod);
        player.getStorage().addResource(COIN,1);
        player.getStorage().addInStrongBox(COIN,1);
        EnumMap<Resource,Integer> cost= new EnumMap<>(Resource.class);
        cost.put(COIN,2);
        Map<Resource,Integer> fStorage = new HashMap<>();
        fStorage.put(COIN,1);
        Map<Resource,Integer> fStrong = new HashMap<>();
        fStrong.put(COIN,1);
        DevelopmentCard card = new DevelopmentCard(1,PURPLE,3,cost,production,1222);
        this.marketController.buyCard(card,fStorage,fStrong, player.getClientID(),12);
        this.marketController.buyCard(card,fStorage,fStrong, player.getClientID(),2);
        try{
            player.getStorage().removeStorage(COIN,1);
        }catch (Exception e){

        }
        this.marketController.buyCard(card,fStorage,fStrong, player.getClientID(),2);

    }


}
