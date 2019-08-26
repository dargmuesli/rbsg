package de.uniks.se1ss19teamb.rbsg.textures;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * A texture class for textures with multiple layers.
 * Mainly used for terrain textures.
 */
class TextureFancy {

    private Texture base;
    
    private Texture overlay;
    
    private int depth;

    /**
     * The constructor.
     *
     * @param texturePathBase The path of the base texture's picture.
     * @param texturePathOverlay The path of the overlay texture's picture.
     * @param depth The overlay count.
     */
    TextureFancy(String texturePathBase, String texturePathOverlay, int depth) {
        base = new Texture(texturePathBase);
        overlay = new Texture(texturePathOverlay);
        this.depth = depth;
    }

    /**
     * Instantiates the base texture.
     * @return Pane of the base texture.
     */
    Pane instantiateBase() {
        return base.instantiate(null);
    }

    /**
     * Instantiate the overlay texture.
     * @param position The overlay's position related to the base's position.
     * @param type Type of the base texture.
     * @return Pane of the overlay texture.
     */
    Pane instantiateOverlay(TextureFancyOverlayPosition position, TextureFancyOverlayType type) {
        int x = position.px;
        int y = position.py + type.offset;
        
        ImageView iv = new ImageView();
        iv.setImage(overlay.image);
        iv.setViewport(new Rectangle2D(x, y, 32, 32));

        return new Pane(iv);
    }

    /**
     * Standard getter.
     *
     * @return The overlay count.
     */
    int getDepth() {
        return depth;
    }
}
