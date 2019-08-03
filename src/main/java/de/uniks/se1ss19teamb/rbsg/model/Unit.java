package de.uniks.se1ss19teamb.rbsg.model;

import java.util.ArrayList;

/**
 * A model for units as given by the gameserver.
 */
public class Unit {

    private String id;
    private String type;
    private int mp;
    private int hp;
    private ArrayList<String> canAttack;

    /**
     * Constructor for an unit with default field values.
     */
    public Unit() {
    }

    /**
     * Constructor for an unit with an individual id and default values for all other fields.
     *
     * @param id The unit's id.
     */
    public Unit(String id) {
        this.id = id;
    }

    /**
     * Constructor for an unit with individual field values.
     *
     * @param id        The unit's id.
     * @param type      The unit's type.
     * @param mp        The unit's move points.
     * @param hp        The unit's health points.
     * @param canAttack A list of unit string representations that the created unit will be able to attack.
     */
    public Unit(String id, String type, int mp, int hp, ArrayList<String> canAttack) {
        this.id = id;
        this.type = type;
        this.mp = mp;
        this.hp = hp;
        this.canAttack = canAttack;
    }

    /**
     * Standard getter.
     *
     * @return The unit's id.
     */
    public String getId() {
        return id;
    }

    /**
     * Standard setter.
     *
     * @param id The unit's id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Standard getter.
     *
     * @return The unit's type.
     */
    public String getType() {
        return type;
    }

    /**
     * Standard setter.
     *
     * @param type The unit's type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Standard getter.
     *
     * @return The unit's move points.
     */
    public int getMp() {
        return mp;
    }

    /**
     * Standard setter.
     *
     * @param mp The unit's move points.
     */
    public void setMp(int mp) {
        this.mp = mp;
    }

    /**
     * Standard getter.
     *
     * @return The unit's health points.
     */
    public int getHp() {
        return hp;
    }

    /**
     * Standard setter.
     *
     * @param hp The unit's health points.
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Standard getter.
     *
     * @return The unit's list of unit string representations that the created unit will be able to attack.
     */
    public ArrayList<String> getCanAttack() {
        return canAttack;
    }

    /**
     * Standard setter.
     *
     * @param canAttack The unit's list of unit string representations that the created unit will be able to attack.
     */
    public void setCanAttack(ArrayList<String> canAttack) {
        this.canAttack = canAttack;
    }

    /**
     * Standard {@code toString()} override.
     *
     * @return A string in the format of {@code Unit{id=<id>, type=<type>, mp=<mp>, hp=<hp>, canAttack=<canAttack>}}.
     */
    @Override
    public String toString() {
        return "Unit{"
            + "id='" + id + "'"
            + ", type='" + type + "'"
            + ", mp=" + mp
            + ", hp=" + hp
            + ", canAttack=" + canAttack
            + '}';
    }
}
