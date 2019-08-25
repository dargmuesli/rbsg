package de.uniks.se1ss19teamb.rbsg.textures;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * A texture class for textures with multiple layers.
 * User here mainly for Terrain Textures.
 */
class TextureFancy {

    private Texture base;
    
    private Texture overlay;
    
    private int depth;

    /**
     * Constructor of TextureFancy.
     *
     * @param classPathBase String of the path of the base, the lowest layer for the texture
     * @param classPathOverlay String of the path of the overlay.
     * @param depth The amount of overlays.
     */
    TextureFancy(String classPathBase, String classPathOverlay, int depth) {
        base = new Texture(classPathBase);
        overlay = new Texture(classPathOverlay);
        this.depth = depth;
    }

    /**
     * Base Pane getter.
     * @return Pane of the base.
     */
    Pane instantiateBase() {
        return base.instantiate(null);
    }

    /**
     * Overlay Pane getter.
     * @param position The position of the overlay fitting for the position of base.
     * @param type Type of the base.
     * @return Pane of the overlay.
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
     * Standart getter.
     *
     * @return The amount of overlays.
     */
    int getDepth() {
        return depth;
    }
}
