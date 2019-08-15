package de.uniks.se1ss19teamb.rbsg.textures;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

    public Pane instantiate(String color) {
        ImageView iv = new ImageView();
        if (buffer != null && color != null) {
            for (int i = 0; i < buffer.getWidth(); i++) {
                for (int j = 0; j < buffer.getHeight(); j++) {
                    if ((buffer.getRGB(i, j) & 0xff000000) >>> 24 != 0) {
                        int bufferrgb = buffer.getRGB(i, j);
                        int rgb = Color.BLUE.getRGB();
                        buffer.setRGB(i, j, rgb);
//                        while (buffer.getRGB(i, j) != 0) {
//                            j++;
//                        }
//                        if (j < buffer.getHeight()) {
//                            buffer.setRGB(i, j - 1, rgb);
//
//                        }
//                        Color c = new Color(rgb);
//                        System.out.println(c.getRGB() + " | " + buffer.getRGB(i, j));
                    }
                }
            }

            Image img = SwingFXUtils.toFXImage(buffer, null);
            iv.setImage(img);
        } else {
            iv.setImage(image);
        }


        return new Pane(iv);
    }

}
