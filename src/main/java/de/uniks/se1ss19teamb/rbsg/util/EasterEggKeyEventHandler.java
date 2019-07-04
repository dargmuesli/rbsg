package de.uniks.se1ss19teamb.rbsg.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;


public class EasterEggKeyEventHandler {

    public void setTicTacToe(Scene scene, KeyCombination keyCombination) {
        scene.setOnKeyPressed(e -> {
            if (keyCombination.match(e)) {
                try {
                    Parent root1 = FXMLLoader
                        .load(getClass().getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/tictactoe.fxml"));
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

    public void setSnake(Scene scene, KeyCombination keyCombination) {
        scene.setOnKeyPressed(e -> {
            if (keyCombination.match(e)) {
                // Snake needs to be implemented
            }
        });
    }
}
