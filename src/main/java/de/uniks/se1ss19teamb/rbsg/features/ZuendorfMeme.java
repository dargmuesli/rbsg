package de.uniks.se1ss19teamb.rbsg.features;

import de.uniks.se1ss19teamb.rbsg.sound.SoundManager;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.RandomUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;

public class ZuendorfMeme {
    private static int HOVER_COUNT;
    private static int HOVER_MEDIUM_COUNT = 10;
    private static int HOVER_MAX_COUNT = 25;
    private static Image IMAGE;
    protected static ImageView IMAGE_VIEW;
    private static int LAST_SIDE = -1;

    static {
        try {
            IMAGE = new Image(
                new FileInputStream(
                    URLDecoder.decode(
                        ZuendorfMeme.class.getResource("/de/uniks/se1ss19teamb/rbsg/memes/zuendorf_icon.png")
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
                    SoundManager.playSound("meme_bad_bitch", 0);
                }
            } else {
                root.getChildren().remove(IMAGE_VIEW);
                SoundManager.playSound("meme_beer", 0);
            }
        });

        root.getChildren().add(IMAGE_VIEW);

        replaceZuendorf(root);
    }

    private static void replaceZuendorf(Pane root) {
        double staticXPosOffset = IMAGE.getWidth() / 2;
        double staticYPosOffset = IMAGE.getHeight() / 2;
        double randomXPos = RandomUtil.distributedPosition(root.getWidth(), IMAGE.getWidth()) - staticXPosOffset;
        double randomYPos = RandomUtil.distributedPosition(root.getHeight(), IMAGE.getHeight()) - staticYPosOffset;
        int side;

        do {
            side = RandomUtil.inRange(0, 3);
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
    }
}
