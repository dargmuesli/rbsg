package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.Main;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

class UiTests extends ApplicationTest {

    private Main main;

    @BeforeAll
    public static void setupHeadlessMode() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    @Override
    public void start(Stage stage) throws Exception {
        main = new Main();
        main.start(stage);
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
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
    void saveArmy() {
        clickOn("#userName");
        write("testTeamB");
        clickOn("#password");
        write("qwertz");
        clickOn("#btnLogin");
        sleep(2000);
        clickOn("#btnArmyManager");
        sleep(2000);
        clickOn("#txtfldArmyName");
        write("testArmy");
        clickOn("#btnSetArmyName");
        clickOn("#btnIncrease");
        clickOn("#btnIncrease");
        clickOn("#btnIncrease");
        clickOn("#btnIncrease");
        clickOn("#btnIncrease");
        clickOn("#btnIncrease");
        clickOn("#btnIncrease");
        clickOn("#btnIncrease");
        clickOn("#btnIncrease");
        clickOn("#btnIncrease");
        clickOn("#btnMinimize");
        clickOn("#btnSave1");
        clickOn("#btnSave2");
        clickOn("#btnSave3");
        clickOn("#btnSaveServer");
        clickOn("#btnLoadServer");
        clickOn("#btnChg");
        clickOn("#btnSave1");
        clickOn("#btnSave2");
        clickOn("#btnSave3");
        clickOn("#ham");
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnLogout");
    }

    @Test
    void loginMainTest() {
        clickOn("#userName");
        write("testTeamB");
        clickOn("#password");
        write("qwertz");
        clickOn("#btnLogin");
        sleep(2000);
        // chat
        clickOn("#message");
        write("/w me test");
        write("/all");
        clickOn("#btnSend");
        clickOn("#btnMinimize");
        clickOn("#btnMinimize");
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
        // logout
        clickOn("#ham");
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnMode");
        clickOn("#btnMode");
        clickOn("#btnLogout");
    }
}