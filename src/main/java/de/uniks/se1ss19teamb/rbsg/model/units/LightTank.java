package de.uniks.se1ss19teamb.rbsg.model.units;

import java.util.ArrayList;
import java.util.Arrays;

public class LightTank extends AbstractUnit {

    private String type = "Light Tank";
    private int mp = 6;
    private int hp = 10;
    private ArrayList<String> canAttack = new ArrayList<>(Arrays.asList("Infantry",
        "Bazooka Trooper", "Jeep", "Light Tank", "Heavy Tank"));
}
