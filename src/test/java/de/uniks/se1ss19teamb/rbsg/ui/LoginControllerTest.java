package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.Main;

import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

// TODO headless mode

class LoginControllerTest extends ApplicationTest {

    private Main main;

    @BeforeClass
    public static void setupHeadlessMode() {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        main = new Main();
        main.start(stage);
    }

    @Test
    void contentTest() {
        Assertions.assertThat(lookup("#btnLogin").queryAs(JFXButton.class)).hasText("Login");
        Assertions.assertThat(lookup("#btnRegistration").queryAs(JFXButton.class)).hasText("Registration");
        Assertions.assertThat(lookup("#rememberLogin").queryAs(JFXCheckBox.class)).hasText("Remember Login");
        Assertions.assertThat(lookup("#password").queryAs(JFXPasswordField.class)).isVisible();
        Assertions.assertThat(lookup("#userName").queryAs(JFXTextField.class)).isVisible();
        Assertions.assertThat(lookup("#btnFullscreen").queryAs(JFXButton.class)).hasText("Fullscreen");
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
        sleep(2000);
        Assertions.assertThat(lookup("#errorContainer").queryAs(AnchorPane.class)).isVisible();
        Assertions.assertThat(lookup("#username").queryAs(JFXTextField.class)).isVisible();
        Assertions.assertThat(lookup("#password").queryAs(JFXPasswordField.class)).isVisible();
        Assertions.assertThat(lookup("#passwordRepeat").queryAs(JFXPasswordField.class)).isVisible();
        clickOn("#btnConfirm");
        clickOn("#btnCancel");
    }

    @Test
    void loginMainTest() {
        clickOn("#userName");
        write("MyNewName");
        clickOn("#password");
        write("blutangel90");
        clickOn("#rememberLogin");
        clickOn("#btnLogin");
        sleep(2000);
        // chat
        clickOn("#message");
        write("/w me test");
        clickOn("#btnSend");
        // game
        clickOn("#gameName");
        write("ayGame");
        clickOn("#btnCreate");
        ListView list = lookup("#gameListView").queryAs(ListView.class);
        HBox box = (HBox) list.getItems().get(list.getItems().size() - 1);
        // ingame
        clickOn(box.getChildren().get(1));
        sleep(2000);
        clickOn("#ham");
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnBack");
        sleep(2000);
        list = lookup("#gameListView").queryAs(ListView.class);
        box = (HBox) list.getItems().get(list.getItems().size() - 1);
        clickOn(box.getChildren().get(2));
        // army
        clickOn("#btnArmyManager");
        sleep(2000);
        clickOn("#ham");
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnBack");
        sleep(2000);
        // logout
        clickOn("#ham");
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnLogout");
    }
}
