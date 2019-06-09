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

    private MainController mainController;


    void setUpGameLabel(Game game, MainController mainController) {
        this.game = game;
        this.mainController = mainController;
        gameNameLabel.setText(game.getName());
    }

    public void joinGame() {
        JoinGameRequest joinGameRequest = new JoinGameRequest(game.getId(), LoginController.getUserKey());
        joinGameRequest.sendRequest();
        mainController.setJoinedGame(game);
    }

    public void deleteGame() {
        DeleteGameRequest deleteGameRequest = new DeleteGameRequest(game.getId(), LoginController.getUserKey());
        deleteGameRequest.sendRequest();
        mainController.updateGameView();
    }

}
