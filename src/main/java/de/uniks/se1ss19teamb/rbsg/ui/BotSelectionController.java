package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import de.uniks.se1ss19teamb.rbsg.bot.BotUser;
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

    /**
     * Creates a bot when the checkbox is selected and a bot does not exist already.
     */
    public void check() {
        if (botCheckbox.isSelected()) {
            if (!botCreated) {
                double difficulty = diffSlider.getValue();
                createBot((int) difficulty);
                botCreated = true;
            }
        } else {
            deactivateBot();
            botCreated = false;
        }
    }

    private void deactivateBot() {
        botManagerController.deactivateBot(botNumber);
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
