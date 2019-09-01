package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.features.Pong;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.robot.Motion;

@ExtendWith(ApplicationExtension.class)
public class PongTest {

    /**
     * Creates a new Pong instance and starts it on the specified stage.
     *
     * @param stage The stage to start with.
     */
    @Start
    public void start(Stage stage) {
        Pong pong = new Pong();
        try {
            pong.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void pongTest(FxRobot robot) {
        robot.sleep(100);
        robot.clickOn("#onePlayer");
        robot.sleep(200);
        robot.clickOn("#canvas");
        robot.sleep(100);
        robot.moveTo(0,50, Motion.DEFAULT);
        robot.moveTo(0, 0, Motion.DEFAULT);
        robot.moveTo(0, -50, Motion.DEFAULT);
    }
}
