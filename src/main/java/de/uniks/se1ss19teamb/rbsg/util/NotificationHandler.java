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

    public void sendError(String errorMessage) {
        // display error message only if that's possible (i.e. a screen is loaded)
        if (popupController != null) {
            popupController.displayErrorMessage(errorMessage);
        }
    }

    public void sendError(String errorMessage, Logger logger, Exception e) {
        logger.error(errorMessage, e);
        sendError(errorMessage);
    }

}