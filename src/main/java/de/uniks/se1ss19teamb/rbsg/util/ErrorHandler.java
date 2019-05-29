package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.ui.ErrorPopupController;

public class ErrorHandler {

    ErrorPopupController errorPopupController;

    public void setErrorPopupController(ErrorPopupController epc) {
        errorPopupController = epc;
    }

    public void sendError(String errorMessage) {
        errorPopupController.displayErrorMessage(errorMessage);
    }


}
