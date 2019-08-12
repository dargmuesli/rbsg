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
    public void initialize() {
        switch (initLevel) {
            case "ERROR":
                label.setStyle("-fx-text-fill: #e00909;");
                break;
            case "INFO":
                label.setStyle("-fx-text-fill: #0c51c2;");
                break;
            case "SUCCESS":
                label.setStyle("-fx-text-fill: #16b807;");
                break;
            case "WARNING":
                label.setStyle("-fx-text-fill: #ffbb00;");
                break;
            default:
                LogManager.getLogger().error("Unknown notification level \"" + initLevel + "\"!");
        }

        label.setText(initText);
    }
}
