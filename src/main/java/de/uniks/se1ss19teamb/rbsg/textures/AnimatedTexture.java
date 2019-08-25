package de.uniks.se1ss19teamb.rbsg.textures;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Child class of Texture.
 * A class that gives you an AnimatedPane with the animation of the texture
 * and makes a colored border around the animation.
 */
public class AnimatedTexture extends Texture {

    private static List<AnimatedPane> animatedPanes = new ArrayList<>();
    private float periodTime;

    /**
     * Constructor of AnimatedTexture.
     *
     * @param classPath The path of the texture.
     * @param periodTime The period between the change of the pectures in nano seconds.
     */
    AnimatedTexture(String classPath, float periodTime) {
        super(classPath);
        this.periodTime = periodTime;
    }

    /**
     * Animation Updater.
     * updates the animations.
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
     * The Animated Pane getter.
     *
     * @param colorName String of the name of the color like "red", "blue" etc.
     * @return An AnimatedPane with the texture as animation.
     *      * The texture has a border around the visible parts of the pictures.
     *      * The color of the border is changed by colorName.
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
