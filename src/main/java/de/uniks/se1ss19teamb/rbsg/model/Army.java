package de.uniks.se1ss19teamb.rbsg.model;

import java.util.ArrayList;

public class Army {

    private String id;
    private String name;
    private ArrayList<String> units;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getUnits() {
        return units;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnits(ArrayList<String> units) {
        this.units = units;
    }
}
