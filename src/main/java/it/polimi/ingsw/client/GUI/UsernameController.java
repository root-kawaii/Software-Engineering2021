package it.polimi.ingsw.client.GUI;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * JavaFX controller for the username choice screen.
 */
public class UsernameController {
    private GUI gui;
    @FXML
    TextField textFieldUsername;
    @FXML
    Button buttonChoose;
    @FXML
    Label labelWaiting;

    @FXML
    private void buttonChooseAction() {
        buttonChoose.setDisable(true);
        textFieldUsername.setDisable(true);
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                gui.setUsername(textFieldUsername.getText());
                Platform.runLater(() -> {
                    labelWaiting.setText("Waiting for everyone");
                    labelWaiting.setVisible(true);
                });
                gui.wake();
                return null;
            }
        }).start();
    }

    /**
     * Shows the player the username they picked wasn't valid.
     */
    public void invalid() {
        labelWaiting.setText("Username already taken");
        buttonChoose.setDisable(false);
        textFieldUsername.setDisable(false);
    }

    /**
     * Sets the GUI.
     * @param gui The GUI
     * @see GUI
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
