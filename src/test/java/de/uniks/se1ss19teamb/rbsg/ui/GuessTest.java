package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.features.Guess;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class GuessTest {
    @Start
    public void start(Stage stage) throws Exception {
        Guess guess = new Guess();
        guess.start(stage);

    }

    @Test
    void guessTest(FxRobot robot) {
        robot.sleep(200);
        robot.clickOn("#btnNewGame");
        robot.sleep(200);
        robot.clickOn("#numberField");
        robot.write("1");
        robot.clickOn("#btnStart");
        robot.sleep(200);
        robot.clickOn("#numberField");
        robot.sleep(200);
        robot.write("2");
        robot.clickOn("#btnStart");
        robot.sleep(200);
        robot.clickOn("#numberField");
        robot.sleep(200);
        robot.write("3");
        robot.clickOn("#btnStart");
        robot.sleep(200);
        robot.clickOn("#numberField");
        robot.sleep(200);
        robot.write("4");
        robot.clickOn("#btnStart");
        robot.sleep(200);
    }
}
