package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.Main;
import de.uniks.se1ss19teamb.rbsg.TestUtil;
import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.RequestUtil;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

public class UiTestsReal extends ApplicationTest {

    @BeforeClass
    public static void setupHeadlessMode() {
        TestUtil.setupHeadlessMode();
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
    @Ignore
    public void testInGame() {
        RestRequestTestsReal.loginUser();
        RestRequestTestsReal.createGame();

        if (!RequestUtil.request(new JoinGameRequest(RestRequestTestsReal.gameId, RestRequestTestsReal.userToken))) {
            Assert.fail();
        }

        RestRequestTestsReal.queryUnits();

        GameSocket gameSocket = new GameSocket(
            RestRequestTestsReal.gameId,
            null,
            false);

        gameSocket.connect();
        gameSocket.changeArmy(RestRequestTestsReal.unitList.get(0).getId());
        gameSocket.readyToPlay();

        // player with UI
        clickOn("#txtUserName");
        write("TeamBTestUser2");
        clickOn("#password");
        write("qwertz");
        clickOn("#btnLogin");
        sleep(3000); // sleep to finish action
        clickOn("#btnArmyManager");
        sleep(2000); // sleep to finish transition
        clickOn("#txtfldArmyName");
        write("testArmy");
        clickOn("#btnSetArmyName");
        for (int i = 0; i < 10; i++) {
            clickOn("+");
        }
        clickOn("#btnMinimize");
        clickOn("#btnMinimize");
        clickOn("#btnLoadServer");
        clickOn("#btnSave1");
        clickOn("#btnSave2");
        clickOn("#btnSave3");
        sleep(2600); // wating for notification do disappear
        clickOn("Save/Load");
        clickOn("#btnSave1");
        clickOn("#btnSave2");
        clickOn("#btnSave3");
        clickOn("#btnSaveServer");
        sleep(2600); // wating for notification do disappear
        clickOn("#btnBack");
        sleep(2000); // sleep to finish transition
        ListView<HBox> list = lookup("#gameListView").queryAs(ListView.class);
        HBox box = null;
        for (HBox gameField : list.getItems()) {
            Label label = (Label) gameField.getChildren().get(0);
            if (label.getText().equals("junitTestGameB")) {
                box = gameField;
                break;
            }
        }
        assert box != null;
        clickOn(box.getChildren().get(1));
        sleep(2000); // sleep to finish action
        clickOn("#select1");
        clickOn("#btnMyReady");
        clickOn("#btnStart");
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
        gameSocket.leaveGame();
        gameSocket.disconnect();
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
            if (label.getText().equals("junitTestGameB")) {
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
    public void loginMainTest() {
        clickOn("#txtUserName");
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
}
