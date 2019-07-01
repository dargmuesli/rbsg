package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.GameMeta;
import de.uniks.se1ss19teamb.rbsg.request.DeleteGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.JoinGameRequest;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

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
        // TODO VADIM root.getChildren().add(root.getScene().lookup("#chatWindow"));
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/armyManager.fxml", root);
    }

    public void deleteGame() {
        DeleteGameRequest deleteGameRequest = new DeleteGameRequest(gameMeta.getId(), LoginController.getUserKey());
        deleteGameRequest.sendRequest();
    }

}
