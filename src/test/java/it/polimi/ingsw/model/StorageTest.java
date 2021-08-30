package it.polimi.ingsw.model;
import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.model.Resource.*;
import static org.junit.jupiter.api.Assertions.*;


public class StorageTest {
    Storage storage;
    Lobby lobby;
    int clientId;
    @BeforeEach
    public void Setup(){
        clientId = 0;
        lobby = new Lobby(0,null);
        storage = new Storage(lobby,clientId);
    }


    @Test
    @DisplayName("should add and remove resources correctly")
    public void ShouldAddAndRemoveResource() {
        storage.addResource(COIN, 1);
        assertEquals(1, storage.getShelvesSize());
        assertEquals(1, storage.getResource(COIN));
        storage.addResource(SERVANT, 1);
        assertEquals(2, storage.getShelvesSize());
        assertEquals(1, storage.getResource(SERVANT));

        storage.addInStrongBox(COIN, 3);
        assertEquals(1, storage.getStrongSize());
        assertEquals(3, storage.getStrong(COIN));
        try {
            storage.removeStorage(SERVANT, 1);
        } catch (Exception e) {
            System.err.println("errore");
        }
        assertEquals(1,storage.getShelvesSize());
    }



    @Test
    @DisplayName("should throw exception when trying to exceed the max number of resource allowed in a shelf")
    public void shouldThrowException(){
        assertThrows(IllegalArgumentException.class, () ->{
            storage.addResource(COIN,4);
        });
        storage.addResource(SERVANT,3);
        assertThrows(IllegalArgumentException.class, () -> {
            storage.addResource(COIN,3);
        });

        assertThrows(IllegalArgumentException.class, () ->{
            storage.addResource(SERVANT,1);
        });
    }
    @Test
    @DisplayName("should throw exception when trying to remove non existent resource from shelves and strongbox")
    public void shouldNotRemove(){
        storage.addResource(COIN,1);
        assertThrows(Exception.class, () -> {
            storage.removeStorage(SERVANT,1);
        });

        storage.addInStrongBox(COIN, 3);
        assertThrows(Exception.class, () -> {
            storage.removeBox(SERVANT,1);
        });
    }

    @Test
    @DisplayName("should throw exception when trying to remove more then we have stored")
    public void tooFew(){
        storage.addResource(SHIELD,2);
        assertThrows(Exception.class, () -> {
            storage.removeStorage(SHIELD,3);
        });
    }
    @Test
    @DisplayName("Check 2,2")
    public void checkTwoTwo(){
        storage.addResource(COIN,2);
        storage.addResource(SERVANT,2);
        storage.addResource(STONE,1);

        assertThrows(Exception.class,() ->{
            storage.addResource(STONE,1);
        });
    }
    @Test
    @DisplayName("Testing extras")
    public void checkExtra(){
        storage.createExtra(COIN);
        storage.createExtra(SERVANT);
        try {
            storage.addInExtra(COIN, 2);
        }
        catch(Exception e){
            System.err.println("error1");
        }

        try {
            storage.addInExtra(SERVANT, 1);
        }
        catch(Exception e){
            System.err.println("error2");
        }

        assertThrows(Exception.class,() -> {
            storage.addInExtra(SERVANT,2);
        });
        assertThrows(Exception.class,() -> {
            storage.addInExtra(SHIELD,1);
        });



    }


}
