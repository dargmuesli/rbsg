package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;

public class UnitConfigController {
    @FXML
    private VBox unitConfig;

    @FXML
    private Pane imageUnitType;

    @FXML
    private Label labelUnitSave1;

    @FXML
    private Label labelUnitSave2;

    @FXML
    private Label labelUnitSave3;

    private GameLobbyController gameLobbyController;
    private int count = 0;

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{unitConfig}));

    }

    void loadConfig(Unit unit, GameLobbyController gameLobbyController) {
        this.gameLobbyController = gameLobbyController;
        imageUnitType.getChildren().add(TextureManager.getTextureInstance(unit.getType()));
    }


    void showNumber(Unit unit) {
        int counter = 0;
        for (int i = 0; i < gameLobbyController.army.getUnits().size(); i++) {
            if (unit.getId().equals(gameLobbyController.army.getUnits().get(0))) {
                counter++;
                labelUnitSave1.setText(Integer.toString(counter));
            }
        }
    }



    void numberUnits(Army army, ArrayList<Unit> units) {
            for (int i = 0; i < army.getUnits().size(); i++) {
                if (units.get(0).getId().equals(army.getUnits().get(i))) {
                    count++;
                    labelUnitSave1.setText(Integer.toString(count));
                }
                if (units.get(1).getId().equals(army.getUnits().get(i))) {

                    labelUnitSave1.setText(Integer.toString(count));
                }
        }
    }


}

