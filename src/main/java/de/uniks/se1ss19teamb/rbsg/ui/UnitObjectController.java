package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.units.AbstractUnit;
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
    private Button btnDecrease;
    @FXML
    private Pane imageUnitType;

    private ArmyManagerController armyManagerController;
    private int count = 0;

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{root}));
    }

    void setUpUnitObject(AbstractUnit unit, ArmyManagerController armyManagerController) {
        this.armyManagerController = armyManagerController;
        labelUnitType.setText(unit.getType());
        updateCount();
        btnDecrease.setDisable(true);
        imageUnitType.getChildren().add(TextureManager.getTextureInstance(unit.getType()));
    }

    int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
        updateCount();
    }

    public void increaseCount() {
        if (count >= 0) {
            if (armyManagerController.getLeftUnits() > 0) {
                count++;
                armyManagerController.setLeftUnits(armyManagerController.getLeftUnits() - 1);
                btnDecrease.setDisable(false);
            }
        } else {
            count = 0;
        }

        updateCount();
    }

    public void decreaseCount() {
        if (count > 0) {
            count--;
            armyManagerController.setLeftUnits(armyManagerController.getLeftUnits() + 1);
            updateCount();
            if (count == 0) {
                btnDecrease.setDisable(true);
            }
        }

    }

    private void updateCount() {
        labelUnitCount.setText(Integer.toString(count));
    }
}
