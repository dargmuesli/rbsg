package de.uniks.se1ss19teamb.rbsg.model;

import java.util.ArrayList;

public class Army {

    private String id = null;
    private String name = null;
    private ArrayList<String> units = null;

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

    public ArrayList<String> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<String> units) {
        this.units = units;
    }
}
