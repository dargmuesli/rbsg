package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import com.sun.xml.internal.bind.v2.TODO;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
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
    private static int count = 0;
    public static TurnUiController instance;

    public static TurnUiController getInstance() {
        return instance;
    }

    public void initialize() {
        instance = this;
        getTurn("movePhase");
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

    public void getTurn(String phase) {
        if (phase == "attackPhase") {
            turnLabel.setText("attack phase");
        } else if (phase == "movePhase") {
            turnLabel.setText("move phase 1");
        } else if (phase == "lastMovePhase") {
            turnLabel.setText("move phase 2");
        }
    }
}
