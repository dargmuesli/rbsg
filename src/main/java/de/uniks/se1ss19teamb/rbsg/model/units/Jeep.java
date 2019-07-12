package de.uniks.se1ss19teamb.rbsg.model.units;

import java.util.ArrayList;
import java.util.Arrays;

public class Jeep extends AbstractUnit {

    private String type = "Jeep";
    private int mp = 8;
    private int hp = 10;
    private ArrayList<String> canAttack = new ArrayList<>(Arrays.asList("Infantry",
        "Bazooka Trooper", "Jeep"));
}
