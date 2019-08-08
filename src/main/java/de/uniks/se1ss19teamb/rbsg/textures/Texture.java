package de.uniks.se1ss19teamb.rbsg.textures;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Texture {

    Image image;

    Texture(String classPath) {
        InputStream stream = this.getClass().getResourceAsStream(classPath);
        image = new Image(stream);
    }

    public Pane instantiate() {
        ImageView iv = new ImageView();
        iv.setImage(image);

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
