package de.uniks.se1ss19teamb.rbsg.model.units;

import java.util.ArrayList;
import java.util.Arrays;

public class Infantry extends AbstractUnit {

    private String type = "Infantry";
    private int mp = 3;
    private int hp = 10;
    private ArrayList<String> canAttack = new ArrayList<>(Arrays.asList("Infantry",
        "Bazooka Trooper", "Jeep", "Light Tank", "Heavy Tank"));
}
