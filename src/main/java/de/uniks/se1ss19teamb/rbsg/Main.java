package de.uniks.se1ss19teamb.rbsg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader
            .load(getClass().getResource("/de/uniks/se1ss19teamb/rbsg/login.fxml"));
        primaryStage.setTitle("RSBG-Team B");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
