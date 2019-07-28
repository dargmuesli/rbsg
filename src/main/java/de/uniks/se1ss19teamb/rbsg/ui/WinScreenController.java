package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
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

    public static WinScreenController instance;

    public static WinScreenController getInstance() {
        return instance;
    }

    public void initialize() {
        instance = this;
    }

    public void checkWinner(String winner) {
        Platform.runLater(() -> {
            InGameController.instance.winScreenPane.setVisible(true);
            trophyPic.setVisible(true);
            winnerLabel.setText(winner);
            winnerLabel.setVisible(true);
        });
    }

    @FXML
    private void setOnAction(ActionEvent event) {
        if (event.getSource().equals(backBtn)) {
            GameSocket.instance.leaveGame();
            GameSocket.instance.disconnect();
            MainController.setInGameChat(false);
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", InGameController.getInstance().winScreenPane);
        }
    }

}
