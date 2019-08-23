package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXComboBox;
import de.uniks.se1ss19teamb.rbsg.Main;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

public class UiTestsReal extends ApplicationTest {
    public static final String TEST_GAME = "TeamBTestGame";

    @Override
    public void start(Stage stage) throws Exception {
        Main main = new Main();
        main.start(stage);
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    private void leaveGameTest() {
        clickOn("#btnBack");
        sleep(500); // sleep to finish action
        AnchorPane ap = lookup("#leaveGame").queryAs(AnchorPane.class);
        Assert.assertTrue(ap.isVisible());
        clickOn("#btnNo");
        sleep(500); // sleep to finish action
        ap = lookup("#leaveGame").queryAs(AnchorPane.class);
        Assert.assertFalse(ap.isVisible());
        clickOn("#btnBack");
        sleep(500); // sleep to finish action
        clickOn("#btnYes");
    }

    // username and password: junit
    @Test
    @Disabled
    public void testInGame() {
        // player with UI
        clickOn("#txtUserName");
        write("TeamBTestUser2");
        clickOn("#password");
        write("qwertz");
        clickOn("#btnLogin");
        sleep(3000); // sleep to finish action

        //tests ui of armymanager
        armyManagerTest();

        //createGame
        clickOn("#gameName");
        write(TEST_GAME);
        clickOn("#btnCreate");
        sleep(2000);

        ListView<HBox> list = lookup("#gameListView").queryAs(ListView.class);
        HBox box = null;
        for (HBox gameField : list.getItems()) {
            Label label = (Label) gameField.getChildren().get(0);
            if (label.getText().equals(TEST_GAME)) {
                box = gameField;
                break;
            }
        }
        assert box != null;
        clickOn(box.getChildren().get(3));

        //in gamelobby
        sleep(4000); // sleep to finish action
        clickOn("#cmbArmies");
        sleep(2000);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        //TODO select a bot

        sleep(1000);
        clickOn("#tglReadiness");
        sleep(1000);
        clickOn("#btnStartGame");
        sleep(7000); // sleep to finish action

        GridPane gridPane = lookup("#gameGrid").queryAs(GridPane.class);
        StackPane stackPane = (StackPane) gridPane.getChildren().get(0);
        Assert.assertTrue(stackPane.getChildren().get(0) instanceof Pane);
        clickOn("#hamburgerMenu");
        sleep(1000); // sleep to finish action
        clickOn("#btnFullscreen");
        clickOn("#btnFullscreen");
        clickOn("#btnMiniMap");
        clickOn("#btnMiniMap");
        clickOn("#btnMinimize");
        clickOn("#message").write("Hello").clickOn("#btnSend");
        clickOn("#message").write("/w TeamBTestUser asd").clickOn("#btnSend");

        clickOn("#btnMinimize");
        clickOn("#chatWindow")
            .press(MouseButton.PRIMARY)
            .drag(targetWindow().getX() + targetWindow().getX() / 2, targetWindow().getY() * 2)
            .drop();

        //leaves game
        leaveGameTest();

        sleep(3000); // sleep to finish action
        ListView list2 = lookup("#gameListView").queryAs(ListView.class);
        HBox box2;
        for (int i = 0; i < list2.getItems().size(); i++) {
            box2 = (HBox) list2.getItems().get(i);
            Label label = (Label) box2.lookup("Label");
            if (label.getText().equals(TEST_GAME)) {
                Button button = (Button) box2.lookup("#delete");
                clickOn(button);
                sleep(500); // sleep to finish action
            }
        }
        sleep(500); // needed to avoid sception
        // logout
        clickOn("#hamburgerMenu");
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnLogout");
        sleep(2000); // sleep to finish transition
    }

    private void armyManagerTest() {
        //TODO import export tests
        clickOn("#btnArmyManager");
        sleep(4000); // sleep to finish transition
        clickOn("#btnAdd");
        sleep(200);
        clickOn("#txtfldArmyName");
        write("testArmy");

        for (int i = 0; i < 10; i++) {
            clickOn("+");
        }

        clickOn("#btnMinimize");
        clickOn("#btnMinimize");

        clickOn("#btnLogout");
        sleep(3000);

        clickOn("#btnSave");
        sleep(2000);
        clickOn("#btnEditIcon");
        clickOn("#btnBack");
        sleep(4000); // sleep to finish transition

        clickOn("#btnArmyManager");
        sleep(4000); // sleep to finish transition

        boolean armyExists = false;
        JFXComboBox armies = lookup("#cmbArmies").queryAs(JFXComboBox.class);

        int nodeCounter = 0;
        for (Object node : armies.getItems()) {
            if (node.toString().equals("testArmy")) {
                armyExists = true;
                break;
            }
            nodeCounter++;
        }
        Assert.assertTrue(armyExists);

        clickOn("#cmbArmies");
        sleep(1000);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn("#btnRemove");

        sleep(200);
        clickOn("#btnBack");
        sleep(4000); // sleep to finish transition
    }

    @Test
    public void falseLoginTest() {
        clickOn("#txtUserName");
        write("");
        clickOn("#password");
        write("");
        clickOn("#rememberLogin");
        clickOn("#btnLogin");
    }

    @Test
    public void registerTest() {
        clickOn("#btnRegistration");
        sleep(3000); // sleep to finish transition
        clickOn("#username");
        clickOn("#username");
        type(KeyCode.DELETE);
        write("TeamBTestUser").push(KeyCode.ENTER);
        clickOn("#btnConfirm");
        clickOn("#password");
        clickOn("#password");
        type(KeyCode.DELETE);
        write("qwertz").push(KeyCode.ENTER);
        clickOn("#btnConfirm");
        clickOn("#passwordRepeat");
        clickOn("#passwordRepeat");
        type(KeyCode.DELETE);
        write("qwert").push(KeyCode.ENTER);
        clickOn("#btnConfirm");
        clickOn("#passwordRepeat");
        write("z").push(KeyCode.ENTER);
        clickOn("#btnConfirm");
        clickOn("#btnCancel");
        sleep(3000); // sleep to finish transition
    }

    @Test
    public void loginMainTest() {
        clickOn("#txtUserName");
        write("TeamBTestUser");
        clickOn("#password");
        write("qwertz");
        clickOn("#btnLogin");
        sleep(4000); // sleep to finish action
        //fullscreen
        clickOn("#hamburgerMenu");
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnFullscreen");
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
        clickOn("#btnColorMode");
        clickOn("#btnColorMode");
        clickOn("#btnLogout");
        sleep(2500); // sleep to finish transition
    }
}
