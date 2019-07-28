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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

// UI Tests take a lot of time.
// To load new Scenes and finish actions javaFX needs time. All the sleeping time below is estimated and can
// probably be reduced if estimated properly.

class UiTests extends ApplicationTest {

    @BeforeAll
    static void setupHeadlessMode() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
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
    void clickFullscreenTest() {
        clickOn("#btnFullscreen");
        clickOn("#btnFullscreen");
    }

    @Test
    void falseLoginTest() {
        clickOn("#userName");
        write("");
        clickOn("#password");
        write("");
        clickOn("#rememberLogin");
        clickOn("#btnLogin");
    }

    @Test
    void registerTest() {
        clickOn("#btnRegistration");
        sleep(2000); // sleep to finish transition
        clickOn("#username");
        write("TeamBTestUser").push(KeyCode.ENTER);
        clickOn("#btnConfirm");
        clickOn("#password");
        write("qwertz").push(KeyCode.ENTER);
        clickOn("#btnConfirm");
        clickOn("#passwordRepeat");
        write("qwert").push(KeyCode.ENTER);
        clickOn("#btnConfirm");
        clickOn("#passwordRepeat");
        write("z").push(KeyCode.ENTER);
        clickOn("#btnConfirm");
        clickOn("#btnCancel");
        sleep(2000); // sleep to finish transition
    }

    @Test
    void loginMainTest() {
        clickOn("#userName");
        write("TeamBTestUser");
        clickOn("#password");
        write("qwertz");
        clickOn("#btnLogin");
        sleep(2000); // sleep to finish action
        // chat
        clickOn("#message");
        write("/all ");
        write("/w me test");
        clickOn("#btnSend");
        // game
        clickOn("#gameName");
        write("ayGame");
        clickOn("#btnCreate");
        sleep(500); // sleep to finish action
        ListView list = lookup("#gameListView").queryAs(ListView.class);
        HBox box;
        for (int i = 0; i < list.getItems().size(); i++) {
            box = (HBox) list.getItems().get(i);
            Label label = (Label) box.lookup("Label");
            if (label.getText().equals("ayGame")) {
                Button button = (Button) box.lookup("#delete");
                clickOn(button);
                sleep(500); // sleep to finish action
            }
        }
        // logout
        clickOn("#hamburgerMenu");
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnColorMode");
        clickOn("#btnColorMode");
        clickOn("#btnLogout");
        sleep(2000); // sleep to finish transition
    }

    @Test
    void ticTacToeTest() {
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
    void snakeTest() {
        press(KeyCode.SHIFT).press(KeyCode.F2);
        sleep(500); // given some time to open window
        push(KeyCode.LEFT);
        push(KeyCode.UP);
        push(KeyCode.RIGHT);
        push(KeyCode.DOWN);
    }

    @Test
    void notificationPopupTest() {

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