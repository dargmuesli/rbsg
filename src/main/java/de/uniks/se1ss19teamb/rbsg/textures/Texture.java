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

    private BufferedImage buffer;
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

    public Pane instantiate(double heigth, double width) {
        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitHeight(heigth);
        iv.setFitWidth(width);
        return new Pane(iv);
    }

}
