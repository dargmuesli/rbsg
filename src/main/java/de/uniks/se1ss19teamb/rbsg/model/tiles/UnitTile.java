package de.uniks.se1ss19teamb.rbsg.model.tiles;

import java.util.Arrays;

public class UnitTile extends AbstractTile {
    private String type;
    private int mp;
    private int mpLeft;
    private int hp;
    private String[] canAttack;
    private String game;
    private String leader;
    private String position;

    public UnitTile() {
    }

    public UnitTile(UnitTile unitTile) {
        this.setId(unitTile.getId());
        this.type = unitTile.getType();
        this.mp = unitTile.getMp();
        this.hp = unitTile.getHp();
        this.canAttack = Arrays.copyOf(unitTile.getCanAttack(), unitTile.getCanAttack().length);
        this.game = unitTile.getGame();
        this.leader = unitTile.getLeader();
        this.position = unitTile.getPosition();
        this.mpLeft = unitTile.getMpLeft();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String[] getCanAttack() {
        return canAttack;
    }

    public void setCanAttack(String[] canAttack) {
        this.canAttack = canAttack;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getMpLeft() {
        return mpLeft;
    }

    public void setMpLeft(int mpLeft) {
        this.mpLeft = mpLeft;
    }
}
