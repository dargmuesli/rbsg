package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameLobbyController {

    private static final Logger logger = LogManager.getLogger();

    @FXML
    private AnchorPane gameLobby1;
    @FXML
    private AnchorPane gameLobby;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private JFXHamburger hamburgerMenu;
    @FXML
    private Label army1;
    @FXML
    private Label army2;
    @FXML
    private Label army3;
    @FXML
    private Label gameName;
    @FXML
    private ListView<Label> playerList;
    @FXML
    private JFXButton btnReady;
    @FXML
    private JFXButton select1;
    @FXML
    private JFXButton select2;
    @FXML
    private JFXButton select3;
    @FXML
    private ListView<Parent> armyList;
    @FXML
    private JFXButton btnMyReady;
    @FXML
    private JFXButton btnStart;
    @FXML
    private AnchorPane errorContainer;

    private static final Path ARMY_SAVE_PATH =
        Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "rbsg_army-save-%d.json");
    private ArrayList<UnitConfigController> configControllers = new ArrayList<>();
    private Army currentArmy;
    private Army armyBuffer1 = new Army(null, null, new ArrayList<>());
    private Army armyBuffer2 = new Army(null, null, new ArrayList<>());
    private Army armyBuffer3 = new Army(null, null, new ArrayList<>());

    private JFXTabPane chatPane;
    private VBox textArea;
    private TextField message;
    private VBox chatBox;
    private JFXButton btnMinimize;
    private boolean isSelected = false;
    
    public static GameLobbyController instance;

    public void initialize() {
        UserInterfaceUtils.initialize(
            gameLobby, gameLobby1, GameLobbyController.class, btnFullscreen, errorContainer);

        Theming.setTheme(Arrays.asList(new Pane[]{gameLobby, gameLobby1}));
        Theming.hamburgerMenuTransition(hamburgerMenu, btnBack);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnLogout);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnFullscreen);

        gameName.setText(GameSelectionController.joinedGame.getName());

        instance = this;

        GameSocket.instance = new GameSocket(
            LoginController.getUser(),
            LoginController.getUserKey(),
            GameSelectionController.joinedGame.getId(),
            ArmyManagerController.currentArmy.getId(),
            ArmyManagerController.spectator);


        Platform.runLater(() -> {
            chatPane = (JFXTabPane) btnLogout.getScene().lookup("#chatPane");
            textArea = (VBox) btnLogout.getScene().lookup("#textArea");
            message = (TextField) btnLogout.getScene().lookup("#message");
            chatBox = (VBox) btnLogout.getScene().lookup("#chatBox");
            btnMinimize = (JFXButton) btnLogout.getScene().lookup("#btnMinimize");
        });


        GameSocket.instance.registerGameMessageHandler((message, from, isPrivate) -> {
            if (isPrivate) {
                addNewPane(from, message, false, chatPane);
            } else {
                addElement(from, message, textArea, false);
            }
        });

        MainController.setGameChat(GameSocket.instance);
        MainController.setInGameChat(true);

        RequestUtil.request(new QueryUnitsRequest(LoginController.getUserKey())).ifPresent(units -> {
            for (Unit unit : units) {
                ArmyManagerController.availableUnits.put(unit.getId(), unit);
            }
        });

        setArmyName();
        showArmyConfig();
        //Testing playerList with someValues
        playerList.getItems().add(new Label("Test"));
        playerList.getItems().add(new Label("Test2"));
        playerList.getItems().add(new Label("Test3"));
    }


    private void showArmyConfig() {

        ArmyManagerController.availableUnits.forEach((s, unit) -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/unitConfig.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                UnitConfigController configController = fxmlLoader.getController();
                configController.loadConfig(unit);
                configControllers.add(configController);
                armyList.getItems().add(parent);
                armyList.setOrientation(Orientation.HORIZONTAL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        for (int i = 1; i <= 3; i++) {
            currentArmy = loadArmyConfig(i);

            if (currentArmy != null) {
                for (UnitConfigController configController : configControllers) {
                    configController.loadNumberOfUnit(currentArmy, i);
                }
            }
        }

    }

    private void setArmyName() {
        armyBuffer1 = loadArmyConfig(1);
        armyBuffer2 = loadArmyConfig(2);
        armyBuffer3 = loadArmyConfig(3);

        army1.setText(armyBuffer1 != null ? armyBuffer1.getName() : "");
        army2.setText(armyBuffer2 != null ? armyBuffer2.getName() : "");
        army3.setText(armyBuffer3 != null ? armyBuffer3.getName() : "");
    }

    private Army loadArmyConfig(int number) {
        File save = new File(String.format(ARMY_SAVE_PATH.toString(), number));

        if (save.exists()) {
            return SerializeUtils.deserialize(save, Army.class);
        } else {
            return null;
        }
    }


    @FXML
    private void eventHandler(ActionEvent event) {
        if (event.getSource().equals(btnBack)) {
            GameSocket.instance.disconnect();
            MainController.setInGameChat(false);
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", gameLobby);
        }
    }

    @FXML
    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnLogout)) {
            if (!RequestUtil.request(new LogoutUserRequest(LoginController.getUserKey()))) {
                return;
            }

            LoginController.setUserKey(null);
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", gameLobby);
        } else if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        } else if (event.getSource().equals(select1)) {
            if (armyBuffer1 != null) {
                GameSocket.instance.changeArmy(ArmyUtil.saveToServer(armyBuffer1));
                select1.setDisable(true);
                select2.setDisable(false);
                select3.setDisable(false);
                isSelected = true;
            } else {
                NotificationHandler.getInstance().sendError("Army Buffer is null!", logger);
            }
        } else if (event.getSource().equals(select2)) {
            if (armyBuffer2 != null) {
                GameSocket.instance.changeArmy(ArmyUtil.saveToServer(armyBuffer2));
                select1.setDisable(false);
                select2.setDisable(true);
                select3.setDisable(false);
                isSelected = true;
            } else {
                NotificationHandler.getInstance().sendError("Army Buffer is null!", logger);
            }
        } else if (event.getSource().equals(select3)) {
            if (armyBuffer3 != null) {
                GameSocket.instance.changeArmy(ArmyUtil.saveToServer(armyBuffer3));
                select1.setDisable(false);
                select2.setDisable(false);
                select3.setDisable(true);
                isSelected = true;
            } else {
                NotificationHandler.getInstance().sendError("Army Buffer is null!", logger);
            }
        } else if (event.getSource().equals(btnMyReady) && isSelected) {
            Platform.runLater(() -> {
                GameSocket.instance.readyToPlay();
                btnMyReady.setDisable(true);
                select1.setDisable(true);
                select2.setDisable(true);
                select3.setDisable(true);
            });

        } else if (event.getSource().equals(btnMyReady)) {
            NotificationHandler.getInstance().sendInfo("Es wurde keine Armee ausgew\u00E4hlt !", logger);
        } else if (event.getSource().equals(btnStart)) {
            RequestUtil.request(new QueryArmiesRequest(LoginController.getUserKey())).ifPresent(armies -> {
                loadFromServer();

                if (armies.size() != 0) {
                    GameSocket.instance.startGame();
                }
            });
        }
    }
    
    public void startGameTransition() {
        VBox chatWindow = (VBox) gameLobby.getScene().lookup("#chatWindow");
        JFXButton btnMinimize = (JFXButton) chatWindow.lookup("#btnMinimize");
        btnMinimize.setDisable(false);
     
        UserInterfaceUtils.makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/fxmls/inGame.fxml", gameLobby,
                gameLobby.getScene().lookup("#chatWindow"));
        btnMinimize.fire();
    }

    private void loadFromServer() {
        RequestUtil.request(new QueryArmiesRequest(LoginController.getUserKey())).ifPresent(armies -> {
            if (armies.size() == 0) {
                NotificationHandler.getInstance()
                    .sendInfo("Keine Armeen auf dem Server gespeichert.", logger);
            }
        });
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


}

