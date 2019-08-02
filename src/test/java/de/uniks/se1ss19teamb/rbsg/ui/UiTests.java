package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.Main;

import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

// UI Tests take a lot of time.
// To load new Scenes and finish actions javaFX needs time. All the sleeping time below is estimated and can
// probably be reduced if estimated properly.

public class UiTests extends ApplicationTest {

    @BeforeClass
    public static void setupHeadlessMode() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main main = new Main();
        main.start(stage);
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @Test
    @Ignore
    public void clickFullscreenTest() {
        clickOn("#btnFullscreen");
        clickOn("#btnFullscreen");
    }

    @Test
    @Ignore
    public void ticTacToeTest() {
        push(KeyCode.SHIFT).push(KeyCode.F1);
        sleep(500); // given some time to open window
        String[] buttons = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        for (String s : buttons) {
            clickOn(String.format("#%s", s));
        }
        clickOn("#btnReplay");
        for (int i = buttons.length - 1; i >= 0; i--) {
            clickOn(String.format("#%s", buttons[i]));
        }
    }

    @Test
    public void snakeTest() {
        press(KeyCode.SHIFT).press(KeyCode.F2);
        sleep(500); // given some time to open window
        push(KeyCode.LEFT);
        push(KeyCode.UP);
        push(KeyCode.RIGHT);
        push(KeyCode.DOWN);
    }

    @Test
    @Ignore
    public void notificationPopupTest() {

        Logger logger = LogManager.getLogger();

        Label el = (Label) lookup("#popup").queryAs(AnchorPane.class).lookup("Label");

        try {
            throw new Exception("Test Exception");
        } catch (Exception e) {
            NotificationHandler.getInstance().sendError("Test ERROR NullPointerException", logger, e);
            sleep(200);
            Assert.assertEquals(el.getText(), "Test ERROR NullPointerException");
            Assert.assertTrue(lookup("#errorContainer").queryAs(AnchorPane.class).isVisible());

            NotificationHandler.getInstance().sendError("Test ERROR NullPointerException Without e Exception", logger);
            sleep(200);
            Assert.assertEquals(el.getText(), "Test ERROR NullPointerException Without e Exception");
            Assert.assertTrue(lookup("#errorContainer").queryAs(AnchorPane.class).isVisible());

            NotificationHandler.getInstance().sendWarning("Test WARNING", logger);
            sleep(200);
            Assert.assertEquals(el.getText(), "Test WARNING");
            Assert.assertTrue(lookup("#errorContainer").queryAs(AnchorPane.class).isVisible());

            NotificationHandler.getInstance().sendInfo("Test INFO", logger);
            sleep(200);
            Assert.assertEquals(el.getText(), "Test INFO");
            Assert.assertTrue(lookup("#errorContainer").queryAs(AnchorPane.class).isVisible());

            NotificationHandler.getInstance().sendSuccess("Test SUCCESS", logger);
            sleep(200);
            Assert.assertEquals(el.getText(), "Test SUCCESS");
            Assert.assertTrue(lookup("#errorContainer").queryAs(AnchorPane.class).isVisible());
        }
    }
}