package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.model.GameMeta;
import de.uniks.se1ss19teamb.rbsg.request.DeleteGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.JoinGameRequest;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameFieldController {

    @FXML
    private HBox root;

    @FXML
    private Label gameNameLabel;

    private GameMeta gameMeta;
    static GameMeta joinedGame;


    void setUpGameLabel(GameMeta gameMeta) {
        this.gameMeta = gameMeta;
        gameNameLabel.setText(gameMeta.getName());
    }

    public void joinGame() {
        JoinGameRequest joinGameRequest = new JoinGameRequest(gameMeta.getId(), LoginController.getUserKey());
        joinGameRequest.sendRequest();
        joinedGame = gameMeta;

        ArmyManagerController.joiningGame = true;

        VBox chatWindow = (VBox) root.getScene().lookup("#chatWindow");
        JFXButton btnMinimize = (JFXButton) chatWindow.lookup("#btnMinimize");
        btnMinimize.setDisable(false);
        btnMinimize.fire();
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/armyManager.fxml", root, chatWindow);
    }

    public void deleteGame() {
        DeleteGameRequest deleteGameRequest = new DeleteGameRequest(gameMeta.getId(), LoginController.getUserKey());
        deleteGameRequest.sendRequest();
    }

}
