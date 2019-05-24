package de.uniks.se1ss19teamb.rbsg;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ErrorPopupController {

    @FXML
    Label errorLabel;

    String errorMessage;

    class ErrorMessageThread implements Runnable {
        @Override
        public void run()
        {
            errorLabel.setText(errorMessage);
            errorLabel.setVisible(true);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            errorLabel.setVisible(false);
        }
    }

    public void displayErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        Thread errorThread = new Thread(new ErrorMessageThread());
        errorThread.start();
    }
}
