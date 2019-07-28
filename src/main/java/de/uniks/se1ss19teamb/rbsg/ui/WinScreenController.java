package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;


public class WinScreenController {
    @FXML
    private ImageView trophyPic;
    @FXML
    private Label winnerLabel;
    
    public static WinScreenController instance;

    public static WinScreenController getInstance() {
        return instance;
    }

    public void initialize () {
        instance = this;
    }

    public void checkWinner (String winner) {
            trophyPic.setVisible(true);
            winnerLabel.setText(winner);
            winnerLabel.setVisible(true);
    }

}
