package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.Game;
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

    private Game game;
    static Game joinedGame;


    void setUpGameLabel(Game game) {
        this.game = game;
        gameNameLabel.setText(game.getName());
    }

    public void joinGame() {
        JoinGameRequest joinGameRequest = new JoinGameRequest(game.getId(), LoginController.getUserKey());
        joinGameRequest.sendRequest();
        joinedGame = game;
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/inGame.fxml", root);
    }

    public void deleteGame() {
        DeleteGameRequest deleteGameRequest = new DeleteGameRequest(game.getId(), LoginController.getUserKey());
        deleteGameRequest.sendRequest();
        MainController.instance.updateGameView();
    }

}
