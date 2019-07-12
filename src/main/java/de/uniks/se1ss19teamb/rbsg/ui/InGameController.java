package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.uniks.se1ss19teamb.rbsg.model.InGameMetadata;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.PlayerTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;
import java.util.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InGameController {

    @FXML
    private AnchorPane inGameScreen;
    @FXML
    private AnchorPane errorContainer;
    @FXML
    private JFXHamburger hamburgerMenu;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private AnchorPane inGameScreen1;
    @FXML
    private GridPane gameGrid;
    @FXML
    private Pane miniMap;
    @FXML
    private JFXButton btnMiniMap;
    @FXML
    private JFXButton yes;
    @FXML
    private JFXButton no;

    public static Logger logger = LogManager.getLogger();
    public static InGameMetadata inGameMetadata;
    public static Map<Pair<Integer, Integer>, EnvironmentTile> environmentTiles = new HashMap<>();
    private Map<String, StackPane> stackPaneMapByEnvironmentTileId = new HashMap<>();
    private Map<String, EnvironmentTile> environmentTileMapById = new HashMap<>();
    public static List<PlayerTile> playerTiles = new ArrayList<>();
    public static List<UnitTile> unitTiles = new ArrayList<>();
    public static boolean gameInitFinished = false;

    private JFXTabPane chatPane;
    private VBox textArea;
    private TextField message;
    private VBox chatBox;
    private JFXButton btnMinimize;
    private Stage leaveGame = new Stage();

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{inGameScreen, inGameScreen1}));
        Theming.hamburgerMenuTransition(hamburgerMenu, btnBack);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnLogout);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnFullscreen);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnMiniMap);

        Platform.runLater(() -> {
            chatPane = (JFXTabPane) btnLogout.getScene().lookup("#chatPane");
            textArea = (VBox) btnLogout.getScene().lookup("#textArea");
            message = (TextField) btnLogout.getScene().lookup("#message");
            chatBox = (VBox) btnLogout.getScene().lookup("#chatBox");
            btnMinimize = (JFXButton) btnLogout.getScene().lookup("#btnMinimize");
        });

        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);

        UserInterfaceUtils.makeFadeInTransition(inGameScreen);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
            .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/popup.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            // controller not used yet, but it's good to have it for later purposes.
            PopupController controller = fxmlLoader.getController();
            NotificationHandler.getInstance().setPopupController(controller);
            Platform.runLater(() -> {
                errorContainer.getChildren().add(parent);
                errorContainer.toFront();
            });
        } catch (IOException e) {
            NotificationHandler.getInstance()
                .sendError("Fehler beim Laden der FXML-Datei für die Lobby!", logger, e);
        }

        GameSocket.instance = new GameSocket(
            LoginController.getUser(),
            LoginController.getUserKey(),
            GameFieldController.joinedGame.getId(),
            ArmyManagerController.currentArmy.getId());

        GameSocket.instance.registerGameMessageHandler((message, from, isPrivate) -> {
            if (isPrivate) {
                addNewPane(from, message, false, chatPane);
            } else {
                addElement(from, message, textArea, false);
            }
        });

        MainController.setGameChat(GameSocket.instance);

        MainController.setInGameChat(true);

        UserInterfaceUtils.makeFadeInTransition(inGameScreen);
        fillGameGrid();
        miniMap = TextureManager.computeMinimap(environmentTiles, 100, 100, 5);
        miniMap.setVisible(false);
        inGameScreen.getChildren().add(miniMap);
    }

    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        } else if (event.getSource().equals(btnLogout)) {
            LogoutUserRequest logout = new LogoutUserRequest(LoginController.getUserKey());
            logout.sendRequest();
            if (logout.getSuccessful()) {
                LoginController.setUserKey(null);
                UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", inGameScreen);
            }
        } else if (event.getSource().equals(btnMiniMap)) {
            if (miniMap.isVisible()) {
                miniMap.setVisible(false);
            } else {
                miniMap.setVisible(true);
            }
        }
    }

    private void fillGameGrid() {
        int maxX = 0;
        int maxY = 0;
        int tryCounter = 0;

        while (!gameInitFinished) {
            try {
                Thread.sleep(1000);
                tryCounter++;
                if (tryCounter == 10) {
                    NotificationHandler.getInstance().sendError("The tiles couldn't be load.",
                        logger);
                    break;
                }

            } catch (InterruptedException e) {
                NotificationHandler.getInstance()
                    .sendError("Fehler beim initialisieren vom Spiel!", logger, e);
            }
        }

        while (environmentTiles.get(new Pair<>(0, maxX)) != null) {
            maxX++;
        }
        while (environmentTiles.get(new Pair<>(maxY, 0)) != null) {
            maxY++;
        }


        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                StackPane stack = new StackPane();
                stack.getChildren().addAll(TextureManager.computeTerrainTextureInstance(environmentTiles, j, i));
                gameGrid.add(stack, j, i);
                stackPaneMapByEnvironmentTileId.put(environmentTiles.get(new Pair<>(j, i)).getId(), stack);
                environmentTileMapById.put(environmentTiles.get(new Pair<>(j, i)).getId(),
                    environmentTiles.get(new Pair<>(j, i)));
            }
        }

        for (UnitTile unitTile : unitTiles) {
            gameGrid.getChildren().remove(stackPaneMapByEnvironmentTileId.get(unitTile.getPosition()));
            StackPane newStackPane = new StackPane();
            int posX = environmentTileMapById.get(unitTile.getPosition()).getX();
            int posY = environmentTileMapById.get(unitTile.getPosition()).getY();
            newStackPane.getChildren().addAll(TextureManager
                .computeTerrainTextureInstance(environmentTiles, posX, posY));
            newStackPane.getChildren().addAll(TextureManager.getTextureInstance(unitTile.getType()));
            gameGrid.add(newStackPane, posX, posY);

        }
        NotificationHandler.getInstance().sendSuccess("Spiel wurde initialisiert!", logger);
    }

    private void addElement(String player, String message, VBox box, boolean whisper) {

        VBox container = new VBox();

        if (player != null) {
            Label name = new Label(player + ":");
            name.setPadding(new Insets(5));
            name.setWrapText(true);
            if (whisper) {
                name.setStyle("-fx-text-fill: -fx-privatetext;");
            } else {
                name.setStyle("-fx-text-fill: black;");
            }
            // whisper on double click
            name.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        setPrivate(player, -1);
                    }
                }
            });
            Label text = new Label(message);
            text.setPadding(new Insets(5));
            text.setWrapText(true);
            setChatStyle(text);

            Platform.runLater(() -> {
                name.setMaxWidth(Region.USE_COMPUTED_SIZE);
                setChatStyle(name);
            });

            container.getChildren().addAll(name, text);
        } else {
            Label text = new Label(message);
            text.setPadding(new Insets(5));
            text.setWrapText(true);
            setChatStyle(text);

            container.getChildren().add(text);
        }

        Platform.runLater(() -> box.getChildren().add(container));
        if (!chatPane.getTabs().get(0).isSelected() && !whisper) {
            Platform.runLater(() ->
                chatPane.getTabs().get(0).setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_CIRCLE)));
        }

        if (!chatBox.isVisible()) {
            Platform.runLater(() ->
                btnMinimize.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_CIRCLE)));
        }
    }

    private void setChatStyle(Label label) {
        label.setStyle("-fx-text-fill: " + (Theming.darkModeActive()
            ? "-fx-primary" : "black") + ";"
            + "-fx-background-color: " + (Theming.darkModeActive()
            ? "-fx-secondary" : "white") + ";"
            + "-fx-border-radius: 20px;"
            + "-fx-background-radius: 10px;");
    }

    private void setPrivate(String input, int count) {
        if (count == -1) {
            MainController.sendTo = input;
        } else if (count == 0) {
            MainController.sendTo = input.substring(3);
        } else {
            MainController.sendTo = input.substring(3, count);
        }
        Platform.runLater(() -> {
            addNewPane(MainController.sendTo, null, true, chatPane);
            message.clear();
            message.setStyle("-fx-text-fill: -fx-privatetext;"
                + "-jfx-focus-color: -fx-privatetext;");
        });
    }

    private void getPrivate(String from, String message, Tab tab) {
        ScrollPane scrollPane = (ScrollPane) tab.getContent();
        VBox area = (VBox) scrollPane.getContent();
        if (message != null) {
            addElement(from, message, area, true);
        }
        MainController.selectionModel.select(tab);
    }

    private void addNewPane(String from, String message, boolean mymessage, JFXTabPane pane) {
        boolean createTab = true;
        for (Tab t : pane.getTabs()) {
            if (t.getText().equals(from)) {
                if (!t.isSelected()) {
                    Platform.runLater(() -> t.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_CIRCLE)));
                }
                if (mymessage) {
                    getPrivate(LoginController.getUser(), message, t);
                    createTab = false;
                } else {
                    getPrivate(from, message, t);
                    createTab = false;
                }
            }
        }
        if (createTab) {
            Platform.runLater(
                () -> {
                    try {
                        Tab newTab = FXMLLoader
                            .load(this.getClass().getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/privateTab.fxml"));
                        newTab.setText(from);
                        pane.getTabs().add(newTab);
                        if (mymessage) {
                            getPrivate(LoginController.getUser(), message, newTab);
                        } else {
                            getPrivate(from, message, newTab);
                        }
                        MainController.selectionModel.select(newTab);
                    } catch (IOException e) {
                        NotificationHandler.getInstance()
                            .sendError("Ein GameField konnte nicht geladen werden!", logger, e);
                    }
                }
            );
        }
    }

    public void leaveGame(ActionEvent event) {

        if(event.getSource().equals(btnBack)) {
            try {
                Parent parent = FXMLLoader
                    .load(getClass().getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/leave.fxml"));

                leaveGame.setScene(new Scene(parent, 200, 100));
                leaveGame.show();
                leaveGame.setResizable(false);
            } catch (IOException b) {
                b.printStackTrace();
            }
        } else if (event.getSource().equals(yes)) {
            GameSocket.instance.disconnect();
            MainController.setInGameChat(false);
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", inGameScreen);
        } else if (event.getSource().equals(no)) {
            leaveGame.close();
        }

    }
}
