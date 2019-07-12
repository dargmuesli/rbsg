package de.uniks.se1ss19teamb.rbsg.model.units;

import java.util.ArrayList;
import java.util.Arrays;

public class BazookaTrooper extends AbstractUnit {

    private String type = "Bazooka Trooper";
    private int mp = 2;
    private int hp = 10;
    private ArrayList<String> canAttack = new ArrayList<>(Arrays.asList("Jeep",
        "Light Tank", "Heavy Tank", "Chopper"));
}
