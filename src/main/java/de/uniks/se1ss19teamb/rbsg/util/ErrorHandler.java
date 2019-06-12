package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.ui.ErrorPopupController;
import org.apache.logging.log4j.Logger;

public class ErrorHandler {

    private static ErrorHandler errorHandler;
    private ErrorPopupController errorPopupController;

    private ErrorHandler() {

    }

    public static ErrorHandler getErrorHandler() {
        if (errorHandler == null) {
            errorHandler = new ErrorHandler();
        }

        return errorHandler;
    }

    public void setErrorPopupController(ErrorPopupController epc) {
        errorPopupController = epc;
    }

    public void sendError(String errorMessage) {
        // display error message only if that's possible (i.e. a screen is loaded)
        if (errorPopupController != null) {
            errorPopupController.displayErrorMessage(errorMessage);
        }
    }

    public void sendError(String errorMessage, Logger logger, Exception e) {
        logger.error(errorMessage, e);
        sendError(errorMessage);
    }

}