package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.ui.PopupController;
import org.apache.logging.log4j.Logger;

public class NotificationHandler {

    private static NotificationHandler notificationHandler;
    private PopupController popupController;

    private NotificationHandler() {

    }

    public static NotificationHandler getNotificationHandler() {
        if (notificationHandler == null) {
            notificationHandler = new NotificationHandler();
        }

        return notificationHandler;
    }

    public void setPopupController(PopupController epc) {
        popupController = epc;
    }

    public void sendSuccess(String successMessage) {
        // display error message only if that's possible (i.e. a screen is loaded)
        if (popupController != null) {
            popupController.displaySuccessMessage(successMessage);
        }
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

        // display error message only if that's possible (i.e. a screen is loaded)
        if (popupController != null) {
            popupController.displayErrorMessage(errorMessage);
        }
    }

}