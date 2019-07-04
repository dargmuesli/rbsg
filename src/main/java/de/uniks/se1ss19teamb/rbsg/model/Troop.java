package de.uniks.se1ss19teamb.rbsg.model;

import java.util.HashMap;
import java.util.Map;

public enum Troop {
    BAZOOKA_TROOPER("5cc051bd62083600017db3b7"),
    CHOPPER("5cc051bd62083600017db3bb"),
    HEAVY_TANK("5cc051bd62083600017db3ba"),
    INFANTRY("5cc051bd62083600017db3b6"),
    JEEP("5cc051bd62083600017db3b8"),
    LIGHT_TANK("5cc051bd62083600017db3b9");

    private static Map<String, Troop> map = new HashMap<>();

    static {
        for (Troop troop : Troop.values()) {
            map.put(troop.id, troop);
        }
    }

    public final String id;

    Troop(final String id) {
        this.id = id;
    }

    public static Troop keyOf(String id) {
        return map.get(id);
    }
}
