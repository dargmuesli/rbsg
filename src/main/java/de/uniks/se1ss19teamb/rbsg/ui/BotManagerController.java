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
    private boolean deactivateNextSelection = false;
    private static boolean[] botSelectorContrainsBot;

    void setMaxPlayers(long maxPlayers) {
        this.maxPlayers = (int) maxPlayers;
        botSelectorContrainsBot = new boolean[(int) maxPlayers];
    }

    void setBotSelections() {
        for (int i = 0; i < maxPlayers ; i++) {
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

    /**
     * Creates a bot.
     *
     * @param numberOfBot            The bot's number.
     * @param difficulty             The bot's difficulty.
     * @param botSelectionController The selection controller to add this bot to.
     */
    void createBot(int numberOfBot, int difficulty, BotSelectionController botSelectionController) {
        BotControl.setGameId(GameSelectionController.joinedGame.getId());
        BotControl.createBotUser(numberOfBot, difficulty, botSelectionController);
        botSelectorContrainsBot[numberOfBot] = true;

        if (botSelectionControllers.size() > numberOfBot + 1) {
            botSelectionControllers.get(numberOfBot + 1).activateCheckbox();
        }
    }

    /**
     * Deactivates a bot with the given number.
     *
     * @param number The number of the bot to deactivate.
     */
    void deactivateBot(int number) {
        botSelectorContrainsBot[number] = false;
        BotControl.deactivateBotUser(number);

        if (!botSelectorContrainsBot[number + 1]) {
            if (botSelectionControllers.size() > number + 1) {
                BotSelectionController botSelectionController = botSelectionControllers.get(number + 1);

                if (botSelectionController != null) {
                    botSelectionController.deactivateCheckbox();
                }
            }
        }
    }
}
