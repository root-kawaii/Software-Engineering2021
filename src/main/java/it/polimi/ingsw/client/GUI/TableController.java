package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * JavaFX controller for the table screen.
 */
public class TableController {
    private Display display;
    private GUI gui;
    @FXML
    private Button buttonViewPlayer1;
    @FXML
    private Button buttonViewPlayer2;
    @FXML
    private Button buttonViewPlayer3;
    @FXML
    private Button buttonViewPlayer4;
    @FXML
    private ImageView card1;
    @FXML
    private ImageView card2;
    @FXML
    private ImageView card3;
    @FXML
    private ImageView card4;
    @FXML
    private ImageView card5;
    @FXML
    private ImageView card6;
    @FXML
    private ImageView card7;
    @FXML
    private ImageView card8;
    @FXML
    private ImageView card9;
    @FXML
    private ImageView card10;
    @FXML
    private ImageView card11;
    @FXML
    private ImageView card12;
    @FXML
    private ImageView marble1;
    @FXML
    private ImageView marble2;
    @FXML
    private ImageView marble3;
    @FXML
    private ImageView marble4;
    @FXML
    private ImageView marble5;
    @FXML
    private ImageView marble6;
    @FXML
    private ImageView marble7;
    @FXML
    private ImageView marble8;
    @FXML
    private ImageView marble9;
    @FXML
    private ImageView marble10;
    @FXML
    private ImageView marble11;
    @FXML
    private ImageView marble12;
    @FXML
    private ImageView marbleExtra;
    @FXML
    private Button buttonInsert1;
    @FXML
    private Button buttonInsert2;
    @FXML
    private Button buttonInsert3;
    @FXML
    private Button buttonInsert4;
    @FXML
    private Button buttonInsert5;
    @FXML
    private Button buttonInsert6;
    @FXML
    private Button buttonInsert7;
    private ArrayList<Integer> clientIDs = new ArrayList<>();
    private HashMap<String, Image> marbleImages = new HashMap<>();
    private ImageView[] cards;
    private ImageView[] marbles;
    private Button[] insertButtons;

    @FXML
    private void initialize() {
        cards = new ImageView[] {card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12};
        marbles = new ImageView[] {marble1, marble2, marble3, marble4, marble5, marble6, marble7, marble8, marble9, marble10, marble11, marble12, marbleExtra};
        insertButtons = new Button[] {buttonInsert1, buttonInsert2, buttonInsert3, buttonInsert4, buttonInsert5, buttonInsert6, buttonInsert7};
        marbleImages.put("BLUE", new Image("graphics/images/icons/Blue.png"));
        marbleImages.put("GREY", new Image("graphics/images/icons/Grey.png"));
        marbleImages.put("PURPLE", new Image("graphics/images/icons/Purple.png"));
        marbleImages.put("RED", new Image("graphics/images/icons/Red.png"));
        marbleImages.put("WHITE", new Image("graphics/images/icons/White.png"));
        marbleImages.put("YELLOW", new Image("graphics/images/icons/Yellow.png"));
    }

    /**
     * Sets up the controller based on the provided parameters.
     * @param display The Display that will show this controller's scene
     * @param gui The GUI the program is using
     */
    public void setup(Display display, GUI gui) {
        this.display = display;
        this.gui = gui;
        if (gui.getBoards().size() < 4) {
            buttonViewPlayer4.setDisable(true);
            buttonViewPlayer4.setVisible(false);
        }
        if (gui.getBoards().size() < 3) {
            buttonViewPlayer3.setDisable(true);
            buttonViewPlayer3.setVisible(false);
        }
        if (gui.getBoards().size() < 2) {
            buttonViewPlayer2.setDisable(true);
            buttonViewPlayer2.setVisible(false);
        }
        for (Integer clientID : gui.getBoards().keySet()) {
            clientIDs.add(clientID);
        }
    }

    @FXML
    private void buy1() {
        buy(1);
    }

    @FXML
    private void buy2() {
        buy(2);
    }

    @FXML
    private void buy3() {
        buy(3);
    }

    @FXML
    private void buy4() {
        buy(4);
    }

    @FXML
    private void buy5() {
        buy(5);
    }

    @FXML
    private void buy6() {
        buy(6);
    }

    @FXML
    private void buy7() {
        buy(7);
    }

    @FXML
    private void buy8() {
        buy(8);
    }

    @FXML
    private void buy9() {
        buy(9);
    }

    @FXML
    private void buy10() {
        buy(10);
    }

    @FXML
    private void buy11() {
        buy(11);
    }

    @FXML
    private void buy12() {
        buy(12);
    }

    @FXML
    private void insert1() {
        insert(0);
    }

    @FXML
    private void insert2() {
        insert(1);
    }

    @FXML
    private void insert3() {
        insert(2);
    }

    @FXML
    private void insert4() {
        insert(3);
    }

    @FXML
    private void insert5() {
        insert(4);
    }

    @FXML
    private void insert6() {
        insert(5);
    }

    @FXML
    private void insert7() {
        insert(6);
    }

    private void insert(int index) {
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                gui.disableMoves();
                Command command = new Command(gui.getClientID(), Messages.INSERTMARBLE, Integer.toString(index));
                gui.sendMessage(command);
                return null;
            }
        }).start();
    }

    private void buy(int index) {
        gui.disableMoves();
        gui.buyCard(index);
    }

    @FXML
    private void viewPlayer1() {
        view(clientIDs.get(0));
    }

    @FXML
    private void viewPlayer2() {
        view(clientIDs.get(1));
    }

    @FXML
    private void viewPlayer3() {
        view(clientIDs.get(2));
    }

    @FXML
    private void viewPlayer4() {
        view(clientIDs.get(3));
    }

    private void view(int clientID) {
        display.switchView(clientID);
    }

    /**
     * Updates the market with the correct marbles.
     * @param colours The colours of every marble, thirteenth colour is the extra one
     */
    public void setMarbleImages(String[] colours) {
        for (int i = 0; i < 13; i++) {
            marbles[i].setImage(marbleImages.get(colours[i]));
        }
    }

    /**
     * Sets the correct cards available for purchase.
     * @param cardIDS
     */
    public void setDecks(String[] cardIDS) {
        for (int i = 0; i < 12; i++) {
            cards[i].setImage(new Image(String.format("graphics/images/cards/%s.png", cardIDS[i])));
        }
    }

    /**
     * Sets a specific card available for purchase.
     * @param index Index of the card on the table
     * @param cardID ID of the card
     */
    public void setDeck(int index, int cardID) {
        cards[index].setImage(new Image(String.format("graphics/images/cards/%d.png", cardID)));
    }

    /**
     * Disables buttons to prevent the player from taking actions.
     * Changing view is still possible.
     */
    public void disableMoves() {
        for (ImageView imageView : cards) {
            imageView.setDisable(true);
        }
        for (Button button : insertButtons) {
            button.setDisable(true);
        }
    }

    /**
     * Enables the player to make a move.
     */
    public void enableMoves() {
        for (ImageView imageView : cards) {
            imageView.setDisable(false);
        }
        for (Button button : insertButtons) {
            button.setDisable(false);
        }
    }
}
