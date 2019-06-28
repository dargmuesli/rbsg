package de.uniks.se1ss19teamb.rbsg.model;

public class InGameTile {
    private String id;
    private String game;
    private int x;
    private int y;
    private boolean isPassable;
    private String left;
    private String right;
    private String bottom;
    private String top;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getGame() {
        return game;
    }
    
    public void setGame(String game) {
        this.game = game;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public boolean isPassable() {
        return isPassable;
    }
    
    public void setPassable(boolean isPassable) {
        this.isPassable = isPassable;
    }
    
    public String getLeft() {
        return left;
    }
    
    public void setLeft(String left) {
        this.left = left;
    }
    
    public String getRight() {
        return right;
    }
    
    public void setRight(String right) {
        this.right = right;
    }
    
    public String getBottom() {
        return bottom;
    }
    
    public void setBottom(String bottom) {
        this.bottom = bottom;
    }
    
    public String getTop() {
        return top;
    }
    
    public void setTop(String top) {
        this.top = top;
    }
}
