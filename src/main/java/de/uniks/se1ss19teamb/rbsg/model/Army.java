package de.uniks.se1ss19teamb.rbsg.model;

import java.util.List;

/**
 * A model for armies.
 */
public class Army {
    private String id;
    private String name;
    private List<String> units;

    public Army(String id, String name, List<String> units) {
        this.id = id;
        this.name = name;
        this.units = units;
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
    public List<String> getUnits() {
        return units;
    }

    /**
     * Standard setter.
     *
     * @param units The army's units.
     */
    public void setUnits(List<String> units) {
        this.units = units;
    }
}
