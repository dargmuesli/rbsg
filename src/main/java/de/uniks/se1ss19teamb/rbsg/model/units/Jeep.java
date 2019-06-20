package de.uniks.se1ss19teamb.rbsg.model.units;

import de.uniks.se1ss19teamb.rbsg.model.Unit;

import java.util.ArrayList;
import java.util.Arrays;

public class Jeep extends Unit {

    private String id = "5cc051bd62083600017db3b8";
    private String type = "Jeep";
    private int mp = 8;
    private int hp = 10;
    private ArrayList<String> canAttack = new ArrayList<>(Arrays.asList("Infantry",
        "Bazooka Trooper", "Jeep"));

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int getMp() {
        return mp;
    }

    @Override
    public void setMp(int mp) {
        this.mp = mp;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public ArrayList<String> getCanAttack() {
        return canAttack;
    }

    @Override
    public void setCanAttack(ArrayList<String> canAttack) {
        this.canAttack = canAttack;
    }
}
