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
 * A texture class that provides picture panes
 * and renders a dynamic border around the non-transparent parts of the picture.
 */
public class Texture {

    private BufferedImage buffer;
    Image image;

    /**
     * Creates an instance from a resource.
     *
     * @param texturePath The path of the texture's picture.
     */
    Texture(String texturePath) {
        try {
            InputStream stream = this.getClass().getResourceAsStream(texturePath);
            buffer = ImageIO.read(stream);
            image = SwingFXUtils.toFXImage(buffer, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a colored pane.
     *
     * @param colorName The color's name like "red", "blue" etc.
     * @return A pane where a contained ImageView will have a border around the visible parts of the picture.
     */
    public Pane instantiate(String colorName) {
        ImageView iv = new ImageView();
        setImageView(iv, colorName);
        return new Pane(iv);
    }

    /**
    *Creates a pane that has variable size.
    *
    * @param heigth The heigth of the pane.
    * @param width The width of the pane.
    * @return A pane with size that is defined be the user.
     */
    public Pane instantiate(double heigth, double width) {
        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitHeight(heigth);
        iv.setFitWidth(width);
        return new Pane(iv);
    }
    
    /**
     * ImageView setter.
     *
     * @param iv The ImageView that contains the texture.
     * @param colorName Color of the border around the texture's non-transparent parts.
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
     * Creates a copy of the given image.
     *
     * @param source The image that is to be copied.
     * @return A new, identical instance of the image.
     */
    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics2D g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
}
