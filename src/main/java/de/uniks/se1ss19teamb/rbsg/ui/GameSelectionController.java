package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.model.GameMeta;
import de.uniks.se1ss19teamb.rbsg.request.DeleteGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.JoinGameRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.SystemSocket;
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

public class GameSelectionController {

    @FXML
    private HBox root;

    @FXML
    private Label spaces;

    @FXML
    private Label gameNameLabel;

    @FXML
    private Button join;

    @FXML
    private Button spectate;

    private GameMeta gameMeta;
    static GameMeta joinedGame;

    private VBox chatWindow;

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{root}));
    }

    void setUpGameLabel(GameMeta gameMeta) {
        this.gameMeta = gameMeta;
        gameNameLabel.setText(gameMeta.getName());
        String s = String.valueOf(gameMeta.getNeededPlayers() - gameMeta.getJoinedPlayers() + " ");
        spaces.setText(s);
    }

    public void joinGame() {
        join();
        join.setDisable(true);
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/gameLobby.fxml", root, chatWindow, false);

    }

    public void join() {
        SystemSocket.instance.disconnect();
        ChatSocket.instance.disconnect();

        if (!RequestUtil.request(new JoinGameRequest(gameMeta.getId(), LoginController.getUserToken()))) {
            return;
        }

        joinedGame = gameMeta;

        chatWindow = (VBox) root.getScene().lookup("#chatWindow");
        JFXButton btnMinimize = (JFXButton) chatWindow.lookup("#btnMinimize");
        // sehr komisch, wenn man zuerst disable(true) und dann fire(), minimiert er das fenster nicht
        // wenn man zuerst fire() macht dann disable(true), minimiert er das fenster auch nicht,
        // damit gehts:

        /*
        btnMinimize.setDisable(false);
        btnMinimize.fire();
        btnMinimize.setDisable(true);

         */

        chatWindow.setPrefHeight(300);
        chatWindow.setPrefWidth(400);
    }

    public void deleteGame() {
        if (!RequestUtil.request(new DeleteGameRequest(gameMeta.getId(), LoginController.getUserToken()))) {
            return;
        }
    }

    public void spectate() {
        ArmyManagerController.spectator = true;
        join();
        chatWindow.setVisible(false);
        spectate.setDisable(true);
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/gameLobby.fxml", root, chatWindow);
    }
}
