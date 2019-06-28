package de.uniks.se1ss19teamb.rbsg.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.File;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Theming {
    private static final Logger logger = LogManager.getLogger();
    private static NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();

    private static File cssModeFile = new File("./src/main/resources/de/uniks/se1ss19teamb/rbsg/cssMode.json");

    public static void setTheme(AnchorPane anchorPane1, AnchorPane anchorPane2) {

        Boolean darkModeActive = darkModeActive();

        if (darkModeActive == null) {
            notificationHandler.sendError("Could not determine dark mode status!", logger);
            return;
        }

        String cssDark = "/de/uniks/se1ss19teamb/rbsg/css/dark-design2.css";
        String cssWhite = "/de/uniks/se1ss19teamb/rbsg/css/white-design2.css";

        anchorPane1.getStylesheets().clear();
        anchorPane2.getStylesheets().clear();
        anchorPane1.getStylesheets().add(darkModeActive ? cssDark : cssWhite);
        anchorPane2.getStylesheets().add(darkModeActive ? cssDark : cssWhite);
    }

    public static boolean darkModeActive() {
        if (!cssModeFile.exists()) {
            SerializeUtils.serialize(cssModeFile.getAbsolutePath(), true);
            return true;
        }

        Boolean cssMode = SerializeUtils.deserialize(cssModeFile, boolean.class);

        if (cssMode == null) {
            notificationHandler.sendError("Could not load css mode from file!", logger);
            return false;
        }

        return cssMode;
    }

    public static void hamburgerMenuTransition(JFXHamburger ham, JFXButton button) {
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(ham);
        transition.setRate(-1);
        ham.addEventHandler(MouseEvent.MOUSE_PRESSED, (event -> {
            transition.setRate(transition.getRate() * -1);

            if (transition.getRate() == -1) {
                button.setVisible(false);
            } else if (transition.getRate() == 1) {
                button.setVisible(true);
            }

            transition.play();
        }));
    }
}
