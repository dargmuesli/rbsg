package de.uniks.se1ss19teamb.rbsg.textures;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javax.imageio.ImageIO;

public class Texture {

    BufferedImage buffer;
    Image image;

    Texture(String classPath) {

        try {
            InputStream stream = this.getClass().getResourceAsStream(classPath);
            buffer = ImageIO.read(stream);
            image = SwingFXUtils.toFXImage(buffer, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pane instantiate(String colorName) {
        ImageView iv = new ImageView();
        setImageView(iv, colorName);
        return new Pane(iv);
    }

    protected ImageView setImageView(ImageView iv, String colorName) {
        Color color;
        try {
            Field field = Class.forName("java.awt.Color").getField(colorName);
            color = (Color)field.get(null);
        } catch (Exception e) {
            color = null;
        }
        if (buffer != null && color != null) {
            for (int i = 0; i < buffer.getWidth(); i++) {
                for (int j = 0; j < buffer.getHeight(); j++) {
                    if ((buffer.getRGB(i, j) & 0xff000000) >>> 24 != 0) {
                        int rgb = color.getRGB();
                        if (j > 0) {
                            buffer.setRGB(i, j - 1, rgb);
                        } else {
                            buffer.setRGB(i, j, rgb);
                        }

                        while (j < buffer.getHeight() && buffer.getRGB(i, j) != 0) {
                            j++;
                        }
                        if (j < buffer.getHeight()) {
                            buffer.setRGB(i, j, rgb);
                        }

                    }
                }
            }

            Image img = SwingFXUtils.toFXImage(buffer, null);
            iv.setImage(img);
        } else {
            iv.setImage(image);
        }
        return iv;
    }

}
