package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;

public class NotificationController {

    public static String initLevel;
    public static String initText;

    @FXML
    private HBox root;
    @FXML
    private Label label;

    @FXML
    private void initialize() {
        switch (initLevel) {
            case "ERROR":
                label.getStyleClass().add("label-error");
                root.getStyleClass().add("border-error");
                break;
            case "INFO":
                label.getStyleClass().add("label-info");
                root.getStyleClass().add("border-info");
                break;
            case "SUCCESS":
                label.getStyleClass().add("label-success");
                root.getStyleClass().add("border-success");
                break;
            case "WARNING":
                label.getStyleClass().add("label-warning");
                root.getStyleClass().add("border-warning");
                break;
            default:
                LogManager.getLogger().error("Unknown notification level \"" + initLevel + "\"!");
        }

        label.setText(initText);
    }
}
