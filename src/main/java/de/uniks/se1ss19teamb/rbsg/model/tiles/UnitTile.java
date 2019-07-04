package de.uniks.se1ss19teamb.rbsg.model.tiles;

public class UnitTile extends AbstractTile {
    private String type;
    private int mp;
    private int hp;
    private String[] canAttack;
    private String game;
    private String leader;
    private String position;

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
}
