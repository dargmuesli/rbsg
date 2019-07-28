package de.uniks.se1ss19teamb.rbsg.ui;

import animatefx.animation.Bounce;
import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

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
    private VBox vBoxOne;
    @FXML
    private VBox vBoxTwo;
    @FXML
    private VBox vBoxThree;
    @FXML
    private VBox vBoxFour;
    @FXML
    private Label turnLabel;

    public static TurnUiController instance;

    public static TurnUiController getInstance() {
        return instance;
    }

    public void initialize() {
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
        vBoxOne.setVisible(true);
        vBoxTwo.setVisible(true);
        vBoxThree.setVisible(true);
        vBoxFour.setVisible(true);
        //implementation of playernames needed.
    }

    public void setTurn(String phase) {
        Platform.runLater(() -> {
            turnLabel.setText(phase);
        });
    }
}
