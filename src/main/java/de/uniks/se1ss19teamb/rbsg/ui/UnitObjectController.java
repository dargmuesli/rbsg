package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UnitObjectController {

    private Unit unit;
    private UnitOverviewController unitOverviewController;

    @FXML
    Label labelUnitType;
    @FXML
    Label labelUnitCount;


    public void setUpUnitObject(Unit unit, UnitOverviewController UnitOverviewController) {
        this.unit = unit;
        this.unitOverviewController = unitOverviewController;
        labelUnitType.setText(unit.getType());

    }
}
