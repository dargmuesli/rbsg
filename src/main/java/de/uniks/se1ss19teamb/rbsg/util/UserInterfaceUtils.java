package de.uniks.se1ss19teamb.rbsg.util;

import java.io.IOException;
import java.util.Random;

import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserInterfaceUtils {
    private static ErrorHandler errorHandler = new ErrorHandler();

    private static final Logger logger = LogManager.getLogger(UserInterfaceUtils.class);

    public static void makeFadeOutTransition(String path, AnchorPane node) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(node);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> {
            try {
                fadeNextScene(path, node);
            } catch (IOException e) {
                errorHandler.sendError("Übergang in die nächste Szene konnte nicht ausgeführt werden!");
                e.printStackTrace();
            }
        });
        fadeTransition.play();
    }
    
    public static void makeFadeInTransition(AnchorPane node) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(node);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }
    
    public static void fadeNextScene(String path, AnchorPane stage) throws IOException {
        Parent nextScene = FXMLLoader.load(UserInterfaceUtils.class.getResource(path));
        Scene scene = new Scene(nextScene);
        Stage currentStage = (Stage) stage.getScene().getWindow();
        currentStage.setScene(scene);
    }
    
    private void slideNextScene(String path, int value, AnchorPane pane) throws IOException {
        Random r = new Random();
        boolean randomValue = r.nextBoolean();
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Timeline timeline;
        KeyValue keyValue;

        if (randomValue) {
            root.translateYProperty().setValue(-400);
            
            timeline = new Timeline();
            keyValue = new KeyValue(root.translateYProperty(),0, Interpolator.EASE_IN);
        } else {
            root.translateXProperty().setValue(-value);
            
            timeline = new Timeline();
            keyValue = new KeyValue(root.translateXProperty(),0,Interpolator.EASE_IN);
        }

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        Scene scene = new Scene(root);
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(scene);
    }
}
