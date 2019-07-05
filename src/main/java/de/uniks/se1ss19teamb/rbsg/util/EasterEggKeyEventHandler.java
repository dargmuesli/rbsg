package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.features.Snake;
import de.uniks.se1ss19teamb.rbsg.ui.TicTacToeController;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class EasterEggKeyEventHandler {

    public static void setTicTacToe(Scene scene, KeyCombination keyCombination) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (keyCombination.match(e)) {
                try {
                    Parent root1 = FXMLLoader
                        .load(TicTacToeController.class
                            .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/tictactoe.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1, 800, 600));
                    stage.show();
                    stage.setResizable(false);
                } catch (IOException b) {
                    b.printStackTrace();
                }
            }
        });
    }

    public static void setSnake(Scene scene, KeyCombination keyCombination) {
        scene.setOnKeyPressed(e -> {
            if (keyCombination.match(e)) {
                Snake snake = new Snake();
                snake.start(Snake.classStage);
            }
        });
    }
}
