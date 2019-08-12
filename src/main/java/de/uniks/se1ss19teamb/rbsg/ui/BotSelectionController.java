package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXCheckBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class BotSelectionController {

    @FXML
    private JFXCheckBox botCheckbox;

    private int botNumber;

    public int getBotNumber() {
        return botNumber;
    }

    public void setBotNumber(int botNumber) {
        this.botNumber = botNumber;
    }

    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(botCheckbox)) {
            changeBotActivation();
        }
    }

    private void changeBotActivation() {

    }
}
