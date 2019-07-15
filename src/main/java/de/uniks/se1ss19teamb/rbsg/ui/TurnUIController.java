package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class TurnUIController {

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

    public void initialize() {
        underLining(labelOne, linePlayerOne);
        underLining(labelTwo, linePlayerTwo);
        underLining(labelThree, linePlayerThree);
        underLining(labelFour, linePlayerFour);
    }

    private void underLining(Label label, Line line) {
        line.setEndX(label.getText().length() * 7);
    }
}
