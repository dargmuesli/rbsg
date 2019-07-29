package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGameObject;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static GameLobbyController instance;

    private static final Logger logger = LogManager.getLogger();

    @FXML
    private AnchorPane errorContainer;
    @FXML
    private AnchorPane gameLobby;
    @FXML
    private AnchorPane gameLobby1;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnStart;
    @FXML
    private JFXHamburger hamburgerMenu;
    @FXML
    private Label gameName;
    @FXML
    private ListView<Parent> playerList;

    private JFXTabPane chatPane;
    private VBox textArea;
    private TextField message;
    private VBox chatBox;
    private JFXButton btnMinimize;

    @FXML
    private void initialize() {
        UserInterfaceUtils.initialize(
            gameLobby, gameLobby1, GameLobbyController.class, btnFullscreen, errorContainer);

        GameLobbyController.instance = this;

        GameSocket.instance = new GameSocket(
            GameSelectionController.joinedGame.getId());

        GameSocket.instance.registerMessageHandler((message, from, isPrivate) -> {
            if (isPrivate) {
                MainController.instance.addNewPane(from, message, false, chatPane);
            } else {
                MainController.instance.addElement(from, message, textArea, false);
            }
        });

        Platform.runLater(() -> {
            chatPane = (JFXTabPane) btnLogout.getScene().lookup("#chatPane");
            textArea = (VBox) btnLogout.getScene().lookup("#textArea");
            message = (TextField) btnLogout.getScene().lookup("#message");
            chatBox = (VBox) btnLogout.getScene().lookup("#chatBox");
            btnMinimize = (JFXButton) btnLogout.getScene().lookup("#btnMinimize");
        });

        MainController.setGameChat(GameSocket.instance);

        gameName.setText(GameSelectionController.joinedGame.getName());
    }

    public void updatePlayers() {
        InGameController.inGameObjects.entrySet().stream()
            .filter(stringInGameObjectEntry -> stringInGameObjectEntry.getValue() instanceof InGamePlayer)
            .forEachOrdered(inGameObjectEntry -> {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/lobbyPlayer.fxml"));

                try {
                    Parent parent = fxmlLoader.load();
                    LobbyPlayerController controller = fxmlLoader.getController();
                    controller.setInGamePlayer((InGamePlayer) inGameObjectEntry.getValue());
                    playerList.getItems().add(parent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    @FXML
    private void goBack() {
        btnBack.setDisable(true);
        GameSocket.instance.disconnect();
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", gameLobby);
    }

    @FXML
    private void logout() {
        if (!RequestUtil.request(new LogoutUserRequest(LoginController.getUserToken()))) {
            return;
        }

        btnLogout.setDisable(true);
        LoginController.setUserToken(null);
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", gameLobby);
    }

    @FXML
    private void toggleFullscreen() {
        UserInterfaceUtils.toggleFullscreen(btnFullscreen);
    }

    @FXML
    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnLogout)) {
            if (!RequestUtil.request(new LogoutUserRequest(LoginController.getUserToken()))) {
                return;
            }
            btnLogout.setDisable(true);
            LoginController.setUserToken(null);
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", gameLobby);
        } else if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        } else if (event.getSource().equals(btnStart)) {
            Platform.runLater(() -> {
                GameSocket.instance.readyToPlay();
                btnStart.setDisable(true);
            });
        } else if (event.getSource().equals(btnStart)) {
            NotificationHandler.getInstance().sendInfo("No army selected.", logger);
        } else if (event.getSource().equals(btnStart)) {
            RequestUtil.request(new QueryArmiesRequest(LoginController.getUserToken())).ifPresent(armies -> {
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
}

