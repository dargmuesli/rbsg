package de.uniks.se1ss19teamb.rbsg.features;

import de.uniks.se1ss19teamb.rbsg.sound.Sound;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;

public class ZuendorfMeme {
    private static int HOVER_COUNT;
    private static int HOVER_MEDIUM_COUNT = 10;
    private static int HOVER_MAX_COUNT = 25;
    private static Image IMAGE;
    private static ImageView IMAGE_VIEW;
    private static int LAST_SIDE = -1;

    static {
        try {
            IMAGE = new Image(
                new FileInputStream(
                    URLDecoder.decode(
                        ZuendorfMeme.class.getResource("/de/uniks/se1ss19teamb/rbsg/memes/zuendorf/zuendorf_icon.png")
                            .getFile(), "UTF-8")));
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            NotificationHandler.getInstance().sendError("Couldn't load Zündorf meme image.", LogManager.getLogger());
        }

        IMAGE_VIEW = new ImageView(IMAGE);
    }

    public static void setup(Pane root) {
        HOVER_COUNT = 0;
        IMAGE_VIEW.setOnMouseEntered(t -> {
            HOVER_COUNT++;

            if (HOVER_COUNT < HOVER_MAX_COUNT) {
                replaceZuendorf(root);

                if (HOVER_COUNT == HOVER_MEDIUM_COUNT) {
                    new Sound("../memes/zuendorf/bad_bitch.wav", true).play(1);
                }
            } else {
                root.getChildren().remove(IMAGE_VIEW);
                new Sound("../memes/zuendorf/beer.wav", true).play(1);
            }
        });

        root.getChildren().add(IMAGE_VIEW);

        replaceZuendorf(root);
    }

    private static void replaceZuendorf(Pane root) {
        Platform.runLater(() -> {
            int min = 0;
            int max = 3;
            int staticXPosOffset = (int) IMAGE.getWidth() / 2;
            int staticYPosOffset = (int) IMAGE.getHeight() / 2;
            double randomXPos = distributedPosition(root.getWidth(), IMAGE.getWidth()) - staticXPosOffset;
            double randomYPos = distributedPosition(root.getHeight(), IMAGE.getHeight()) - staticYPosOffset;
            int side;

            do {
                side = min + new Random().nextInt(max - min + 1);
            } while (LAST_SIDE == side);

            switch (side) {
                case 0:
                    IMAGE_VIEW.setRotate(180);
                    IMAGE_VIEW.setX(randomXPos);
                    IMAGE_VIEW.setY(staticYPosOffset * -1);
                    break;
                case 1:
                    IMAGE_VIEW.setRotate(270);
                    IMAGE_VIEW.setX(root.getWidth() - staticXPosOffset);
                    IMAGE_VIEW.setY(randomYPos);
                    break;
                case 2:
                    IMAGE_VIEW.setRotate(0);
                    IMAGE_VIEW.setX(randomXPos);
                    IMAGE_VIEW.setY(root.getHeight() - staticYPosOffset);
                    break;
                case 3:
                    IMAGE_VIEW.setRotate(90);
                    IMAGE_VIEW.setX(staticXPosOffset * -1);
                    IMAGE_VIEW.setY(randomYPos);
                    break;
                default:
                    NotificationHandler.getInstance().sendError(
                        "You really need to check your code!", LogManager.getLogger());
            }

            LAST_SIDE = side;
        });
    }

    private static double distributedPosition(double parentSize, double imageSize) {
        return (parentSize - (2 * imageSize)) * new Random().nextFloat() + imageSize;
    }
}
