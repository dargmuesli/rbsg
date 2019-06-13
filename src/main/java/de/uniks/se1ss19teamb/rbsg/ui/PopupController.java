package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.concurrent.Task;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class PopupController {

    @FXML
    AnchorPane popup;

    @FXML
    Label label;

    public void displayErrorMessage(String errorMessage) {
        label.setText(errorMessage);
        show();

    }

    public void displaySuccessMessage(String successMessage) {
        label.setText(successMessage);
        // TODO green
        show();
    }

    private void show() {
        popup.setVisible(true);
        new Thread(new Task<Void>() {
            @Override
            public Void call() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                popup.setVisible(false);
                return null;
            }
        }).start();
    }
}
