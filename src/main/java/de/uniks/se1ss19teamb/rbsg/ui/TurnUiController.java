package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class TurnUiController {
    @FXML
    private Label labelOne;
    @FXML
    private Label labelTwo;
    @FXML
    private Label labelThree;
    @FXML
    private Label labelFour;
    @FXML
    private JFXButton phaseBtn;
    @FXML
    private Label turnLabel;
    private ArrayList<InGamePlayer> inGamePlayerList = new ArrayList<>();

    public static TurnUiController instance;

    public static TurnUiController getInstance() {
        return instance;
    }

    public void initialize() {
        updatePlayers();
        labelOne.setStyle("-fx-text-fill: Red");
        instance = this;
        setTurn("movePhase");
        phaseBtn.setTranslateY(-4);
    }

    private void updatePlayers() {
        InGameController.inGameObjects.entrySet().stream()
            .filter(stringInGameObjectEntry -> stringInGameObjectEntry.getValue() instanceof InGamePlayer)
            .forEachOrdered(inGameObjectEntry -> {
                inGamePlayerList.add(((InGamePlayer)inGameObjectEntry.getValue()));
            });
        players();
    }

    @FXML
    private void setOnAction(ActionEvent event) {
        if (event.getSource().equals(phaseBtn)) {
            GameSocket.instance.nextPhase();
        }
    }

    private void players() {
        if (inGamePlayerList.size() == 2) {
            labelOne.setVisible(true);
            labelTwo.setVisible(true);
            labelOne.setText(inGamePlayerList.get(1).getName());
            labelTwo.setText(inGamePlayerList.get(0).getName());
        } else if (inGamePlayerList.size() == 4) {
            labelOne.setVisible(true);
            labelTwo.setVisible(true);
            labelThree.setVisible(true);
            labelFour.setVisible(true);
            labelOne.setText(inGamePlayerList.get(3).getName());
            labelTwo.setText(inGamePlayerList.get(2).getName());
            labelThree.setText(inGamePlayerList.get(1).getName());
            labelFour.setText(inGamePlayerList.get(0).getName());
        }
    }

    public void setTurn(String phase) {
        Platform.runLater(() -> {
            turnLabel.setText(phase);
        });
    }

    public void showTurn() {
        Platform.runLater(() -> {
            if (labelOne.getStyle() == "-fx-text-fill: Red") {
                labelTwo.setStyle("-fx-text-fill: Red");
                labelOne.setStyle("-fx-text-fill: #FFFF8d");
            } else if (labelTwo.getStyle() == "-fx-text-fill: Red") {
                labelOne.setStyle("-fx-text-fill: Red");
                labelTwo.setStyle("-fx-text-fill: #FFFF8d");
            }
        });
    }
}
