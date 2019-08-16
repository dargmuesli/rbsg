package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.features.Snake;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
class SnakeTest {

    @Start
    public void start(Stage stage) {
        Snake snake = new Snake();
        snake.start(stage);
    }

    @Test
    void snakeTest(FxRobot robot) {
        robot.push(KeyCode.LEFT);
        robot.push(KeyCode.UP);
        robot.push(KeyCode.RIGHT);
        robot.push(KeyCode.DOWN);
    }
}
