package de.uniks.se1ss19teamb.rbsg.ui.modules;

import de.uniks.se1ss19teamb.rbsg.model.GameMeta;
import de.uniks.se1ss19teamb.rbsg.request.DeleteGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.JoinGameRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.SystemSocket;
import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.ui.MainController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.RequestUtil;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;

public class GameSelectionController {

    public static GameMeta joinedGame;

    @FXML
    private Button join;
    @FXML
    private Button spectate;
    @FXML
    private HBox hbxRoot;
    @FXML
    private Label gameNameLabel;
    @FXML
    private Label spaces;

    private GameMeta gameMeta;
    private VBox chatWindow;

    public static boolean spectator = false;

    @FXML
    private void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{hbxRoot}));

        GameSelectionController.spectator = false;
    }

    /**
     * Sets this element's labels' texts.
     *
     * @param gameMeta The data to source the information that is to be displayed from.
     */
    public void setUpGameLabel(GameMeta gameMeta) {
        this.gameMeta = gameMeta;
        gameNameLabel.setText(gameMeta.getName());
        spaces.setText(gameMeta.getNeededPlayers() - gameMeta.getJoinedPlayers() + " ");
    }

    /**
     * Joins the game.
     */
    public void joinGame() {
        join();
        join.setDisable(true);
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/gameLobby.fxml", MainController.instance.apnFade, chatWindow, false);
    }

    private void join() {
        SystemSocket.instance.disconnect();
        ChatSocket.instance.disconnect();

        joinedGame = gameMeta;

        chatWindow = (VBox) hbxRoot.getScene().lookup("#chatWindow");
        chatWindow.setPrefHeight(300);
        chatWindow.setPrefWidth(400);

        if (!GameSelectionController.spectator) {
            if (!RequestUtil.request(new JoinGameRequest(gameMeta.getId(), LoginController.getUserToken()))) {
                NotificationHandler.sendError("Could not join game!", LogManager.getLogger());
            }
        }
    }

    /**
     * Deletes the game.
     */
    public void deleteGame() {
        if (!RequestUtil.request(new DeleteGameRequest(gameMeta.getId(), LoginController.getUserToken()))) {
            NotificationHandler.sendError("Could not delete game!", LogManager.getLogger());
        }
    }

    /**
     * Joins a game in spectator mode.
     */
    public void spectate() {
        GameSelectionController.spectator = true;
        join();
        chatWindow.setVisible(false);
        spectate.setDisable(true);
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/gameLobby.fxml", MainController.instance.apnFade, chatWindow, false);
    }
}
