package de.uniks.se1ss19teamb.rbsg.model;

import java.util.ArrayList;

// This class shows the build of a unit as is given by the server.

public class Unit {

    private String id;
    private String type;
    private String mp;
    private String hp;
    private ArrayList<String> canAttack;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMp() {
        return mp;
    }

    public void setMp(String mp) {
        this.mp = mp;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public ArrayList<String> getCanAttack() {
        return canAttack;
    }

    public void setCanAttack(ArrayList<String> canAttack) {
        this.canAttack = canAttack;
    }
}
