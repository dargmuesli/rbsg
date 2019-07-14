package de.uniks.se1ss19teamb.rbsg.ui;

import animatefx.animation.AnimationFX;
import animatefx.animation.Bounce;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class TurnUIController {

    @FXML
    Line linePlayerOne;
    @FXML
    Line linePlayerTwo;
    @FXML
    Line linePlayerThree;
    @FXML
    Line linePlayerFour;
    @FXML
    Label labelOne;
    @FXML
    Label labelTwo;
    @FXML
    Label labelThree;
    @FXML
    Label labelFour;

    private ArrayList<Label> labels = new ArrayList<>();

    public void initialize() {
        labelOne.setText("jdbnbnfvodjfn");
        underLining(labelOne, linePlayerOne);
        underLining(labelTwo, linePlayerTwo);
        underLining(labelThree, linePlayerThree);
        underLining(labelFour, linePlayerFour);
    }

    private void underLining(Label label, Line line) {
        line.setEndX(label.getText().length() * 7);
    }
}
