package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.application.Platform;
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
        Platform.runLater(() -> {
            label.setStyle("-fx-text-fill: #e00909;");
            label.setText(errorMessage);
            show();
        });
    }

    public void displayInformation(String informationMessage) {
        Platform.runLater(() -> {
            label.setStyle("-fx-text-fill: #0c51c2;");
            label.setText(informationMessage);
            show();
        });
    }

    public void displaySuccess(String successMessage) {
        Platform.runLater(() -> {
            label.setStyle("-fx-text-fill: #16b807;");
            label.setText(successMessage);
            show();
        });
    }

    public void displayWarning(String warningMessage) {
        Platform.runLater(() -> {
            label.setStyle("-fx-text-fill: #ffbb00;");
            label.setText(warningMessage);
            show();
        });
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
