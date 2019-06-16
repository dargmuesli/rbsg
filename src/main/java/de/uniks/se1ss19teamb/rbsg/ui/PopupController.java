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

    public void displayError(String errorMessage) {
        label.setText(errorMessage);
        show();

    }

    public void displayInformation(String informationMessage) {
        label.setText(informationMessage);
        // TODO blue @Patrick
        show();
    }

    public void displaySuccess(String successMessage) {
        label.setText(successMessage);
        // TODO green @Patrick
        show();
    }

    public void displayWarning(String warningMessage) {
        label.setText(warningMessage);
        // TODO orange @Patrick
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
