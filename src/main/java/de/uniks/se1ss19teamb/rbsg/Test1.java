package de.uniks.se1ss19teamb.rbsg;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Test1 extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Test.fxml"));

        primaryStage.setTitle("Test 1");
        primaryStage.setScene(new Scene(root,700,700));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


}
