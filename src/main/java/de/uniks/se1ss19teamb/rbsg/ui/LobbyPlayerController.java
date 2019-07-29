package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;

public class LobbyPlayerController {

    @FXML
    private Label lblPlayerName;

    private InGamePlayer inGamePlayer;

    void setInGamePlayer(InGamePlayer inGamePlayer) {
        this.inGamePlayer = inGamePlayer;
        this.lblPlayerName.setText(inGamePlayer.getName());

        List<String> knownPlayerColors = new ArrayList<>();
        knownPlayerColors.add("RED");

        if (!knownPlayerColors.contains(this.inGamePlayer.getColor())) {
            NotificationHandler.getInstance()
                .sendError("Unknown player color '" + this.inGamePlayer.getColor()
                        + "'. Please add a css class like 'inGamePlayerRED', the new color to the list in "
                        + "LobbyPlayerController and remove the helper method when four colors are known.",
                    LogManager.getLogger());
        }

        this.lblPlayerName.setId("inGamePlayer" + this.inGamePlayer.getColor());
    }
}
