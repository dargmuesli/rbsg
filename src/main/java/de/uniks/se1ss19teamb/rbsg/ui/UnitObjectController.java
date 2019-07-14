package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import java.util.Arrays;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


public class UnitObjectController {

    @FXML
    private HBox root;
    @FXML
    private Label labelUnitType;
    @FXML
    private Label labelUnitCount;
    @FXML
    private Button btnIncrease;
    @FXML
    private Button btnDecrease;
    @FXML
    private Pane imageUnitType;

    private Unit unit;
    private int count = 0;

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{root}));
    }

    void setUpUnitObject(Unit unit) {
        this.unit = unit;
        labelUnitType.setText(unit.getType());
        ArmyManagerController.getInstance().updateCounts();
        btnDecrease.setDisable(true);
        imageUnitType.getChildren().add(TextureManager.getTextureInstance(unit.getType()));
    }

    Unit getUnit() {
        return this.unit;
    }

    void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void increaseCount() {
        count++;
        ArmyManagerController.currentArmy.getUnits().add(0, unit);
        btnDecrease.setDisable(false);

        if (ArmyManagerController.currentArmy.getUnits().size() == ArmyManagerController.MAXIMUM_UNIT_COUNT) {
            btnIncrease.setDisable(true);
        }

        ArmyManagerController.getInstance().updateCounts();
    }

    public void decreaseCount() {
        count--;
        ArmyManagerController.currentArmy.getUnits().remove(unit);
        btnIncrease.setDisable(false);

        if (count == 0) {
            btnDecrease.setDisable(true);
        }

        ArmyManagerController.getInstance().updateCounts();
    }

    void update(int count) {
        this.count = count;
        labelUnitCount.setText(Integer.toString(count));

        if (ArmyManagerController.currentArmy.getUnits().size() == 10) {
            btnIncrease.setDisable(true);

            if (this.count > 0) {
                btnDecrease.setDisable(false);
            } else {
                btnDecrease.setDisable(true);
            }
        } else if (ArmyManagerController.currentArmy.getUnits().size() == 0) {
            btnDecrease.setDisable(true);

            if (this.count == 0) {
                btnIncrease.setDisable(false);
            } else {
                btnIncrease.setDisable(true);
            }
        } else {
            btnIncrease.setDisable(false);

            if (this.count > 0) {
                btnDecrease.setDisable(false);
            }
        }
    }
}
