package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.File;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class UnitOverviewController {
    @FXML
    Label lblCount;
    @FXML
    JFXButton btnMore;
    @FXML
    JFXButton btnLess;
    @FXML
    VBox unitBox;
    private String path = "./src/main/resources/de/uniks/se1ss19teamb/rbsg/cssMode.json";
    private String cssDark = "/de/uniks/se1ss19teamb/rbsg/css/dark-design2.css";
    private String cssWhite = "/de/uniks/se1ss19teamb/rbsg/css/white-design2.css";

    private int count = 0;

    public void initialize() {
        if (SerializeUtils.deserialize(new File(path), boolean.class)) {
            unitBox.getStylesheets().clear();
            unitBox.getStylesheets().add(cssDark);
        } else {
            unitBox.getStylesheets().clear();
            unitBox.getStylesheets().add(cssWhite);
        }

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
