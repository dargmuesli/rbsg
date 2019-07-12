package de.uniks.se1ss19teamb.rbsg.model.units;

import java.util.ArrayList;

// This class shows the build of a unit as is given by the server.

public interface IUnit {

    String getType();

    void setType(String type);

    int getMp();

    void setMp(int mp);

    int getHp();

    void setHp(int hp);

    ArrayList<String> getCanAttack();

    void setCanAttack(ArrayList<String> canAttack);

    String toString();
}
