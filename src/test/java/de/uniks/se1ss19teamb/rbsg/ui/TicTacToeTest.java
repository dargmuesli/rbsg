package de.uniks.se1ss19teamb.rbsg.ui;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
class TicTacToeTest {

    @Start
    public void start(Stage stage) throws IOException {
        Parent root1 = FXMLLoader
            .load(TicTacToeController.class
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/tictactoe.fxml"));
        stage.setScene(new Scene(root1, 800, 600));
        stage.show();
        stage.setResizable(false);
    }

    @Test
    void ticTacToeTest(FxRobot robot) {
        robot.clickOn("#btnReplay");
    }
}
