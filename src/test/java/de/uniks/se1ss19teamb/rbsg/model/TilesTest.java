package de.uniks.se1ss19teamb.rbsg.model;

import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import org.junit.Assert;
import org.junit.Test;


public class TilesTest {
    private EnvironmentTile eT = new EnvironmentTile();
    private UnitTile uT = new UnitTile();

    // EnvironmentTile parameters
    private String game = "TestGame";
    private int xPos = 1;
    private int yPos = 1;
    private boolean passable = true;
    private String left = "abc";
    private String right = "cba";
    private String bottom = "south";
    private String top = "north";

    // PlayerTile parameters
    private String name = "Player1";
    private String color = "GREEN";
    private String[] army = {"JEEP", "CHOPPER", "TANK"};

    // UnitTile parameters
    private String type = "TROOPER";
    private int mp = 20;
    private int hp = 10;
    private String[] canAttack = {"CHOPPER", "TROOPER", "JEEP", "BAZOOKA TROOPER"};
    private String position = "POSITION";

    @Test
    public void environmentTileTest() {
        eT.setGame(game);
        Assert.assertEquals(eT.getGame(), game);

        eT.setX(xPos);
        Assert.assertEquals(eT.getX(), xPos);

        eT.setY(yPos);
        Assert.assertEquals(eT.getY(), yPos);

        eT.setPassable(passable);
        Assert.assertEquals(eT.isPassable(), passable);

        eT.setLeft(left);
        Assert.assertEquals(eT.getLeft(), left);

        eT.setRight(right);
        Assert.assertEquals(eT.getRight(), right);

        eT.setBottom(bottom);
        Assert.assertEquals(eT.getBottom(), bottom);

        eT.setTop(top);
        Assert.assertEquals(eT.getTop(), top);
    }

    @Test
    public void unitTileTest() {
        uT.setType(type);
        Assert.assertEquals(uT.getType(), type);

        uT.setMp(mp);
        Assert.assertEquals(uT.getMp(), mp);

        uT.setHp(hp);
        Assert.assertEquals(uT.getHp(), hp);

        uT.setCanAttack(canAttack);
        Assert.assertArrayEquals(uT.getCanAttack(), canAttack);

        uT.setGame(game);
        Assert.assertEquals(uT.getGame(), game);

        uT.setLeader(name);
        Assert.assertEquals(uT.getLeader(), name);

        uT.setPosition(position);
        Assert.assertEquals(uT.getPosition(), position);
    }
}
