package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameFieldController {

    @FXML
    private Label gameNameLabel;

    @FXML
    private Button joinGameButton;

    @FXML
    private Button deleteGameButton;

    private Game game;


    public void setUpGameLabel(Game game) {
        this.game = game;
        gameNameLabel.setText(game.getName());
    }

    public void joinGame() {

    }

}
