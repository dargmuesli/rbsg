package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.ui.NotificationController;
import org.apache.logging.log4j.Logger;

public class NotificationHandler {

    private static NotificationHandler notificationHandler;
    private NotificationController notificationController;

    private NotificationHandler() {
    }

    public static NotificationHandler getInstance() {
        if (notificationHandler == null) {
            notificationHandler = new NotificationHandler();
        }

        return notificationHandler;
    }

    public NotificationController getNotificationController() {
        return this.notificationController;
    }

    public void setNotificationController(NotificationController epc) {
        this.notificationController = epc;
    }

    public void sendError(String errorMessage, Logger logger) {
        sendError(errorMessage, logger, null);
    }

    public void sendError(String errorMessage, Logger logger, Exception e) {
        if (e == null) {
            logger.error(errorMessage);
        } else {
            logger.error(errorMessage, e);
        }

        // display message only if that's possible (i.e. a screen is loaded)
        if (notificationController != null) {
            notificationController.displayError(errorMessage);
        }
    }

    public void sendInfo(String infoMessage, Logger logger) {
        logger.info(infoMessage);

        // display message only if that's possible (i.e. a screen is loaded)
        if (notificationController != null) {
            notificationController.displayInformation(infoMessage);
        }
    }

    public void sendSuccess(String successMessage, Logger logger) {
        logger.debug(successMessage);

        // display message only if that's possible (i.e. a screen is loaded)
        if (notificationController != null) {
            notificationController.displaySuccess(successMessage);
        }
    }

    public void sendWarning(String warningMessage, Logger logger) {
        logger.warn(warningMessage);

        // display message only if that's possible (i.e. a screen is loaded)
        if (notificationController != null) {
            notificationController.displayWarning(warningMessage);
        }
    }
}