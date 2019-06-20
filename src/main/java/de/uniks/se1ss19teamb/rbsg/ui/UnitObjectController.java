package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UnitObjectController {

    private Unit unit;
    private ArmyManagerController armyManagerController;
    private int count = 0;

    @FXML
    Label labelUnitType;
    @FXML
    Label labelUnitCount;
    @FXML
    Button btnIncrease;
    @FXML
    Button btnDecrease;


    void setUpUnitObject(Unit unit, ArmyManagerController armyManagerController) {
        this.unit = unit;
        this.armyManagerController = armyManagerController;
        labelUnitType.setText(unit.getType());
        updateCount();
        btnDecrease.setDisable(true);

    }

    public void setCount(int count) {
        this.count = count;
        updateCount();
    }

    public int getCount() {
        return count;
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
