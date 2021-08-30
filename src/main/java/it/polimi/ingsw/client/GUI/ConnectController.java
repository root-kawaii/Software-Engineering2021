package it.polimi.ingsw.client.GUI;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * JavaFX Controller for the connection screen.
 */
public class ConnectController {
    private Display display;
    private Thread gui;
    @FXML
    private TextField textFieldAddress;
    @FXML
    private TextField textFieldPort;
    @FXML
    private Button buttonConnect;
    @FXML
    private Label labelConnect;

    @FXML
    private void buttonConnectAction() {
        buttonConnect.setDisable(true);
        labelConnect.setText("Connecting...");
        labelConnect.setVisible(true);
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    gui = new Thread(new GUI(textFieldAddress.getText(), Integer.parseInt(textFieldPort.getText()), display));
                    gui.start();
                    Platform.runLater(() -> {
                        labelConnect.setText("Waiting for lobby to be set up.");
                    });
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        labelConnect.setText("Couldn't connect.");
                        buttonConnect.setDisable(false);
                    });
                }
                return null;
            }
        }).start();
    }

    /**
     * Sets the Display.
     * @param display The Display
     * @see Display
     */
    public void setDisplay(Display display) {
        this.display = display;
    }
}
