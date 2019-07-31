package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LobbyPlayerController {

    @FXML
    private Label lblPlayerName;

    private InGamePlayer inGamePlayer;

    void setInGamePlayer(InGamePlayer inGamePlayer) {
        this.inGamePlayer = inGamePlayer;
        this.lblPlayerName.setText(inGamePlayer.getName());
        this.lblPlayerName.setId("inGamePlayer" + this.inGamePlayer.getColor());
    }
}
