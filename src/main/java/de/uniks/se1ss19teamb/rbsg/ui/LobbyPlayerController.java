package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LobbyPlayerController {

    @FXML
    private Label lblPlayerName;
    @FXML
    private Circle crclReadiness;

    void setInGamePlayer(InGamePlayer inGamePlayer) {
        this.lblPlayerName.setText(inGamePlayer.getName());
        this.lblPlayerName.setId("inGamePlayer" + inGamePlayer.getColor());
        this.crclReadiness.setFill(inGamePlayer.isReady() ? Color.GREEN : Color.RED);
    }
}
