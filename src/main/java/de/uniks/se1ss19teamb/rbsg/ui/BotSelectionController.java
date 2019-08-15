package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import de.uniks.se1ss19teamb.rbsg.bot.BotUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class BotSelectionController {

    @FXML
    private JFXCheckBox botCheckbox;
    @FXML
    JFXSlider diffSlider;

    private int botNumber;

    private BotUser botUser;

    private boolean botCreated = false;

    BotManagerController botManagerController;

    public int getBotNumber() {
        return botNumber;
    }

    public void setBotManagerController(BotManagerController botManagerController) {
        this.botManagerController = botManagerController;
    }

    void setBotNumber(int botNumber) {
        this.botNumber = botNumber;
    }

    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(botCheckbox)) {
            if (botCheckbox.isSelected()) {
                if (!botCreated) {
                    double difficulty = diffSlider.getValue();
                    createBot((int) difficulty);
                    botCreated = true;
                }
            }
        }
    }

    private void setCheckBoxName(String botName) {
        botCheckbox.setText(botName);
    }

    public void setBot(BotUser botUser) {
        this.botUser = botUser;
        setCheckBoxName(botUser.getBotUserName());
    }


    private void createBot(int difficulty) {
        botManagerController.createBot(botNumber, difficulty, this);
    }
}
