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


    void setImageView(ImageView iv, String colorName) {
        Color color;
        try {
            Field field = Class.forName("java.awt.Color").getField(colorName);
            color = (Color)field.get(null);
        } catch (Exception e) {
            color = null;
        }
        BufferedImage copyImage = copyImage(buffer);
        if (copyImage != null && color != null) {
            for (int i = 0; i < copyImage.getWidth(); i++) {
                for (int j = 0; j < copyImage.getHeight(); j++) {

                    if ((copyImage.getRGB(i, j) & 0xff000000) >>> 24 != 0) {
                        int rgb = color.getRGB();
                        if (j > 0) {
                            copyImage.setRGB(i, j - 1, rgb);
                        } else {
                            copyImage.setRGB(i, j, rgb);
                        }
                        while (j < copyImage.getHeight() && copyImage.getRGB(i, j) != 0) {
                            j++;
                        }
                        if (j < copyImage.getHeight()) {
                            copyImage.setRGB(i, j, rgb);
                        }
                    }
                }
            }
            Image img = SwingFXUtils.toFXImage(copyImage, null);
            iv.setImage(img);
        } else {
            iv.setImage(image);
        }
    }

    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics2D g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

}
