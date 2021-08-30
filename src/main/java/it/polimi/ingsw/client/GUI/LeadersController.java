package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * JavaFX controller for the leader choice screen.
 */
public class LeadersController {
    private GUI gui;
    private int[] cardIDs;
    private ArrayList<Integer> choices = new ArrayList<>();
    @FXML
    ImageView imageView1;
    @FXML
    ImageView imageView2;
    @FXML
    ImageView imageView3;
    @FXML
    ImageView imageView4;
    @FXML
    Button buttonChoose;

    @FXML
    private void imageView1Clicked() {
        if (!choices.contains(cardIDs[0])) {
            if (choices.size() < 2) {
                choices.add(cardIDs[0]);
                imageView1.setEffect(new DropShadow(BlurType.ONE_PASS_BOX, Color.BLUE, 16, 1, 0, 0));
            }
        } else {
            choices.remove((Integer) cardIDs[0]);
            imageView1.setEffect(null);
        }
        checkButton();
    }

    @FXML
    private void imageView2Clicked() {
        if (!choices.contains(cardIDs[1])) {
            if (choices.size() < 2) {
                choices.add(cardIDs[1]);
                imageView2.setEffect(new DropShadow(BlurType.ONE_PASS_BOX, Color.BLUE, 16, 1, 0, 0));
            }
        } else {
            choices.remove((Integer) cardIDs[1]);
            imageView2.setEffect(null);
        }
        checkButton();
    }

    @FXML
    private void imageView3Clicked() {
        if (!choices.contains(cardIDs[2])) {
            if (choices.size() < 2) {
                choices.add(cardIDs[2]);
                imageView3.setEffect(new DropShadow(BlurType.ONE_PASS_BOX, Color.BLUE, 16, 1, 0, 0));
            }
        } else {
            choices.remove((Integer) cardIDs[2]);
            imageView3.setEffect(null);
        }
        checkButton();
    }

    @FXML
    private void imageView4Clicked() {
        if (!choices.contains(cardIDs[3])) {
            if (choices.size() < 2) {
                choices.add(cardIDs[3]);
                imageView4.setEffect(new DropShadow(BlurType.ONE_PASS_BOX, Color.BLUE, 16, 1, 0, 0));
            }
        } else {
            choices.remove((Integer) cardIDs[3]);
            imageView4.setEffect(null);
        }
        checkButton();
    }

    @FXML
    private void buttonChooseAction() {
        buttonChoose.setDisable(true);
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                gui.getHand()[0] = gui.getCards()[choices.get(0) - 1];
                gui.getHand()[1] = gui.getCards()[choices.get(1) - 1];
                Command command = new Command(gui.getClientID(), Messages.SENDLEADERS, choices.get(0) + " " + choices.get(1));
                gui.sendMessage(command);
                gui.wake();
                return null;
            }
        }).start();
    }

    private void checkButton() {
        if (choices.size() == 2) {
            buttonChoose.setDisable(false);
        } else {
            buttonChoose.setDisable(true);
        }
    }

    /**
     * Sets the GUI.
     * @param gui The GUI
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Sets the card available to choose from.
     * @param cardIDs The IDs of the cards
     */
    public void setCardIDs(int[] cardIDs) {
        this.cardIDs = cardIDs;
        imageView1.setImage(new Image(String.format("graphics/images/cards/%d.png", cardIDs[0])));
        imageView2.setImage(new Image(String.format("graphics/images/cards/%d.png", cardIDs[1])));
        imageView3.setImage(new Image(String.format("graphics/images/cards/%d.png", cardIDs[2])));
        imageView4.setImage(new Image(String.format("graphics/images/cards/%d.png", cardIDs[3])));
    }
}
