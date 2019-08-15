package de.uniks.se1ss19teamb.rbsg.textures;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class AnimatedTexture extends Texture {

    private static List<AnimatedPane> animatedPanes = new ArrayList<>();
    private float periodTime;

    AnimatedTexture(String classPath, float periodTime) {
        super(classPath);
        this.periodTime = periodTime;
    }

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

    @Override
    public Pane instantiate(String color) {
        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setViewport(new Rectangle2D(0, 0, image.getWidth(), image.getWidth()));

        AnimatedPane textured = new AnimatedPane(iv, 0f, periodTime, this);
        animatedPanes.add(textured);

        return textured;
    }

}
