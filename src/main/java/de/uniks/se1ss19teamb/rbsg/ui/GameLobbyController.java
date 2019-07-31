package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToggleButton;
import de.uniks.se1ss19teamb.rbsg.chat.Chat;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.*;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class GameLobbyController {

    public static GameLobbyController instance;

    @FXML
    public VBox vbxMinimap;

    @FXML
    private AnchorPane errorContainer;
    @FXML
    public AnchorPane gameLobby;
    @FXML
    private AnchorPane gameLobby1;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnStartGame;
    @FXML
    private JFXToggleButton tglReadiness;
    @FXML
    private Label gameName;
    @FXML
    private VBox playerList;

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

        MainController.chat = new Chat(GameSocket.instance, Chat.chatLogPath);

        GameSocket.instance = new GameSocket(
            GameSelectionController.joinedGame.getId());
        GameSocket.instance.registerMessageHandler((message, from, isPrivate) -> {
            if (isPrivate) {
                MainController.instance.addNewPane(from, message, false, chatPane);
            } else {
                MainController.instance.addElement(from, message, textArea, false);
            }
        });
        GameSocket.instance.connect();

        Platform.runLater(() -> {
            chatPane = (JFXTabPane) btnLogout.getScene().lookup("#chatPane");
            textArea = (VBox) btnLogout.getScene().lookup("#textArea");
            message = (TextField) btnLogout.getScene().lookup("#message");
            chatBox = (VBox) btnLogout.getScene().lookup("#chatBox");
            btnMinimize = (JFXButton) btnLogout.getScene().lookup("#btnMinimize");
        });

        gameName.setText(GameSelectionController.joinedGame.getName());
    }

    public void updatePlayers() {
        Platform.runLater(() -> {
            playerList.getChildren().clear();

            InGameController.inGameObjects.entrySet().stream()
                .filter(stringInGameObjectEntry -> stringInGameObjectEntry.getValue() instanceof InGamePlayer)
                .forEachOrdered(inGameObjectEntry -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                        .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/lobbyPlayer.fxml"));

                    try {
                        Parent parent = fxmlLoader.load();
                        LobbyPlayerController controller = fxmlLoader.getController();
                        controller.setInGamePlayer((InGamePlayer) inGameObjectEntry.getValue());
                        playerList.getChildren().add(parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        });
    }

    @FXML
    private void goBack() {
        btnBack.setDisable(true);
        GameLobbyController.quit();
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", gameLobby);
    }

    @FXML
    private void logout() {
        UserInterfaceUtils.logout(gameLobby, btnLogout);
        GameLobbyController.quit();
    }

    @FXML
    private void toggleFullscreen() {
        UserInterfaceUtils.toggleFullscreen(btnFullscreen);
    }

    @FXML
    private void toggleReadiness() {
        GameSocket.instance.readyToPlay();
        tglReadiness.setDisable(true);
    }

    @FXML
    private void startGame() {
        GameSocket.instance.startGame();
    }

    public void confirmReadiness() {
        Platform.runLater(() -> {
            tglReadiness.setText("Ready");
            btnStartGame.setDisable(false);
        });
    }

    public void denyReadiness() {
        Platform.runLater(() -> {
            tglReadiness.setSelected(false);
            tglReadiness.setDisable(false);
        });
    }

    public void startGameTransition() {
        VBox chatWindow = (VBox) gameLobby.getScene().lookup("#chatWindow");
        JFXButton btnMinimize = (JFXButton) chatWindow.lookup("#btnMinimize");
        btnMinimize.setDisable(false);
     
        UserInterfaceUtils.makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/fxmls/inGame.fxml", gameLobby,
                gameLobby.getScene().lookup("#chatWindow"));
        btnMinimize.fire();
    }

    private static void quit() {
        GameSocket.instance.leaveGame();
        GameSocket.instance.disconnect();
        GameLobbyController.instance = null;
    }
}

