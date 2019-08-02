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
    private Button btnDecrease;
    @FXML
    private Button btnIncrease;
    @FXML
    private HBox hbxRoot;
    @FXML
    private Label labelUnitCount;
    @FXML
    private Label labelUnitType;
    @FXML
    private Pane imageUnitType;

    private Unit unit;
    private int count = 0;

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{hbxRoot}));
    }

    void setUpUnitObject(Unit unit) {
        this.unit = unit;

        labelUnitType.setText(unit.getType());
        imageUnitType.getChildren().add(TextureManager.getTextureInstance(unit.getType()));

        update(0);
    }

    Unit getUnit() {
        return this.unit;
    }

    public void increaseCount() {
        ArmyManagerController.getInstance().discardConfirmation = false;
        ArmyManagerController.army.getUnits().add(0, unit);
        ArmyManagerController.getInstance().updateUnits();
    }

    public void decreaseCount() {
        ArmyManagerController.getInstance().discardConfirmation = false;
        ArmyManagerController.army.getUnits().remove(unit);
        ArmyManagerController.getInstance().updateUnits();
    }

    void update(int count) {
        this.count = count;
        labelUnitCount.setText(Integer.toString(count));

        if (ArmyManagerController.army == null || ArmyManagerController.army.getUnits() == null) {
            return;
        }

        if (ArmyManagerController.army.getUnits().size() == 10) {
            btnIncrease.setDisable(true);

            if (this.count > 0) {
                btnDecrease.setDisable(false);
            } else {
                btnDecrease.setDisable(true);
            }
        } else if (ArmyManagerController.army.getUnits().size() == 0) {
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
            } else {
                btnDecrease.setDisable(true);
            }
        }
    }
}
