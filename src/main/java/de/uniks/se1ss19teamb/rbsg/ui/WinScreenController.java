package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;


public class WinScreenController {
    @FXML
    private ImageView trophyPic;
    @FXML
    private Label winnerLabel;

    private Boolean winnerFound = false;
    private String winner;

    public void initialize () {

    }

    public void checkWinner () {
        if (winnerFound) {
            trophyPic.setVisible(true);
            winnerLabel.setText(winner);
            winnerLabel.setVisible(true);
        }
    }

    private void setWinnerToLabel () {
        // winner = nameofWinner;
    }

    private void calculateWinner () {
        winnerFound = true;
    }
}
