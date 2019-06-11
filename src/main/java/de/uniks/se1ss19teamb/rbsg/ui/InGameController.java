package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InGameController {

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("inGame.fxml"));
        primaryStage.setTitle("InGameScreen");
        primaryStage.setScene(new Scene(root, 400, 600));
        primaryStage.show();
    }
}
