package de.uniks.se1ss19teamb.rbsg.ui;

import static java.lang.Thread.sleep;

import de.uniks.se1ss19teamb.rbsg.Main;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

// UI Tests take a lot of time.
// To load new Scenes and finish actions javaFX needs time. All the sleeping time below is estimated and can
// probably be reduced if estimated properly.

@ExtendWith(ApplicationExtension.class)
class UiTests {

    @Start
    public void start(Stage stage) throws Exception {
        Main main = new Main();
        main.start(stage);
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @Test
    void clickFullscreenTest(FxRobot robot) {
        robot.clickOn("#btnFullscreen");
        robot.clickOn("#btnFullscreen");
    }

    @Test
    void notificationPopupTest(FxRobot robot) throws InterruptedException {

        Logger logger = LogManager.getLogger();

        try {
            throw new Exception("Test Exception");
        } catch (Exception e) {
            NotificationHandler.getInstance().sendError("Test ERROR NullPointerException", logger, e);
            sleep(200);

            Label el = (Label) robot.lookup("#apnRoot").queryAs(AnchorPane.class).lookup("#label");
            Assert.assertEquals(el.getText(), "Test ERROR NullPointerException");
            Assert.assertTrue(robot.lookup("#errorContainer").queryAs(AnchorPane.class).isVisible());

            NotificationHandler.getInstance().sendError("Test ERROR NullPointerException Without e Exception", logger);
            sleep(200);
            Assert.assertEquals(el.getText(), "Test ERROR NullPointerException Without e Exception");
            Assert.assertTrue(robot.lookup("#errorContainer").queryAs(AnchorPane.class).isVisible());

            NotificationHandler.getInstance().sendWarning("Test WARNING", logger);
            sleep(200);
            Assert.assertEquals(el.getText(), "Test WARNING");
            Assert.assertTrue(robot.lookup("#errorContainer").queryAs(AnchorPane.class).isVisible());

            NotificationHandler.getInstance().sendInfo("Test INFO", logger);
            sleep(200);
            Assert.assertEquals(el.getText(), "Test INFO");
            Assert.assertTrue(robot.lookup("#errorContainer").queryAs(AnchorPane.class).isVisible());

            NotificationHandler.getInstance().sendSuccess("Test SUCCESS", logger);
            sleep(200);
            Assert.assertEquals(el.getText(), "Test SUCCESS");
            Assert.assertTrue(robot.lookup("#errorContainer").queryAs(AnchorPane.class).isVisible());
        }
    }
}