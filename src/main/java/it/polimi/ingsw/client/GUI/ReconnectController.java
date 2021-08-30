package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * JavaFX controller for the reconnect screen.
 */
public class ReconnectController {
    private GUI gui;
    private String username;
    @FXML
    private Label labelReconnect;
    @FXML
    private Button buttonOK;
    @FXML
    private Button buttonKO;

    /**
     * Sets the reconnection text.
     * @param username Username of the player to reconnect as
     */
    public void setLabelReconnect(String username) {
        labelReconnect.setText("Would you like to reconnect as " + username + "?");
        this.username = username;
    }

    @FXML
    private void connectOK() {
        buttonOK.setDisable(true);
        buttonKO.setDisable(true);
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                gui.setUsername(username, true);
                Command command = new Command(gui.getClientID(), Messages.OKRECONNECT, null);
                gui.sendMessage(command);
                gui.wake();
                return null;
            }
        }).start();
    }

    @FXML
    private void connectKO() {
        buttonOK.setDisable(true);
        buttonKO.setDisable(true);
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Command command = new Command(gui.getClientID(), Messages.KORECONNECT, null);
                gui.sendMessage(command);
                gui.wake();
                return null;
            }
        }).start();
    }

    /**
     * Sets the GUI
     * @param gui The GUI
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
