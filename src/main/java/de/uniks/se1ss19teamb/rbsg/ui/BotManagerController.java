package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.bot.BotControl;
import de.uniks.se1ss19teamb.rbsg.bot.BotUser;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class BotManagerController {

    @FXML
    private VBox botSelectionVBox;

    int maxPlayers = 0;
    private ArrayList<BotSelectionController> botSelectionControllers = new ArrayList<>();
    boolean deactivateNextSelection = false;
    static boolean[] botSelectorContrainsBot;

    void setMaxPlayers(long maxPlayers) {
        this.maxPlayers = (int) maxPlayers;
        botSelectorContrainsBot = new boolean[(int) maxPlayers];
    }

    void setBotSelections() {
        for (int i = 0; i < maxPlayers - 1; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/botSelection.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                BotSelectionController controller = fxmlLoader.getController();
                botSelectionControllers.add(i, controller);
                botSelectionVBox.getChildren().add(parent);
                controller.setBotNumber(i);
                controller.setBotManagerController(this);
                if (deactivateNextSelection) {
                    controller.deactivateCheckbox();
                }
                BotUser bot = BotControl.getBotUser(i);
                if (bot != null) {
                    controller.setBot(bot);
                    controller.setCheckbox();
                } else {
                    deactivateNextSelection = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //TODO: fix javadoc
    /**
     * Javadoc for checkstyle.
     *
     */

    public void createBot(int numberOfBot, int difficulty, BotSelectionController botSelectionController) {
        BotControl.setGameId(GameSelectionController.joinedGame.getId());
        BotControl.createBotUser(numberOfBot, difficulty, botSelectionController);
        botSelectorContrainsBot[numberOfBot] = true;

        if (botSelectionControllers.size() > numberOfBot + 1) {
            botSelectionControllers.get(numberOfBot + 1).activateCheckbox();
        }
    }

    //TODO: fix checkstyle

    /**
     * javadoc for checkstyle.
     *
     */

    public void deactivateBot(int number) {
        botSelectorContrainsBot[number] = false;
        BotControl.deactivateBotUser(number);
        if (botSelectorContrainsBot[number + 1] == false) {
            if (botSelectionControllers.size() > number + 1) {
                BotSelectionController botSelectionController = botSelectionControllers.get(number + 1);
                if (botSelectionController != null) {
                    botSelectionController.deactivateCheckbox();
                }
            }
        }
    }
}
