package de.uniks.se1ss19teamb.rbsg.textures;


public enum TextureFancyOverlayPosition {
    TOP_LEFT(-1, -1, 0, 0),
    TOP_RIGHT(1, -1, 32, 0), 
    BOTTOM_LEFT(-1, 1, 0, 32),
    BOTTOM_RIGHT(1, 1, 32, 32);
    
    public int x;
    public int y;
    
    public int px;
    public int py;
    
    TextureFancyOverlayPosition(int x, int y, int px, int py) {
        this.x = x;
        this.y = y;
        this.px = px;
        this.py = py;
    }
}
