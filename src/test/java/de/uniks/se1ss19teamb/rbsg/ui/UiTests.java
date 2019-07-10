package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.Main;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

// UI Tests take a lot of time.
// To load new Scenes and finish actions javaFX needs time. All the sleeping time below is estimated and can
// probably be reduced if estimated properly.

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
        sleep(2000); // sleep to finisch transition
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
        sleep(2000); // sleep to finisch transition
    }

    @Test
    void saveArmyTest() {
        clickOn("#userName");
        write("testTeamB");
        clickOn("#password");
        write("qwertz");
        clickOn("#btnLogin");
        sleep(2000); // sleep to finisch transition
        clickOn("#btnArmyManager");
        sleep(2000); // sleep to finisch transition
        clickOn("#txtfldArmyName");
        write("testArmy");
        clickOn("#btnSetArmyName");
        for (int i = 0; i < 10; i++) {
            clickOn("+");
        }
        clickOn("#btnMinimize");
        clickOn("#btnMinimize");
        clickOn("#btnSave1");
        clickOn("#btnSave2");
        clickOn("#btnSave3");
        sleep(2600); // wating for notification do disappear
        clickOn("Save/Load");
        clickOn("#btnSave1");
        clickOn("#btnSave2");
        clickOn("#btnSave3");
        clickOn("#btnLoadServer");
        clickOn("#btnSaveServer");
        clickOn("#btnLogout");
        sleep(2000); // sleep to finisch transition
    }

    @Test
    void loginMainTest() {
        clickOn("#userName");
        write("testTeamB2");
        clickOn("#password");
        write("qwertz");
        clickOn("#btnLogin");
        sleep(2000); // sleep to finisch action
        // chat
        clickOn("#message");
        write("/all ");
        write("/w me test");
        clickOn("#btnSend");
        // game
        clickOn("#gameName");
        write("ayGame");
        clickOn("#btnCreate");
        sleep(500); // sleep to finisch action
        ListView list = lookup("#gameListView").queryAs(ListView.class);
        HBox box;
        for (int i = 0; i < list.getItems().size(); i++) {
            box = (HBox) list.getItems().get(i);
            Label label = (Label) box.lookup("Label");
            if (label.getText().equals("ayGame")) {
                Button button = (Button) box.lookup("#delete");
                clickOn(button);
                sleep(500); // sleep to finisch action
            }
        }
        // logout
        clickOn("#hamburgerMenu");
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnColorMode");
        clickOn("#btnColorMode");
        clickOn("#btnLogout");
        sleep(2000); // sleep to finisch transition
    }

    // username and password: junit
    @Test
    void testInGame() {
        clickOn("#userName");
        write("junit");
        clickOn("#password");
        write("junit");
        clickOn("#btnLogin");
        sleep(5000); // sleep to finisch action
        clickOn("#gameName");
        write("junitTestGameB");
        clickOn("#btnCreate");
        sleep(2000); // sleep to finisch action
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
        sleep(2000); // sleep to finisch action
        clickOn("#btnLoadServer");
        sleep(1000); // sleep to finisch action
        HBox btnBox = lookup("#hboxLowerButtons").queryAs(HBox.class);
        clickOn(btnBox.getChildren().get(2));
        sleep(7000); // sleep to finisch action
        GridPane gridPane = lookup("#gameGrid").queryAs(GridPane.class);
        StackPane stackPane = (StackPane) gridPane.getChildren().get(0);
        Assert.assertTrue(stackPane.getChildren().get(0) instanceof Pane);
        clickOn("#hamburgerMenu");
        sleep(1000); // sleep to finisch action
        clickOn("#btnFullscreen");
        clickOn("#btnFullscreen");
        clickOn("#chatWindow")
            .press(MouseButton.PRIMARY)
            .drag(targetWindow().getX() + targetWindow().getX() / 2, targetWindow().getY() * 2)
            .drop();
        clickOn("#btnBack");
        sleep(3000); // sleep to finisch action
        ListView list2 = lookup("#gameListView").queryAs(ListView.class);
        HBox box2;
        for (int i = 0; i < list2.getItems().size(); i++) {
            box2 = (HBox) list2.getItems().get(i);
            Label label = (Label) box2.lookup("Label");
            if (label.getText().equals("junitTestGameB")) {
                Button button = (Button) box2.lookup("#delete");
                clickOn(button);
                sleep(500); // sleep to finisch action
            }
        }
        sleep(500); // needed to avoid sception
        // logout
        clickOn("#hamburgerMenu");
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnLogout");
        sleep(2000); // sleep to finisch transition
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

}