package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.shared.Command;
import it.polimi.ingsw.shared.Messages;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaFX controller for the player board screen.
 */
public class PlayerBoardController {
    private Display display;
    private GUI gui;
    private ArrayList<Integer> clientIDs = new ArrayList<>();
    private HashMap<Resource, Image> resourceImages = new HashMap<>();
    private ImageView tempSelected;
    private int buyIndex;
    private EnumMap<Resource, Integer> fromStorage;
    private EnumMap<Resource, Integer> fromStrong;
    private ArrayList<Integer> toggled;
    private int resourceChoicesTotal;
    private ArrayList<Resource> resourceChoices;
    private boolean statusProduction = false;
    private boolean statusResourceChoice = false;
    private  final int[][] tilePositions = new int[][] {
            {0, 100}, {50, 100}, {100, 100}, {100, 50}, {100, 0},
            {150, 0}, {199, 0}, {248, 0}, {298, 0}, {347, 0},
            {347, 50}, {347, 100}, {397, 100}, {446, 100}, {495, 100},
            {544, 100}, {593, 100}, {593, 50}, {593, 0}, {643, 0},
            {692, 0}, {741, 0}, {790, 0}, {840, 0}, {889, 0}
    };
    @FXML
    private Label labelUsername;
    @FXML
    private Button buttonViewTable;
    @FXML
    private Button buttonViewPlayer1;
    @FXML
    private Button buttonViewPlayer2;
    @FXML
    private Button buttonViewPlayer3;
    @FXML
    private Button buttonViewPlayer4;
    @FXML
    private ImageView tilePlayer;
    @FXML
    private ImageView tileCPU;
    @FXML
    private ImageView storage1;
    @FXML
    private ImageView storage2;
    @FXML
    private ImageView storage3;
    @FXML
    private Label storage1Amount;
    @FXML
    private Label storage2Amount;
    @FXML
    private Label storage3Amount;
    @FXML
    private Label strongCoinAmount;
    @FXML
    private Label strongServantAmount;
    @FXML
    private Label strongShieldAmount;
    @FXML
    private Label strongStoneAmount;
    @FXML
    private HBox tempStorage;
    @FXML
    private Button toStorage;
    @FXML
    private Button toExtra;
    @FXML
    private Button buttonEndTurn;
    @FXML
    private Label labelMessage;
    @FXML
    private ImageView leader1;
    @FXML
    private ImageView leader2;
    @FXML
    private ImageView storageExtra1;
    @FXML
    private ImageView storageExtra2;
    @FXML
    private ImageView storageExtra3;
    @FXML
    private ImageView storageExtra4;
    @FXML
    private Label inactive1;
    @FXML
    private Label inactive2;
    @FXML
    private ImageView cardBuying;
    @FXML
    private Label labelBuying;
    @FXML
    private VBox resourceTotals;
    @FXML
    private Label totalsCoin;
    @FXML
    private Label totalsServant;
    @FXML
    private Label totalsShield;
    @FXML
    private Label totalsStone;
    @FXML
    private ImageView strongCoin;
    @FXML
    private ImageView strongServant;
    @FXML
    private ImageView strongShield;
    @FXML
    private ImageView strongStone;
    @FXML
    private Button buyCol1;
    @FXML
    private Button buyCol2;
    @FXML
    private Button buyCol3;
    @FXML
    private Button buttonProduction;
    @FXML
    private ToggleButton toggleProduction0;
    @FXML
    private ToggleButton toggleProduction1;
    @FXML
    private ToggleButton toggleProduction2;
    @FXML
    private ToggleButton toggleProduction3;
    @FXML
    private ToggleButton toggleProduction4;
    @FXML
    private ToggleButton toggleProduction5;
    @FXML
    private Button buttonPlay1;
    @FXML
    private Button buttonPlay2;
    @FXML
    private Button buttonDiscard1;
    @FXML
    private Button buttonDiscard2;
    @FXML
    private AnchorPane col1;
    @FXML
    private AnchorPane col2;
    @FXML
    private AnchorPane col3;

    @FXML
    private void initialize() {
        resourceImages.put(Resource.COIN, new Image("graphics/images/icons/COIN.png"));
        resourceImages.put(Resource.SERVANT, new Image("graphics/images/icons/SERVANT.png"));
        resourceImages.put(Resource.SHIELD, new Image("graphics/images/icons/SHIELD.png"));
        resourceImages.put(Resource.STONE, new Image("graphics/images/icons/STONE.png"));
    }

    /**
     * Sets up the controller based on the provided parameters.
     * @param display The Display that will show this controller's scene
     * @param gui The GUI the program is using
     * @param username The player's username
     * @see Display
     * @see GUI
     */
    public void setup(Display display, GUI gui, String username) {
        this.display = display;
        this.gui = gui;
        this.labelUsername.setText(username);
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
        if (username.equals(gui.getUsername())) {
            leader1.setImage(new Image(String.format("graphics/images/cards/%d.png", gui.getHand()[0].getID())));
            inactive1.setVisible(true);
            leader2.setImage(new Image(String.format("graphics/images/cards/%d.png", gui.getHand()[1].getID())));
            inactive2.setVisible(true);
        }
    }

    @FXML
    private void viewTable() {
        view(0);
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
     * Updates the player's username
     * @param username The new username
     */
    public void setUsername(String username) {
        labelUsername.setText(username);
    }

    /**
     * Moves this player's faith marker.
     * @param points The total amount of points the player has
     */
    public void setFaith(int points) {
        if (points < 25) {
            tilePlayer.setLayoutX(tilePositions[points][0]);
            tilePlayer.setLayoutY(tilePositions[points][1]);
        } else {
            tilePlayer.setLayoutX(tilePositions[24][0]);
            tilePlayer.setLayoutX(tilePositions[24][1]);
        }
    }

    /**
     * Moves the computer player's faith marker.
     * @param points The total amount of points the player has
     */
    public void setCPUFaith(int points) {
        tileCPU.setVisible(true);
        if (points < 25) {
            tileCPU.setLayoutX(tilePositions[points][0]);
            tileCPU.setLayoutY(tilePositions[points][1]);
        } else {
            tileCPU.setLayoutX(tilePositions[24][0]);
            tileCPU.setLayoutX(tilePositions[24][1]);
        }
    }

    /**
     * Updates the player's resources.
     * @param clientID ID of the player to update
     */
    public void updateResources(int clientID) {
        if (gui.getBoard(clientID).getShelves().size() < 3) {
            storage3.setVisible(false);
            storage3Amount.setVisible(false);
        }
        if (gui.getBoard(clientID).getShelves().size() < 2) {
            storage2.setVisible(false);
            storage2Amount.setVisible(false);
        }
        if (gui.getBoard(clientID).getShelves().size() < 1) {
            storage1.setVisible(false);
            storage1Amount.setVisible(false);
        }
        int i = 0;
        for (Map.Entry entry : gui.getBoard(clientID).getShelves().entrySet()) {
            if (i == 0) {
                storage1.setImage(resourceImages.get(entry.getKey()));
                storage1Amount.setText(String.format("%d", entry.getValue()));
                storage1.setVisible(true);
                storage1Amount.setVisible(true);
            } else if (i == 1) {
                storage2.setImage(resourceImages.get(entry.getKey()));
                storage2Amount.setText(String.format("%d", entry.getValue()));
                storage2.setVisible(true);
                storage2Amount.setVisible(true);
            } else if (i == 2) {
                storage3.setImage(resourceImages.get(entry.getKey()));
                storage3Amount.setText(String.format("%d", entry.getValue()));
                storage3.setVisible(true);
                storage3Amount.setVisible(true);
            }
            i++;
        }
    }

    /**
     * Updates the player's Strongbox.
     * @param clientID ID of the player to update
     */
    public void updateStrong(int clientID) {
        strongCoinAmount.setText(gui.getBoard(clientID).getStrongBox().get(Resource.COIN) != null ? gui.getBoard(clientID).getStrongBox().get(Resource.COIN).toString() : "0");
        strongServantAmount.setText(gui.getBoard(clientID).getStrongBox().get(Resource.SERVANT) != null ? gui.getBoard(clientID).getStrongBox().get(Resource.SERVANT).toString() : "0");
        strongShieldAmount.setText(gui.getBoard(clientID).getStrongBox().get(Resource.SHIELD) != null ? gui.getBoard(clientID).getStrongBox().get(Resource.SHIELD).toString() : "0");
        strongStoneAmount.setText(gui.getBoard(clientID).getStrongBox().get(Resource.STONE) != null ? gui.getBoard(clientID).getStrongBox().get(Resource.STONE).toString() : "0");
    }

    /**
     * Updates resources in the extra storage.
     * @param clientID ID of the player to update
     */
    public void updateExtra(int clientID) {
        int i = 0;
        for (Resource resource : gui.getBoard(clientID).getExtra().keySet()) {
            if (i == 0) {
                storageExtra1.setImage(gui.getBoard(clientID).getExtra().get(resource) > 0 ? resourceImages.get(resource) : null);
                storageExtra2.setImage(gui.getBoard(clientID).getExtra().get(resource) > 1 ? resourceImages.get(resource) : null);
            } else if (i == 1) {
                storageExtra3.setImage(gui.getBoard(clientID).getExtra().get(resource) > 0 ? resourceImages.get(resource) : null);
                storageExtra4.setImage(gui.getBoard(clientID).getExtra().get(resource) > 1 ? resourceImages.get(resource) : null);
            }
            i++;
        }
    }

    /**
     * Adds a card to this player's board at the top of the specified column.
     * @param cardID The ID of the card
     * @param column The column number
     */
    public void addCard(int clientID, int cardID, int column) {
        ImageView card = null;
        switch (column) {
            case 0 -> card = (ImageView) col1.getChildren().get(gui.getBoard(clientID).getDevelopmentCards()[0].size() - 1);
            case 1 -> card = (ImageView) col2.getChildren().get(gui.getBoard(clientID).getDevelopmentCards()[1].size() - 1);
            case 2 -> card = (ImageView) col3.getChildren().get(gui.getBoard(clientID).getDevelopmentCards()[2].size() - 1);
        }
        card.setImage(new Image(String.format("graphics/images/cards/%d.png", cardID)));
    }

    /**
     * Adds a leader to this player's board
     * @param cardID ID of the card added
     * @param column Where the card should be put
     */
    public void addLeader(int cardID, int column) {
        if (column == 0) {
            leader1.setImage(new Image(String.format("graphics/images/cards/%d.png", cardID)));
            inactive1.setVisible(false);
        } else {
            leader2.setImage(new Image(String.format("graphics/images/cards/%d.png", cardID)));
            inactive2.setVisible(false);
        }
    }

    /**
     * Enables managing the temporary storage.
     */
    public void enableTemp() {
        labelMessage.setVisible(false);
        toStorage.setDisable(false);
        toExtra.setDisable(false);
        buttonEndTurn.setDisable(false);
        buttonEndTurn.setVisible(true);
    }

    /**
     * Used to prevent the player from taking actions until the server replies.
     */
    public void disableTemp() {
        toStorage.setDisable(true);
        toExtra.setDisable(true);
        buttonEndTurn.setDisable(true);
    }

    /**
     * Updates temporary storage for this player.
     * @param clientID The ID of the player to update
     */
    public void updateTemp(int clientID) {
        tempSelected = null;
        if (tempStorage.getChildren().size() > 2) tempStorage.getChildren().remove(2, tempStorage.getChildren().size());
        if (gui.getBoard(clientID).getTemp().size() == 0) {
            tempStorage.setDisable(true);
            tempStorage.setVisible(false);
        } else {
            tempStorage.setDisable(false);
            tempStorage.setVisible(true);
        }
        for (Resource resource : gui.getBoard(clientID).getTemp()) {
            tempStorage.getChildren().add(makeTempResource(resource));
        }
    }

    @FXML
    private void insertStorage() {
        insertResource(false);
    }

    @FXML
    private void insertExtra() {
        insertResource(true);
    }

    private void insertResource(boolean extra) {
        if (tempSelected != null) {
            disableTemp();
            labelMessage.setVisible(false);
            new Thread(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    for (int i = 2; i < tempStorage.getChildren().size(); i++) {
                        if (tempStorage.getChildren().get(i) == tempSelected) {
                            Command command = new Command(gui.getClientID(), Messages.INSERTRESOURCE, Integer.toString(i - 2) + " " + (extra ? "E" : "S"));
                            gui.sendMessage(command);
                            break;
                        }
                    }
                    return null;
                }
            }).start();
        }
    }

    @FXML
    private void activateProduction() {
        if (!statusProduction) {
            statusProduction = true;
            gui.disableMoves();
            buttonProduction.setText("Done");
            buttonProduction.setDisable(false);
            startTotals();
            toggleProduction0.setDisable(false);
            toggleProduction0.setVisible(true);
            toggleProduction1.setDisable(false);
            toggleProduction1.setVisible(true);
            toggleProduction2.setDisable(false);
            toggleProduction2.setVisible(true);
            toggleProduction3.setDisable(false);
            toggleProduction3.setVisible(true);
            toggleProduction4.setDisable(false);
            toggleProduction4.setVisible(true);
            toggleProduction5.setDisable(false);
            toggleProduction5.setVisible(true);
        } else {
            gui.disableMoves();
            buttonProduction.setText("Production");
            endTotals();
            toggleProduction0.setDisable(true);
            toggleProduction0.setVisible(false);
            toggleProduction1.setDisable(true);
            toggleProduction1.setVisible(false);
            toggleProduction2.setDisable(true);
            toggleProduction2.setVisible(false);
            toggleProduction3.setDisable(true);
            toggleProduction3.setVisible(false);
            toggleProduction4.setDisable(true);
            toggleProduction4.setVisible(false);
            toggleProduction5.setDisable(true);
            toggleProduction5.setVisible(false);
            getProductions();
        }
    }

    private void getProductions() {
        resourceChoicesTotal = 0;
        toggled = new ArrayList<>();
        if (toggleProduction5.isSelected()) {
            toggled.add(5);
            resourceChoicesTotal++;
        }
        if (toggleProduction4.isSelected()) {
            toggled.add(4);
            resourceChoicesTotal++;
        }
        if (toggleProduction3.isSelected()) toggled.add(3);
        if (toggleProduction2.isSelected()) toggled.add(2);
        if (toggleProduction1.isSelected()) toggled.add(1);
        if (toggleProduction0.isSelected()) {
            toggled.add(0);
            resourceChoicesTotal++;
        }
        if (toggled.size() == 0) {
            gui.invalidMove(gui.getClientID());
        } else if (resourceChoicesTotal != 0) {
            startResourceChoice();
        } else {
            endProduction();
        }
    }

    private void endProduction() {
        endResourceChoice();
        if (statusProduction) {
            statusProduction = false;
            new Thread(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    ArrayList<String> args = new ArrayList<>();
                    int i = 0;
                    for (Integer integer : toggled) {
                        args.add(integer.toString());
                        if (integer == 0 || integer == 4 || integer == 5) {
                            args.add(resourceChoices.get(i).name());
                            i++;
                        }
                    }
                    ArrayList<String> storageArgs = new ArrayList<>();
                    ArrayList<String> strongArgs = new ArrayList<>();
                    for (Resource resource : fromStorage.keySet()) {
                        storageArgs.add(resource.name());
                        storageArgs.add(fromStorage.get(resource).toString());
                    }
                    for (Resource resource : fromStrong.keySet()) {
                        strongArgs.add(resource.name());
                        strongArgs.add(fromStrong.get(resource).toString());
                    }
                    Command command;
                    command = new Command(gui.getClientID(), Messages.FROMSTORAGE, String.join(" ", storageArgs));
                    gui.sendMessage(command);
                    command = new Command(gui.getClientID(), Messages.FROMSTRONG, String.join(" ", strongArgs));
                    gui.sendMessage(command);
                    command = new Command(gui.getClientID(), Messages.ACTICVATEPRODUCTION, String.join(" ", args));
                    gui.sendMessage(command);
                    return null;
                }
            }).start();
        } else {
            endResourceChoice();
            new Thread(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    ArrayList<String> args = new ArrayList<>();
                    for (Resource resource : resourceChoices) {
                        args.add(resource.name());
                    }
                    Command command = new Command(gui.getClientID(), Messages.CHOOSERESOURCE, String.join(" ", args));
                    gui.sendMessage(command);
                    return null;
                }
            }).start();
        }
    }

    private void startResourceChoice() {
        statusResourceChoice = true;
        resourceChoices = new ArrayList<>();
        resourceTotals.setVisible(true);
        strongCoin.setDisable(false);
        strongServant.setDisable(false);
        strongShield.setDisable(false);
        strongStone.setDisable(false);
        labelMessage.setText("Choose resources to get by clicking icons in the strongbox.");
        labelMessage.setVisible(true);
    }

    private void endResourceChoice() {
        statusResourceChoice = false;
        labelMessage.setVisible(false);
        labelMessage.setText("Invalid move. Please try again.");
        endTotals();
    }

    /**
     * Lets the player pick resources at the start of the game.
     * @param amount The amount of resources the player can pick
     */
    public void chooseResources(int amount) {
        resourceChoicesTotal = amount;
        startResourceChoice();
    }

    private void startTotals() {
        resourceTotals.setVisible(true);
        if (gui.getBoard(gui.getClientID()).getShelves().size() > 0) storage1.setDisable(false);
        if (gui.getBoard(gui.getClientID()).getShelves().size() > 1) storage2.setDisable(false);
        if (gui.getBoard(gui.getClientID()).getShelves().size() > 2) storage3.setDisable(false);
        strongCoin.setDisable(false);
        strongServant.setDisable(false);
        strongShield.setDisable(false);
        strongStone.setDisable(false);
        fromStorage = new EnumMap<Resource, Integer>(Resource.class);
        fromStrong = new EnumMap<Resource, Integer>(Resource.class);
    }

    private void endTotals() {
        resourceTotals.setVisible(false);
        storage1.setDisable(true);
        storage2.setDisable(true);
        storage3.setDisable(true);
        strongCoin.setDisable(true);
        strongServant.setDisable(true);
        strongShield.setDisable(true);
        strongStone.setDisable(true);
        totalsCoin.setText("0");
        totalsServant.setText("0");
        totalsShield.setText("0");
        totalsStone.setText("0");
    }

    public void buyCard(int index) {
        startTotals();
        labelMessage.setVisible(false);
        buyIndex = index;
        cardBuying.setImage(new Image(String.format("graphics/images/cards/%d.png", gui.getTable().getDecks()[index - 1].getID())));
        labelBuying.setVisible(true);
        buyCol1.setDisable(false);
        buyCol1.setVisible(true);
        buyCol2.setDisable(false);
        buyCol2.setVisible(true);
        buyCol3.setDisable(false);
        buyCol3.setVisible(true);
    }

    @FXML
    private void buyIn1() {
        buy(1);
    }

    @FXML
    private void  buyIn2() {
        buy(2);
    }

    @FXML
    private void buyIn3() {
        buy(3);
    }

    private void buy(int column) {
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ArrayList<String> storageArgs = new ArrayList<>();
                ArrayList<String> strongArgs = new ArrayList<>();
                for (Resource resource : fromStorage.keySet()) {
                    storageArgs.add(resource.name());
                    storageArgs.add(fromStorage.get(resource).toString());
                }
                for (Resource resource : fromStrong.keySet()) {
                    strongArgs.add(resource.name());
                    strongArgs.add(fromStrong.get(resource).toString());
                }
                Command command;
                command = new Command(gui.getClientID(), Messages.FROMSTORAGE, String.join(" ", storageArgs));
                gui.sendMessage(command);
                command = new Command(gui.getClientID(), Messages.FROMSTRONG, String.join(" ", strongArgs));
                gui.sendMessage(command);
                command = new Command(gui.getClientID(), Messages.BUYCARD, buyIndex + " " + column);
                gui.sendMessage(command);
                return null;
            }
        }).start();
        endTotals();
        cardBuying.setImage(null);
        labelBuying.setVisible(false);
        resourceTotals.setVisible(false);
        buyCol1.setDisable(true);
        buyCol1.setVisible(false);
        buyCol2.setDisable(true);
        buyCol2.setVisible(false);
        buyCol3.setDisable(true);
        buyCol3.setVisible(false);
    }

    @FXML
    private void storage1Clicked() {
        storageClicked(0);
    }

    @FXML
    private void storage2Clicked() {
        storageClicked(1);
    }

    @FXML
    private void storage3Clicked() {
        storageClicked(2);
    }

    private void storageClicked(int index) {
        int i = 0;
        for (Resource resource : gui.getBoard(gui.getClientID()).getShelves().keySet()) {
            if (index == i) {
                if (fromStorage.putIfAbsent(resource, 1) != null) {
                    fromStorage.replace(resource, fromStorage.get(resource) + 1);
                }
                if (!fromStrong.containsKey(resource)) {
                    fromStrong.put(resource, 0);
                }
                switch (resource) {
                    case COIN -> totalsCoin.setText(Integer.toString(Integer.parseInt(totalsCoin.getText()) + 1));
                    case SERVANT -> totalsServant.setText(Integer.toString(Integer.parseInt(totalsServant.getText()) + 1));
                    case SHIELD -> totalsShield.setText(Integer.toString(Integer.parseInt(totalsShield.getText()) + 1));
                    case STONE -> totalsStone.setText(Integer.toString(Integer.parseInt(totalsStone.getText()) + 1));
                }
            }
            i++;
        }
    }

    @FXML
    private void strongCoinClicked() {
        if (!statusResourceChoice) {
            strongClicked(Resource.COIN);
        } else {
            resourceChoices.add(Resource.COIN);
            if (resourceChoices.size() == resourceChoicesTotal) endProduction();
        }
    }

    @FXML
    private void strongServantClicked() {
        if (!statusResourceChoice) {
            strongClicked(Resource.SERVANT);
        } else {
            resourceChoices.add(Resource.SERVANT);
            if (resourceChoices.size() == resourceChoicesTotal) endProduction();
        }
    }

    @FXML
    private void strongShieldClicked() {
        if (!statusResourceChoice) {
            strongClicked(Resource.SHIELD);
        } else {
            resourceChoices.add(Resource.SHIELD);
            if (resourceChoices.size() == resourceChoicesTotal) endProduction();
        }
    }
    @FXML
    private void strongStoneClicked() {
        if (!statusResourceChoice) {
            strongClicked(Resource.STONE);
        } else {
            resourceChoices.add(Resource.STONE);
            if (resourceChoices.size() == resourceChoicesTotal) endProduction();
        }
    }

    private void strongClicked(Resource resource) {
        if (fromStrong.putIfAbsent(resource, 1) != null) {
            fromStrong.replace(resource, fromStrong.get(resource) + 1);
        }
        if (!fromStorage.containsKey(resource)) {
            fromStorage.put(resource, 0);
        }
        switch (resource) {
            case COIN -> totalsCoin.setText(Integer.toString(Integer.parseInt(totalsCoin.getText()) + 1));
            case SERVANT -> totalsServant.setText(Integer.toString(Integer.parseInt(totalsServant.getText()) + 1));
            case SHIELD -> totalsShield.setText(Integer.toString(Integer.parseInt(totalsShield.getText()) + 1));
            case STONE -> totalsStone.setText(Integer.toString(Integer.parseInt(totalsStone.getText()) + 1));
        }
    }

    @FXML
    private void playLeader1() {
        disableMoves();
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Command command = new Command(gui.getClientID(), Messages.PLAYLEADER, "1");
                gui.sendMessage(command);
                return null;
            }
        }).start();
    }

    @FXML
    private void playLeader2() {
        disableMoves();
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Command command = new Command(gui.getClientID(), Messages.PLAYLEADER, "2");
                gui.sendMessage(command);
                return null;
            }
        }).start();
    }

    @FXML
    private void discardLeader1() {
        if (gui.getHand()[0] != null) {
            leader1.setImage(null);
            inactive1.setVisible(false);
            new Thread(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    gui.getHand()[0] = null;
                    Command command = new Command(gui.getClientID(), Messages.DISCARDLEADER, "1");
                    gui.sendMessage(command);
                    return null;
                }
            }).start();
        }
    }

    @FXML
    private void discardLeader2() {
        if (gui.getHand()[1] != null) {
            leader2.setImage(null);
            inactive2.setVisible(false);
            new Thread(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    gui.getHand()[1] = null;
                    Command command = new Command(gui.getClientID(), Messages.DISCARDLEADER, "2");
                    gui.sendMessage(command);
                    return null;
                }
            }).start();
        }
    }

    /**
     * Disables buttons to prevent the player from taking actions.
     * Changing view is still possible.
     */
    public void disableMoves()  {
        buttonProduction.setDisable(true);
        buttonPlay1.setDisable(true);
        buttonPlay2.setDisable(true);
        buttonDiscard1.setDisable(true);
        buttonDiscard2.setDisable(true);
    }

    /**
     * Enables buttons to let the player make a move.
     */
    public void enableMoves() {
        buttonProduction.setDisable(false);
        buttonPlay1.setDisable(false);
        buttonPlay2.setDisable(false);
        buttonDiscard1.setDisable(false);
        buttonDiscard2.setDisable(false);
    }

    /**
     * Shows the player they made an invalid move.
     */
    public void invalidMove() {
        labelMessage.setVisible(true);
    }

    @FXML
    private void endTurn() {
        tempSelected = null;
        toStorage.setDisable(true);
        toExtra.setDisable(true);
        tempStorage.setVisible(false);
        buttonEndTurn.setDisable(true);
        buttonEndTurn.setVisible(false);
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Command command = new Command(gui.getClientID(), Messages.ENDTURN, null);
                gui.sendMessage(command);
                return null;
            }
        }).start();
    }

    private ImageView makeTempResource(Resource resource) {
        ImageView tempResource = new ImageView(resourceImages.get(resource));
        tempResource.setFitWidth(50);
        tempResource.setFitHeight(50);
        tempResource.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tempSelected == null) {
                    tempSelected = tempResource;
                    tempResource.setEffect(new DropShadow(BlurType.ONE_PASS_BOX, Color.BLUE, 16, 1, 0, 0));
                } else if (tempSelected == tempResource) {
                    tempSelected = null;
                    tempResource.setEffect(null);
                }
            }
        });
        return tempResource;
    }
}
