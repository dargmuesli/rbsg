package de.uniks.se1ss19teamb.rbsg.model.units;

import java.util.ArrayList;

// This class shows the build of a unit as is given by the server.

public abstract class AbstractUnit implements IUnit {

    private String type;
    private int mp;
    private int hp;
    private ArrayList<String> canAttack;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public ArrayList<String> getCanAttack() {
        return canAttack;
    }

    public void setCanAttack(ArrayList<String> canAttack) {
        this.canAttack = canAttack;
    }

    @Override
    public String toString() {
        return "AbstractUnit{"
            + ", type='" + type + '\''
            + ", mp=" + mp
            + ", hp=" + hp
            + ", canAttack=" + canAttack
            + '}';
    }
}
