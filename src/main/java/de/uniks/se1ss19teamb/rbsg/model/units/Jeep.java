package de.uniks.se1ss19teamb.rbsg.model.units;

import de.uniks.se1ss19teamb.rbsg.model.Unit;

import java.util.ArrayList;
import java.util.Arrays;

public class Jeep extends Unit {

    private String id = "5cc051bd62083600017db3b8";
    private String type ="Jeep";
    private int mp = 8;
    private int hp = 10;
    private ArrayList<String> canAttack = new ArrayList<>
        (Arrays.asList("Infantry", "Bazooka Trooper", "Jeep"));

}
