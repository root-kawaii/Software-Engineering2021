package it.polimi.ingsw.client.GUI;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.HashMap;

/**
 * Class that creates the GUI.
 */
public class Display extends Application {
    private static boolean online = true;
    private Stage stage;
    private GUI gui;
    private HashMap<Integer, Scene> boards = new HashMap<>();
    private HashMap<Integer, PlayerBoardController> boardControllers = new HashMap<>();
    private Scene table;
    private TableController tableController;

    /**
     * Starts the GUI.
     * Called by launch.
     * @param stage The stage
     * @throws Exception If something went wrong
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        stage.setTitle("Masters of Renaissance");
        stage.getIcons().add(new Image("graphics/images/icons/lorenzo.png"));
        stage.setResizable(false);
        this.stage = stage;
        if (online) {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("graphics/fxml/connect.fxml"));
            AnchorPane anchorPane = loader.load();
            ConnectController connectController = loader.getController();
            connectController.setDisplay(this);
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.sizeToScene();
        } else {
            new Thread(new GUI(this)).start();
        }
        stage.show();
    }

    /**
     * Sets up the Table's scene and controller.
     */
    public void setup() {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("graphics/fxml/table.fxml"));
        try {
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            TableController tableController = loader.getController();
            table = scene;
            this.tableController = tableController;
            gui.setTableController(tableController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the max player choice screen
     */
    public void askMaxPlayers() {
        try {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("graphics/fxml/maxPlayers.fxml"));
            AnchorPane anchorPane = loader.load();
            MaxPlayersController maxPlayersController = loader.getController();
            maxPlayersController.setGui(gui);
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Shows the username choice screen.
     * @param invalid If true notifies the player they have to pick another username
     */
    public void askUsername(boolean invalid) {
        try {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("graphics/fxml/username.fxml"));
            AnchorPane anchorPane = loader.load();
            UsernameController usernameController = loader.getController();
            usernameController.setGui(gui);
            if (invalid) {
                usernameController.invalid();
            }
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Shows the leader choice screen.
     * @param cardIDs IDs of the cards to choose from
     */
    public void askLeaders(int[] cardIDs) {
        try {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("graphics/fxml/leaders.fxml"));
            AnchorPane anchorPane = loader.load();
            LeadersController leadersController = loader.getController();
            leadersController.setGui(gui);
            leadersController.setCardIDs(cardIDs);
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Creates scenes and controllers for each player board.
     */
    public void startGame() {
        for (Integer clientID : gui.getBoards().keySet()) {
            try {
                FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("graphics/fxml/playerBoard.fxml"));
                AnchorPane anchorPane = loader.load();
                PlayerBoardController playerBoardController = loader.getController();
                playerBoardController.setup(this, gui, gui.getBoard(clientID).getUsername());
                Scene scene = new Scene(anchorPane);
                boards.put(clientID, scene);
                boardControllers.put(clientID, loader.getController());
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        gui.setSceneControllers(boardControllers);
        tableController.setup(this, gui);
        stage.setScene(boards.get(gui.getClientID()));
        stage.sizeToScene();
    }

    /**
     * Changes scene to the specified client's.
     * @param clientID ID of the client to switch to
     */
    public void switchView(int clientID) {
        if (clientID == 0) {
            stage.setScene(table);
        } else {
            stage.setScene(boards.get(clientID));
        }
        stage.sizeToScene();
    }

    /**
     * Shows the reconnection screen.
     * @param username The username to reconnect as
     */
    public void reconnect(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("graphics/fxml/reconnect.fxml"));
            AnchorPane anchorPane = loader.load();
            ReconnectController reconnectController = loader.getController();
            reconnectController.setGui(gui);
            reconnectController.setLabelReconnect(username);
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the winner of the game and lets the user quit.
     * @param winner Username of the winner
     */
    public void endGame(String winner) {
        try {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("graphics/fxml/end.fxml"));
            AnchorPane anchorPane = loader.load();
            EndController endController = loader.getController();
            endController.setWinner(winner);
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.sizeToScene();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates IDs of a scene and a controller when a player reconnects
     * @param old Old ID
     * @param current new ID
     */
    public void updateID(int old, int current) {
        boardControllers.put(current, boardControllers.get(old));
        boardControllers.remove(old);
        boards.put(current, boards.get(old));
        boards.remove(old);
    }

    /**
     * Sets the GUI.
     * @param gui the GUI
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Launches the GUI.
     * @param args If there's arguments, start in local mode
     */
    public static void main(String[] args) {
        if (args != null) {
            online = false;
        }
        Display.launch();
        System.exit(0);
    }
}
