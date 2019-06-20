package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.Unit;

public class UnitObjectController {

    private Unit unit;
    UnitOverviewController unitOverviewController;

    public void setUpUnitObject(Unit unit, UnitOverviewController UnitOverviewController) {
        this.unit = unit;
        this.unitOverviewController = unitOverviewController;

    }
}
