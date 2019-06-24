package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.GameMeta;
import de.uniks.se1ss19teamb.rbsg.request.DeleteGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.JoinGameRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameFieldController {

    @FXML
    private Label gameNameLabel;

    private GameMeta gameMeta;

    private MainController mainController;


    void setUpGameLabel(GameMeta gameMeta, MainController mainController) {
        this.gameMeta = gameMeta;
        this.mainController = mainController;
        gameNameLabel.setText(gameMeta.getName());
    }

    public void joinGame() {
        JoinGameRequest joinGameRequest = new JoinGameRequest(gameMeta.getId(), LoginController.getUserKey());
        joinGameRequest.sendRequest();
        mainController.setJoinedGameMeta(gameMeta);
    }

    public void deleteGame() {
        DeleteGameRequest deleteGameRequest = new DeleteGameRequest(gameMeta.getId(), LoginController.getUserKey());
        deleteGameRequest.sendRequest();
        mainController.updateGameView();
    }

}
