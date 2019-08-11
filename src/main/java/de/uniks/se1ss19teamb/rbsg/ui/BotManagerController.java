package de.uniks.se1ss19teamb.rbsg.ui;

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
        for (int i = 0; i < maxPlayers; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/botSelection.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                BotSelectionController controller = fxmlLoader.getController();
                botSelectionControllers.add(i, controller);
                botSelectionVBox.getChildren().add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
