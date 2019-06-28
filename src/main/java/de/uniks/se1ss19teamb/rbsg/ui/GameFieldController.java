package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.GameMeta;
import de.uniks.se1ss19teamb.rbsg.request.DeleteGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.JoinGameRequest;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import java.util.Arrays;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


public class GameFieldController {

    @FXML
    private HBox root;

    @FXML
    private Label gameNameLabel;

    private GameMeta gameMeta;
    static GameMeta joinedGame;

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{root}));
    }

    void setUpGameLabel(GameMeta gameMeta) {
        this.gameMeta = gameMeta;
        gameNameLabel.setText(gameMeta.getName());
    }

    public void joinGame() {
        JoinGameRequest joinGameRequest = new JoinGameRequest(gameMeta.getId(), LoginController.getUserKey());
        joinGameRequest.sendRequest();
        joinedGame = gameMeta;

        ArmyManagerController.joiningGame = true;
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/armyManager.fxml", root);
    }

    public void deleteGame() {
        DeleteGameRequest deleteGameRequest = new DeleteGameRequest(gameMeta.getId(), LoginController.getUserKey());
        deleteGameRequest.sendRequest();
    }

}
