package de.uniks.se1ss19teamb.rbsg.model;

import java.util.ArrayList;

public class UserArmy {

    private String id;
    private String name;
    private ArrayList<Unit> units;

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

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public void initialize(Army army) {
        id = army.getId();
        name = army.getName();
    }
}
