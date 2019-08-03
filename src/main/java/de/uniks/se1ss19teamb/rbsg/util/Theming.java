package de.uniks.se1ss19teamb.rbsg.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Theming {
    private static final Logger logger = LogManager.getLogger();

    public static final File cssModeFile =
        Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "rgsb_dark-mode-active.json").toFile();

    public static void setTheme(List<Pane> panes) {
        String darkDesignCssPath = Theming.class.getResource("/de/uniks/se1ss19teamb/rbsg/css/darkDesign.css")
            .toExternalForm();
        String whiteDesignCssPath = Theming.class.getResource("/de/uniks/se1ss19teamb/rbsg/css/whiteDesign.css")
            .toExternalForm();

        if (darkModeActive()) {
            panes.forEach(pane -> {
                if (!pane.getStylesheets().contains(darkDesignCssPath)) {
                    pane.getStylesheets().add(darkDesignCssPath);
                }
            });
        } else {
            panes.forEach(pane -> {
                pane.getStylesheets().remove(darkDesignCssPath);
                pane.getStylesheets().add(whiteDesignCssPath);
            });
        }
    }

    public static boolean darkModeActive() {
        if (!cssModeFile.exists()) {
            SerializeUtil.serialize(cssModeFile.getAbsolutePath(), true);
            return true;
        }

        Boolean cssMode = SerializeUtil.deserialize(cssModeFile, boolean.class);

        if (cssMode == null) {
            NotificationHandler.getInstance().sendError("Could not load css mode from file!", logger);
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
