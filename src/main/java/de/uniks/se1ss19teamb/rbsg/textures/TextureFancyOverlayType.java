package de.uniks.se1ss19teamb.rbsg.textures;


public enum TextureFancyOverlayType {
    HORIZONTAL(64),
    VERTICAL(0), 
    BOTH(192),
    DIAGONAL(128);
    
    public int offset;
    
    TextureFancyOverlayType(int offset) {
        this.offset = offset;
    }
}
