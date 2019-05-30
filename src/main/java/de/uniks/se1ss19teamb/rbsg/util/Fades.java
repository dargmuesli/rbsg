package de.uniks.se1ss19teamb.rbsg.util;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Fades {
    
    public static void makeFadeOutTransition(String path, AnchorPane node){
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
        Parent nextScene = FXMLLoader.load(Fades.class.getResource(path));
        Scene scene = new Scene(nextScene);
        Stage currentStage = (Stage) stage.getScene().getWindow();
        
        currentStage.setScene(scene);
    }
}
