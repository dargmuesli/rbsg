package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.Main;
import de.uniks.se1ss19teamb.rbsg.ui.modules.NotificationController;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationHandler {

    public static void sendError(String errorMessage, Logger logger) {
        sendError(errorMessage, logger, null);
    }

    /**
     * Logs and displays an error.
     *
     * @param errorMessage The user readable message that is shown on the UI.
     * @param logger       The logger to log the error to.
     * @param e            The exception that caused the error.
     */
    public static void sendError(String errorMessage, Logger logger, Exception e) {
        if (e == null) {
            logger.error(errorMessage);
        } else {
            logger.error(errorMessage, e);
        }

        send("ERROR", errorMessage);
    }

    public static void sendInfo(String infoMessage, Logger logger) {
        logger.info(infoMessage);
        send("INFO", infoMessage);
    }

    public static void sendSuccess(String successMessage, Logger logger) {
        logger.debug(successMessage);
        send("SUCCESS", successMessage);
    }

    public static void sendWarning(String warningMessage, Logger logger) {
        logger.warn(warningMessage);
        send("WARNING", warningMessage);
    }

    private static void send(String level, String message) {
        if (Main.PRIMARY_STAGE != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(NotificationHandler.class
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/modules/notification.fxml"));

            try {
                NotificationController.initLevel = level;
                NotificationController.initText = message;

                HBox hbxNotification = fxmlLoader.load();

                new Thread(() -> {
                    Platform.runLater(() -> ((AnchorPane) Main.PRIMARY_STAGE.getScene().lookup("#apnFade"))
                        .getChildren().add(hbxNotification));

                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Platform.runLater(() -> ((AnchorPane) Main.PRIMARY_STAGE.getScene().lookup("#apnFade"))
                        .getChildren().remove(hbxNotification));
                }).start();
            } catch (IOException e) {
                LogManager.getLogger().error("Error loading the popup controller's fxml!", e);
            }
        }
    }
}