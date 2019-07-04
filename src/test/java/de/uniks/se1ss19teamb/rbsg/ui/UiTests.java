package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.Main;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
        clickOn("#username");
        write("testTeamB").push(KeyCode.ENTER);
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
    }

    @Test
    void saveArmyTest() {
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
        clickOn("#btnLoadServer");
        clickOn("#btnSaveServer");
        clickOn("#btnChg");
        clickOn("#btnSave1");
        clickOn("#btnSave2");
        clickOn("#btnSave3");
        clickOn("#ham");
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnLogout");
    }

    @Test
    void inGameTest() {
        clickOn("#userName");
        write("testTeamB");
        clickOn("#password");
        write("qwertz");
        clickOn("#btnLogin");
        sleep(2000);
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
        clickOn("#btnFullscreen");
        clickOn("#btnFullscreen");
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
        write("/all ");
        write("/w me test");
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

    // username and password: junit
    @Test
    void testInGame() {
        clickOn("#userName");
        write("junit");
        clickOn("#password");
        write("junit");
        clickOn("#btnLogin");
        sleep(5000);
        clickOn("#gameName");
        write("junitTestGameB");
        clickOn("#btnCreate");
        sleep(2000);
        ListView<HBox> list = lookup("#gameListView").queryAs(ListView.class);
        HBox box = null;
        for (HBox gameField : list.getItems()) {
            Label label = (Label) gameField.getChildren().get(0);
            if (label.getText().equals("junitTestGameB")) {
                box = gameField;
                break;
            }
        }
        clickOn(box.getChildren().get(1));
        sleep(2000);
        clickOn("#btnLoadServer");
        sleep(1000);
        HBox btnBox = lookup("#hboxLowerButtons").queryAs(HBox.class);
        clickOn(btnBox.getChildren().get(2));
        sleep(10000);
    }

}