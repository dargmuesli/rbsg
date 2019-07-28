package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class TurnUiController {

    @FXML
    private Line linePlayerOne;
    @FXML
    private Line linePlayerTwo;
    @FXML
    private Line linePlayerThree;
    @FXML
    private Line linePlayerFour;
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

    private FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
    public static TurnUiController instance;

    public static TurnUiController getInstance() {
        return instance;
    }

    public void initialize() {
        instance = this;
        setTurn("movePhase");
        players();
        underLining(labelOne, linePlayerOne);
        underLining(labelTwo, linePlayerTwo);
        underLining(labelThree, linePlayerThree);
        underLining(labelFour, linePlayerFour);
        phaseBtn.setTranslateY(-4);
    }

    private void underLining(Label label, Line line) {
        line.setEndX(fontLoader.computeStringWidth(label.getText(), label.getFont()));
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
