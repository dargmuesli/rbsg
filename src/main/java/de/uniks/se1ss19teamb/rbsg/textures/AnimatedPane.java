package de.uniks.se1ss19teamb.rbsg.textures;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class AnimatedPane extends Pane {
    private ImageView iv;
    private AnimatedTexture texture;
    private int numberOfSprites;
    private double time;
    private int sprite;
    private float periodTime;

    AnimatedPane(ImageView iv, float phi, float periodTime, AnimatedTexture texture) {
        super(iv);
        this.iv = iv;
        this.periodTime = periodTime;
        this.texture = texture;
        this.numberOfSprites = (int) (texture.image.getHeight()) / (int) (texture.image.getWidth());
        this.sprite = 0;

        {
            int periodAdvances = (int) (phi / periodTime);
            sprite += periodAdvances % numberOfSprites;
        }

        this.time = 0;

    }

    void advance(double delta) {
        time += delta;

        if (time >= periodTime) {
            time -= periodTime;

            if (++sprite >= numberOfSprites) {
                sprite = 0;
            }

            double width = texture.image.getWidth();
            double height = texture.image.getWidth();
            iv.setViewport(new Rectangle2D(0, sprite * width, width, height));
        }
    }
}
