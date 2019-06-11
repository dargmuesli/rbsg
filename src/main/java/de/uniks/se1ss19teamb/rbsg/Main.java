package de.uniks.se1ss19teamb.rbsg;

import de.uniks.se1ss19teamb.rbsg.sound.SoundManager;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        TextureManager.init();
        SoundManager.init();
        
        Parent root = FXMLLoader
            .load(getClass().getResource("/de/uniks/se1ss19teamb/rbsg/login.fxml"));
        primaryStage.setTitle("RSBG-Team B");
        primaryStage.setScene(new Scene(root, 800, 650));
        // values taken from the largest fxml's root container width & height
        // the minHeight is slightly larger so that the horizontal 1px separator pane is always displayed
        primaryStage.setMinWidth(533);
        primaryStage.setMinHeight(621);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }
}
