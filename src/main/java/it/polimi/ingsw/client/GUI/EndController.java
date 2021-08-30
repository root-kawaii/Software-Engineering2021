package it.polimi.ingsw.client.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * JavaFX controller for the game end screen.
 */
public class EndController {
    @FXML
    private Button exitButton;
    @FXML
    private Label winner;

    @FXML
    private void exit() {
        System.exit(0);
    }

    /**
     * Sets the screen to show
     * @param winner
     */
    public void setWinner(String winner) {
        this.winner.setText(winner + " Won!");
    }
}
