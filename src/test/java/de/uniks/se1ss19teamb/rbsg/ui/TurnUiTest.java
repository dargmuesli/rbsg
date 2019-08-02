package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.ingame.InGameGame;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

class TurnUiTest extends ApplicationTest {

    private InGameGame inGameGame = new InGameGame();
    private InGamePlayer inGamePlayer1 = new InGamePlayer();
    private InGamePlayer inGamePlayer2 = new InGamePlayer();
    private TurnUiController turnUiController = new TurnUiController();

    @Override
    public void start(Stage stage) {
        try {
            stage.setScene(new Scene(FXMLLoader
                .load(getClass().getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/turnUi.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.show();
    }

    @Before
    public void before() {
        inGameGame.setId("123");
        inGamePlayer1.setName("Peter");
        inGamePlayer2.setName("Hans");
        InGameController.inGameObjects.put("player1", inGamePlayer1);
        InGameController.inGameObjects.put("player2", inGamePlayer2);
        InGameController.inGameObjects.put("Game", inGameGame);
    }

    @Test
    void updatePlayersTest() {
        Assert.assertEquals(0, turnUiController.inGamePlayerList.size());
        turnUiController.updatePlayers();
        Assert.assertEquals(2, turnUiController.inGamePlayerList.size());
    }
}
