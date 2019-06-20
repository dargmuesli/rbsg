package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UnitObjectController {

    private Unit unit;
    private UnitOverviewController unitOverviewController;
    private int count = 0;

    @FXML
    Label labelUnitType;
    @FXML
    Label labelUnitCount;
    @FXML
    Button btnIncrease;
    @FXML
    Button btnDecrease;


    void setUpUnitObject(Unit unit, UnitOverviewController UnitOverviewController) {
        this.unit = unit;
        this.unitOverviewController = unitOverviewController;
        labelUnitType.setText(unit.getType());
       updateCount();

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
            count++;
        } else {
            count = 0;
        }

        updateCount();
    }

    public void decreaseCount() {
        if (count > 0) {
            count--;
            updateCount();
        }

    }

    private void updateCount() {
        labelUnitCount.setText(Integer.toString(count));
    }
}
