package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXCheckBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class BotSelectionController {

    @FXML
    private JFXCheckBox botCheckbox;

    private int botNumber;

    private boolean botCreated = false;

    BotManagerController botManagerController;

    public int getBotNumber() {
        return botNumber;
    }

    public void setBotManagerController(BotManagerController botManagerController) {
        this.botManagerController = botManagerController;
    }

    public void setBotNumber(int botNumber) {
        this.botNumber = botNumber;
    }

    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(botCheckbox)) {
            if (botCheckbox.isSelected()) {
                if (!botCreated) {
                    createBot();
                }
            }
        }
    }

    private void createBot() {
        botManagerController.createBot(botNumber);
    }
}
