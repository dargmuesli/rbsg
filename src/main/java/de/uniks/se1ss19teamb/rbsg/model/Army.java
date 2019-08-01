package de.uniks.se1ss19teamb.rbsg.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A model for armies.
 */
public class Army {
    private String id = "";
    private String name = "";
    private List<Unit> units = new ArrayList<>();

    public Army() {
    }

    public Army(String id, String name, List<Unit> units) {
        this.id = id;
        this.name = name;
        this.units = units;
    }

    public Army(Army army) {
        this.id = army.getId();
        this.name = army.getName();
        this.units = new ArrayList<>(army.getUnits());
    }

    /**
     * Standard getter.
     *
     * @return The army's id.
     */
    public String getId() {
        return id;
    }

    /**
     * Standard setter.
     *
     * @param id The army's id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Standard getter.
     *
     * @return The army's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Standard setter.
     *
     * @param name The army's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Standard getter.
     *
     * @return The army's units.
     */
    public List<Unit> getUnits() {
        return units;
    }

    /**
     * Standard setter.
     *
     * @param units The army's units.
     */
    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    /**
     * Standard {@code toString()} override.
     *
     * @return The army's name.
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
