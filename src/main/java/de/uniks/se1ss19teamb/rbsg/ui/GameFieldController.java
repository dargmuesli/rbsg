package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.Game;
import de.uniks.se1ss19teamb.rbsg.request.DeleteGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.JoinGameRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameFieldController {

    @FXML
    private Label gameNameLabel;

    private Game game;


    void setUpGameLabel(Game game) {
        this.game = game;
        gameNameLabel.setText(game.getName());
    }

    public void joinGame() {
        JoinGameRequest joinGameRequest = new JoinGameRequest(game.getId(), LoginController.getUserKey());
        joinGameRequest.sendRequest();
        MainController.joinedGame = game;
    }

    public void deleteGame() {
        DeleteGameRequest deleteGameRequest = new DeleteGameRequest(game.getId(), LoginController.getUserKey());
        deleteGameRequest.sendRequest();
        MainController.updateGameView();
    }

}
