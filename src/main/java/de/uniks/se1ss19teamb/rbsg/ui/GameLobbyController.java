package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToggleButton;
import de.uniks.se1ss19teamb.rbsg.chat.Chat;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocketDistributor;
import de.uniks.se1ss19teamb.rbsg.util.*;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameLobbyController {

    public static GameLobbyController instance;

    public boolean gameInitFinished = false;

    @FXML
    public AnchorPane apnFade;
    @FXML
    public VBox vbxMinimap;
    @FXML
    private AnchorPane errorContainer;
    @FXML
    private AnchorPane apnRoot;
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
    @FXML
    private JFXButton botButton;

    private JFXTabPane chatPane;
    private VBox textArea;
    private TextField message;
    private VBox chatBox;
    private JFXButton btnMinimize;

    private GameSocket gameSocket;

    @FXML
    private void initialize() {
        UserInterfaceUtils.initialize(
            apnFade, apnRoot, GameLobbyController.class, btnFullscreen, errorContainer);

        GameLobbyController.instance = this;
        GameSocketDistributor
            .setGameSocket(0, GameSelectionController.joinedGame.getId());
        gameSocket = GameSocketDistributor.getGameSocket(0);
        gameSocket.registerMessageHandler((message, from, isPrivate) -> {
            if (isPrivate) {
                MainController.instance.addNewPane(from, message, false, chatPane);
            } else {
                MainController.instance.addElement(from, message, textArea, false);
            }
        });
        gameSocket.connect();

        MainController.chat = new Chat(gameSocket, Chat.chatLogPath);

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
            "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", apnFade);
    }

    @FXML
    private void logout() {
        UserInterfaceUtils.logout(apnFade, btnLogout);
        GameLobbyController.quit();
    }

    @FXML
    private void toggleFullscreen() {
        UserInterfaceUtils.toggleFullscreen(btnFullscreen);
    }

    @FXML
    private void toggleReadiness() {
        gameSocket.readyToPlay();
        tglReadiness.setDisable(true);
    }

    @FXML
    private void startGame() {
        gameSocket.startGame();
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
        VBox chatWindow = (VBox) apnFade.getScene().lookup("#chatWindow");
        JFXButton btnMinimize = (JFXButton) chatWindow.lookup("#btnMinimize");
        btnMinimize.setDisable(false);

        UserInterfaceUtils.makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/fxmls/inGame.fxml", apnFade,
            apnFade.getScene().lookup("#chatWindow"));
        btnMinimize.fire();
    }

    private static void quit() {
        GameSocketDistributor.getGameSocket(0).leaveGame();
        GameSocketDistributor.getGameSocket(0).disconnect();
        GameLobbyController.instance = null;
    }

    public TextField getMessage() {
        return message;
    }

    public VBox getChatBox() {
        return chatBox;
    }

    public JFXButton getBtnMinimize() {
        return btnMinimize;
    }

    public void openBotWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
            .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/botManager.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            BotManagerController controller = fxmlLoader.getController();
            Stage botWindow = new Stage();
            botWindow.setTitle("Bot Manager");
            botWindow.initModality(Modality.APPLICATION_MODAL);
            botWindow.initStyle(StageStyle.DECORATED);
            botWindow.setScene(new Scene(parent));
            botWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

