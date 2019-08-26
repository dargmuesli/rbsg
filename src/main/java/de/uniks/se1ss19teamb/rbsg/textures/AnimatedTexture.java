package de.uniks.se1ss19teamb.rbsg.textures;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * A class that creates {@link AnimatedPane}s of animated textures
 * with a colored border around the non-transparent parts.
 */
public class AnimatedTexture extends Texture {

    private static List<AnimatedPane> animatedPanes = new ArrayList<>();
    private float periodTime;

    /**
     * The constructor.
     *
     * @param classPath The texture resource's path.
     * @param periodTime The time period between picture changes in nanoseconds.
     */
    AnimatedTexture(String classPath, float periodTime) {
        super(classPath);
        this.periodTime = periodTime;
    }

    /**
     * Creates a new {@link AnimationTimer} for the texture.
     */
    static void registerAnimUpdates() {
        new AnimationTimer() {
            long ns = 0;

            @Override
            public void handle(long now) {
                if (ns == 0) {
                    ns = now;
                }

                double delta = (double) (now - ns) / 1000000.0;
                ns = now;

                for (AnimatedPane pane : animatedPanes) {
                    pane.advance(delta);
                }
            }
        }.start();
    }

    /**
     * Creates an animated pane.
     *
     * @param colorName The color's name like "red", "blue" etc.
     * @return A pane where a contained ImageView will have a border around the visible parts of the animation.
     */
    @Override
    public Pane instantiate(String colorName) {
        ImageView iv = new ImageView();
        setImageView(iv, colorName);
        iv.setViewport(new Rectangle2D(0, 0, image.getWidth(), image.getWidth()));

        AnimatedPane textured = new AnimatedPane(iv, 0f, periodTime, this);
        animatedPanes.add(textured);

        return textured;
    }
}
