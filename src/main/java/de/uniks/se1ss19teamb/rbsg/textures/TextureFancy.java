package de.uniks.se1ss19teamb.rbsg.textures;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

class TextureFancy {

    private Texture base;
    
    private Texture overlay;
    
    private int depth;
    
    TextureFancy(String classPathBase, String classPathOverlay, int depth) {
        base = new Texture(classPathBase);
        overlay = new Texture(classPathOverlay);
        this.depth = depth;
    }
    
    Pane instantiateBase() {
        return base.instantiate();
    }
    
    Pane instantiateOverlay(TextureFancyOverlayPosition position, TextureFancyOverlayType type) {
        int x = position.px;
        int y = position.py + type.offset;
        
        ImageView iv = new ImageView();
        iv.setImage(overlay.image);
        iv.setViewport(new Rectangle2D(x, y, 32, 32));

        return new Pane(iv);
    }

    int getDepth() {
        return depth;
    }
}
