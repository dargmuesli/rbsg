package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import de.uniks.se1ss19teamb.rbsg.bot.BotUser;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGameObject;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import java.util.Map;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;



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
        int numberOfPlayers = 0;

        for (Map.Entry<String, InGameObject> entry : InGameController.inGameObjects.entrySet()) {
            if (entry.getValue() instanceof InGamePlayer) {
                numberOfPlayers++;
            }
        }

        if (botCheckbox.isSelected()) {
            if (numberOfPlayers >= botManagerController.maxPlayers) {
                botCheckbox.setSelected(false);
                NotificationHandler.sendInfo("There is no space for a bot anymore.", LogManager.getLogger());
                return;
            }
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

    public void setCheckbox() {
        botCheckbox.setSelected(true);
    }
}
