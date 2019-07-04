package de.uniks.se1ss19teamb.rbsg.model.tiles;

public class PlayerTile extends AbstractTile {
    private String name;
    private String color;
    private String currentGame;
    private String[] army;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(String currentGame) {
        this.currentGame = currentGame;
    }

    public String[] getArmy() {
        return army;
    }

    public void setArmy(String[] army) {
        this.army = army;
    }
}
