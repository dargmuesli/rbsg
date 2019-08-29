package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.bot.BotControl;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class BotManagerController {

    @FXML
    private VBox botSelectionVBox;

    private int maxPlayers = 0;
    private ArrayList<BotSelectionController> botSelectionControllers = new ArrayList<>();

    void setMaxPlayers(long maxPlayers) {
        this.maxPlayers = (int) maxPlayers;
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void createBot(int numberOfBot, BotSelectionController botSelectionController) {
        BotControl.setGameId(GameSelectionController.joinedGame.getId());
        BotControl.createBotUser(numberOfBot, botSelectionController);
        BotControl.setInGameController(InGameController.getInstance());
    }

    public BotSelectionController getBotSelectionController(int number) {
        return botSelectionControllers.get(number);
    }
}
