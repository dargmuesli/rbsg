package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
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

    public static TurnUiController instance;

    public static TurnUiController getInstance() {
        return instance;
    }

    public void initialize() {
        labelOne.setStyle("-fx-text-fill: Red");
        instance = this;
        setTurn("movePhase");
        players();
        phaseBtn.setTranslateY(-4);
    }

    @FXML
    private void setOnAction(ActionEvent event) {
        if (event.getSource().equals(phaseBtn)) {
            GameSocket.instance.nextPhase();
        }
    }

    private void players() {
        //implementation of playernames needed.
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
