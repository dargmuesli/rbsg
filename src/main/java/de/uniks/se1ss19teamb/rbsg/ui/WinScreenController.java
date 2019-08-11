package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;



public class WinScreenController {
    @FXML
    private ImageView trophyPic;
    @FXML
    private Label winnerLabel;
    @FXML
    private JFXButton backBtn;

    public ArrayList<InGamePlayer> inGamePlayerList = new ArrayList<>();

    private static WinScreenController instance;

    public static WinScreenController getInstance() {
        return instance;
    }

    public void initialize() {
        instance = this;
        updatePlayers();
    }

    public void updatePlayers() {
        // fills inGamePlayerList with InGamePlayers
        InGameController.inGameObjects.entrySet().stream()
            .filter(stringInGameObjectEntry -> stringInGameObjectEntry.getValue() instanceof InGamePlayer)
            .forEachOrdered(inGameObjectEntry -> inGamePlayerList.add((InGamePlayer)inGameObjectEntry.getValue()));
    }

    public void calculateWinner(String winner) {
        for (InGamePlayer player: inGamePlayerList) {
            if (player.getId().equals(winner)) {
                winnerLabel.setText(player.getName());
            }
        }
    }

    public void setWinningScreen(String winner) {
        Platform.runLater(() -> {
            InGameController.instance.vBoxWinScreen.setVisible(true);
            calculateWinner(winner);
        });
    }

    @FXML
    private void setOnAction(ActionEvent event) {
        if (event.getSource().equals(backBtn)) {
            GameSocket.instance.leaveGame();
            GameSocket.instance.disconnect();
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", InGameController.getInstance().winScreenPane);
        }
    }

}
