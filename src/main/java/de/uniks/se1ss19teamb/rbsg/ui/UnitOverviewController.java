package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UnitOverviewController {
    @FXML
    Label lblCount;
    @FXML
    JFXButton btnMore;
    @FXML
    JFXButton btnLess;

    private int count = 0;

    public void initialize() {
        updateCount();
    }

    @FXML
    private void eventHandler(ActionEvent event) {
        switch (((JFXButton) event.getSource()).getId()) {
            case "btnMore":
                count++;
                updateCount();
                break;
            case "btnLess":
                if (count > 0) {
                    count--;
                    updateCount();
                }

                break;
            default:
        }
    }

    private void updateCount() {
        if (count == 0) {
            btnLess.setDisable(true);
        } else {
            btnLess.setDisable(false);
        }

        lblCount.setText("Anzahl: " + count);
    }
}
