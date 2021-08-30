package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;

import static it.polimi.ingsw.model.Resource.COIN;
import static it.polimi.ingsw.model.Resource.STONE;
import static org.junit.jupiter.api.Assertions.*;

public class BoardControllerTest {

    MainController mainController;
    ArrayList<Player> players = new ArrayList<Player>();
    Lobby lobby;
    Match match;

    @BeforeEach
    public void init() {
        lobby = new Lobby(0,null);
        match = new Match(true, lobby);
        mainController = new MainController(match,lobby);
    }

    @Test
    @DisplayName("testing new boardController")
    public void getPlayerController() {
        mainController.addPlayer("root-kawaii",0);
        BoardController test = new BoardController(match,lobby);
        assertFalse(match.getPlayers().get(0).getFaithPoints()==3);//addFaith check
        assertFalse(test.checkPope(match.getPlayers().get(0)));
    }

    @Test
    @DisplayName("testing the activation of production")
    public void activateProduction(){
        mainController.addPlayer("root-kawaii",0);
        EnumMap<Resource,Integer> req = new EnumMap<>(Resource.class);
        req.put(COIN,1);
        EnumMap<Resource,Integer> prod = new EnumMap<>(Resource.class);
        prod.put(STONE,1);
        Production production = new Production(req,prod);
        Bonus test = new Bonus(production);
        match.getPlayer(0).getBonus().add(test);
        BoardController test2 = new BoardController(match,lobby);
        EnumMap<Resource,Integer> fStorage= new EnumMap<>(Resource.class);
        fStorage.put(COIN,1);
        EnumMap<Resource,Integer> fStrong= new EnumMap<>(Resource.class);
        fStrong.put(COIN,0);
        match.getActivePlayer().getStorage().addResource(COIN,1);
        match.getActivePlayer().getStorage().addInStrongBox(COIN,1);
        test2.activateProduction(production,match.getPlayers().get(match.getPlayerTurn()),fStorage,fStrong,null);
        //assertTrue(false); //have to check this fuction over here so put this to make test fail
        EnumMap<Resource,Integer> fStorage2= new EnumMap<>(Resource.class);
        match.getActivePlayer().getStorage().addResource(COIN,1);
        test2.activateProduction(test.getProduction(),match.getActivePlayer(),fStorage,fStrong,STONE);
        match.getActivePlayer().getStorage().addInStrongBox(COIN,1);
        test2.activateProduction(test.getProduction(),match.getActivePlayer(),fStorage,fStrong,COIN);
        fStrong.put(COIN,1);
        match.getActivePlayer().getStorage().addResource(COIN,1);
        test2.activateBaseProduction(fStorage,fStrong,COIN);
        String[] neww = {"COIN","STONE"};
        try{
            test2.chooseResource(0,neww);
        }catch(Exception e){
        }
        Bonus newBonus = new Bonus("COIN");
        Bonus newBonus2 = new Bonus("STONE");
        match.getPlayer(0).getBonus().add(newBonus);
        match.getPlayer(0).getBonus().add(newBonus2);
        try{
            match.getActivePlayer().getStorage().addInExtra(COIN,1);
            match.getActivePlayer().getStorage().addInExtra(STONE,1);
        }catch (Exception e){

        }
        test2.activateProduction(production, match.getActivePlayer(), fStorage,fStrong,COIN);
    }
}
