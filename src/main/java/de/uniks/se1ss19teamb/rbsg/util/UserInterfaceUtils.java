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

public class UserInterfaceUtils {
    
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
        if (randomValue) {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            root.translateYProperty().setValue(-400);
            
            Timeline timeline = new Timeline();
            KeyValue keyValue = new KeyValue(root.translateYProperty(),0, Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.setScene(scene);
        } else {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            root.translateXProperty().setValue(-value);
            
            Timeline timeline = new Timeline();
            KeyValue keyValue = new KeyValue(root.translateXProperty(),0,Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.setScene(scene);
        }
    }
}
