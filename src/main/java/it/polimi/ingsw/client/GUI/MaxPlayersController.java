package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * JavaFX controller for the player number choice screen.
 */
public class MaxPlayersController {
    GUI gui;
    @FXML
    Button button1;
    @FXML
    Button button2;
    @FXML
    Button button3;
    @FXML
    Button button4;

    @FXML
    private void button1Action() {
        disableButtons();
        send(1);
    }

    @FXML
    private void button2Action() {
        disableButtons();
        send(2);
    }

    @FXML
    private void button3Action() {
        disableButtons();
        send(3);
    }

    @FXML
    private void button4Action() {
        disableButtons();
        send(4);

    }

    @FXML
    private void disableButtons() {
        button1.setDisable(true);
        button2.setDisable(true);
        button3.setDisable(true);
        button4.setDisable(true);
    }

    @FXML
    private void send(int players) {
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Command command = new Command(gui.getClientID(), Messages.SENDMAXPLAYERS, Integer.toString(players));
                gui.sendMessage(command);
                gui.wake();
                return null;
            }
        }).start();
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
