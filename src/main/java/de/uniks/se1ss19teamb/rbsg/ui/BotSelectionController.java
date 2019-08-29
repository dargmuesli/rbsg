package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXCheckBox;
import de.uniks.se1ss19teamb.rbsg.bot.BotUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class BotSelectionController {

    @FXML
    private JFXCheckBox botCheckbox;

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

    /**
     * Creates a bot when the checkbox is selected and a bot does not exist already.
     */
    public void check() {
        if (botCheckbox.isSelected()) {
            if (!botCreated) {
                createBot();
                botCreated = true;
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


    private void createBot() {
        botManagerController.createBot(botNumber, this);
    }
}
