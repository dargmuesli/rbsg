package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.ui.ErrorPopupController;

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
        errorPopupController.displayErrorMessage(errorMessage);
    }

}