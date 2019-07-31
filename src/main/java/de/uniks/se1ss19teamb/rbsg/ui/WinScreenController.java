package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.ArrayList;


public class WinScreenController {
    @FXML
    private ImageView trophyPic;
    @FXML
    private Label winnerLabel;
    @FXML
    private JFXButton backBtn;

    ArrayList<InGamePlayer> inGamePlayerList = new ArrayList<>();

    public static WinScreenController instance;

    public static WinScreenController getInstance() {
        return instance;
    }

    public void initialize() {
        instance = this;
        updatePlayer();
    }

    private void updatePlayer() {
        InGameController.inGameObjects.entrySet().stream()
            .filter(stringInGameObjectEntry -> stringInGameObjectEntry.getValue() instanceof InGamePlayer)
            .forEachOrdered(inGameObjectEntry -> {
                inGamePlayerList.add((InGamePlayer)inGameObjectEntry.getValue());
            });
    }

    private void calculateWinner(String winner) {
        for (InGamePlayer player: inGamePlayerList) {
            if (player.getId() == winner) {
                winnerLabel.setText(player.getName());
            } else {
                return;
            }
        }
    }

    public void checkWinner(String winner) {
        Platform.runLater(() -> {
            InGameController.instance.winScreenPane.setVisible(true);
            trophyPic.setVisible(true);
            calculateWinner(winner);
            winnerLabel.setVisible(true);
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
