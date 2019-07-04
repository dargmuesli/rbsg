package de.uniks.se1ss19teamb.rbsg;

import de.uniks.se1ss19teamb.rbsg.sound.SoundManager;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import de.uniks.se1ss19teamb.rbsg.util.EasterEggKeyEventHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final EasterEggKeyEventHandler easterEggKeyEventHandler = new EasterEggKeyEventHandler();
        TextureManager.init();
        SoundManager.init();

        Parent root = FXMLLoader
            .load(getClass().getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml"));
        primaryStage.setTitle("RSBG-Team B");
        Scene scene = new Scene(root, 800, 650);
        primaryStage.setScene(scene);
        // values taken from the largest fxml's root container width & height
        // the minHeight is slightly larger so that the horizontal 1px separator pane is always displayed
        primaryStage.setMinWidth(533);
        primaryStage.setMinHeight(621);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
        easterEggKeyEventHandler.setTicTacToe(scene, new KeyCodeCombination(KeyCode.F1, KeyCombination.SHIFT_ANY));
        easterEggKeyEventHandler.setSnake(scene, new KeyCodeCombination(KeyCode.F2, KeyCombination.SHIFT_ANY));
    }
}
