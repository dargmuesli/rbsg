package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.ai.BotControl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class BotManagerController {

    @FXML
    private VBox botSelectionVBox;

    private int maxPlayers = 0;
    private ArrayList<BotSelectionController> botSelectionControllers = new ArrayList<>();

    void setMaxPlayers(long maxPlayers) {
        this.maxPlayers = (int) maxPlayers;
    }

    public void setBotSelections() {
        for (int i = 0; i < maxPlayers - 1; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/botSelection.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                BotSelectionController controller = fxmlLoader.getController();
                controller.setBotNumber(i);
                controller.setBotManagerController(this);
                botSelectionControllers.add(i, controller);
                botSelectionVBox.getChildren().add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createBot(int numberOfBot, BotSelectionController botSelectionController) {
        BotControl.setGameId(GameSelectionController.joinedGame.getId());
        BotControl.createBotUser(numberOfBot, botSelectionController);
        BotControl.setInGameController(InGameController.getInstance());
    }

    public BotSelectionController getBotSelectionController(int number) {
        return botSelectionControllers.get(number);
    }
}
