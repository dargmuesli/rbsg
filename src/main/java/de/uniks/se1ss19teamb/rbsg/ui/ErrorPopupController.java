package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ErrorPopupController {

    @FXML
    AnchorPane errorPopup;

    @FXML
    Label errorLabel;

    public void displayErrorMessage(String errorMessage) {
        errorLabel.setText(errorMessage);
        errorPopup.setVisible(true);
        new Thread(new Task<Void>() {
            @Override
            public Void call() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                errorPopup.setVisible(false);
                return null;
            }
        }).start();
    }
}
