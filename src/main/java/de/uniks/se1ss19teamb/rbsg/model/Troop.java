package de.uniks.se1ss19teamb.rbsg.model;

import java.util.HashMap;
import java.util.Map;

public enum Troop {
    BAZOOKA_TROOPER("Bazooka Trooper"),
    CHOPPER("Chopper"),
    HEAVY_TANK("Heavy Tank"),
    INFANTRY("Infantry"),
    JEEP("Jeep"),
    LIGHT_TANK("Light Tank");

    private static Map<String, Troop> map = new HashMap<>();

    static {
        for (Troop troop : Troop.values()) {
            map.put(troop.name, troop);
        }
    }

    public final String name;

    Troop(final String name) {
        this.name = name;
    }

    public static Troop keyOf(String name) {
        return map.get(name);
    }
}
