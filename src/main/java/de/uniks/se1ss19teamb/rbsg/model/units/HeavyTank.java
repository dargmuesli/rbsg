package de.uniks.se1ss19teamb.rbsg.model.units;

import de.uniks.se1ss19teamb.rbsg.model.Unit;

import java.util.ArrayList;
import java.util.Arrays;

public class HeavyTank extends Unit {
    private String id = "5cc051bd62083600017db3ba";
    private String type ="Heavy Tank";
    private int mp = 4;
    private int hp = 10;
    private ArrayList<String> canAttack = new ArrayList<>
        (Arrays.asList("Infantry", "Bazooka Trooper", "Jeep", "Light Tank", "Heavy Tank", "Chopper"));
}
