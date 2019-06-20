package de.uniks.se1ss19teamb.rbsg.model.units;

import de.uniks.se1ss19teamb.rbsg.model.Unit;

import java.util.ArrayList;
import java.util.Arrays;

public class BazookaTrooper extends Unit {

    private String id = "5cc051bd62083600017db3b7";
    private String type ="Bazooka Trooper";
    private int mp = 2;
    private int hp = 10;
    private ArrayList<String> canAttack = new ArrayList<>
        (Arrays.asList("Jeep", "Light Tank", "Heavy Tank", "Chopper"));

}
