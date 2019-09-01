package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.features.Snake;
import de.uniks.se1ss19teamb.rbsg.ui.TicTacToeController;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;


public class EasterEggKeyEventHandler {

    private static final KeyCodeCombination snake = new KeyCodeCombination(KeyCode.F1, KeyCombination.SHIFT_ANY);
    private static final KeyCodeCombination tictactoe = new KeyCodeCombination(KeyCode.F2, KeyCombination.SHIFT_ANY);

    /**
     * Sets a scene's key combinations which open easter eggs.
     *
     * @param scene The screne to set the key combinations for.
     */
    public static void setEvents(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (snake.match(e)) {
                Snake game = new Snake();
                game.start(Snake.classStage);
            }

            if (tictactoe.match(e)) {
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
}
