package de.uniks.se1ss19teamb.rbsg.model;

import de.uniks.se1ss19teamb.rbsg.model.units.AbstractUnit;

import java.util.ArrayList;

public class UserArmy {

    private String id;
    private String name;
    private ArrayList<AbstractUnit> units;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<AbstractUnit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<AbstractUnit> units) {
        this.units = units;
    }

    public void initialize(Army army) {
        id = army.getId();
        name = army.getName();
    }
}
