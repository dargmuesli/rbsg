package de.uniks.se1ss19teamb.rbsg.ui;


import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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

    private Unit unit;

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{unitConfig}));

    }

    void loadConfig(Unit unit) {
        this.unit = unit;
        Pane textureInstance = TextureManager.getTextureInstance(unit.getType());
        imageUnitType.getChildren().add(textureInstance);
    }

    void loadNumberOfUnit(Army currentArmy, int numberArmy) {
        int counter = 0;
        for (int i = 0; i < currentArmy.getUnits().size(); i++) {
            if (unit.getType().equals(currentArmy.getUnits().get(i).getType())) {
                counter++;
            }
        }

        switch (numberArmy) {
            case 1:
                labelUnitSave1.setText(String.valueOf(counter));
                break;
            case 2:
                labelUnitSave2.setText(String.valueOf(counter));
                break;
            case 3:
                labelUnitSave3.setText(String.valueOf(counter));
                break;
            default:
                break;
        }

    }

}

