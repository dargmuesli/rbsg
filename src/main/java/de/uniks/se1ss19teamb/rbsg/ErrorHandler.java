package de.uniks.se1ss19teamb.rbsg;

public class ErrorHandler {

    ErrorPopupController errorPopupController;

    public void setErrorPopupController(ErrorPopupController epc) {
        errorPopupController = epc;
    }

    public void sendError(String errorMessage) {
        errorPopupController.displayErrorMessage(errorMessage);
    }


}
