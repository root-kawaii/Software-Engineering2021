
package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Marker;
import it.polimi.ingsw.server.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.EmptyStackException;
import java.util.Stack;

public class ComputerPlayerTest {

    ComputerPlayer computerPlayer;
    Lobby lobby;
    Table table;
    Stack<Marker> markers;
    @BeforeEach
    @DisplayName("init")
    public void init() {
        lobby = new Lobby(0,null);
        computerPlayer = new ComputerPlayer(lobby);
    }


    @Test
    @DisplayName("tests getter")
    public void testAttributes() {
        assertEquals("Lorenzo il Magnifico", computerPlayer.getUsername());
        assertEquals(0, computerPlayer.getFaithPoints());
        assertEquals(7, computerPlayer.getMarkers().size());
    }

    @Test
    @DisplayName("check too many Pops")
    public void tooManyPops() {
        for (int i = 0; i < 7; i++) {
            computerPlayer.popMarker();
        }
        assertThrows(EmptyStackException.class, computerPlayer::popMarker);
    }

    @Test
    @DisplayName("Create new markers.")
    public void testNewMarkers() {
        Marker popped = computerPlayer.popMarker();
        assertEquals(6, computerPlayer.getMarkers().size());
        computerPlayer.newMarkers();
        assertEquals(7, computerPlayer.getMarkers().size());
        for (int i = 0; i < 7; i++) {
            computerPlayer.popMarker();
        }
        assertEquals(0, computerPlayer.getMarkers().size());
    }

    @Test
    public void addPoints() {
        computerPlayer.addFaithPoints(4);
        assertEquals(4, computerPlayer.getFaithPoints());
    }
}



