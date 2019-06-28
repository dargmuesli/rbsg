package de.uniks.se1ss19teamb.rbsg.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.File;
import java.util.List;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Theming {
    private static final Logger logger = LogManager.getLogger();
    private static NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();

    private static File cssModeFile = new File("./src/main/resources/de/uniks/se1ss19teamb/rbsg/darkModeActive.json");

    public static void setTheme(List<Pane> panes) {
        String darkDesignCssPath = Theming.class.getResource("/de/uniks/se1ss19teamb/rbsg/css/darkDesign.css")
            .toExternalForm();

        if (darkModeActive()) {
            panes.forEach(pane -> pane.getStylesheets().add(darkDesignCssPath));
        } else {
            panes.forEach(pane -> pane.getStylesheets().remove(darkDesignCssPath));
        }
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
