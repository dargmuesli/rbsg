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

/**
 * A texture class that can give you a pane of a picture
 * and make a dynamic border around the visible parts of the picture
 */

public class Texture {

    private BufferedImage buffer;
    Image image;

    /**
     * Constructor of a normal Texture
     * should be used for Units
     *
     * @param classPath The path of the picture of the unit
     */
    Texture(String classPath) {

        try {
            InputStream stream = this.getClass().getResourceAsStream(classPath);
            buffer = ImageIO.read(stream);
            image = SwingFXUtils.toFXImage(buffer, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The pane getter
     *
     * @param colorName String of the name of the color like "red", "blue" etc.
     * @return A pane with the texture as Imageview
     * The texture has a border around the visible parts of the picture
     * The color of the border is changed by colorName
     */
    public Pane instantiate(String colorName) {
        ImageView iv = new ImageView();
        setImageView(iv, colorName);
        return new Pane(iv);
    }

    /**
     * ImageView Setter
     *
     * @param iv The ImageView that should contain the texture
     * @param colorName Color of the border around the visible parts of the texture
     */
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

    /**
     * BufferedImage deep copy
     *
     * @param source The BufferedImage that should be copied
     * @return A new BufferedImage that contains the same information as the source
     */
    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics2D g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
}
