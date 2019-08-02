package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.ingame.InGameGame;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import javafx.scene.control.Label;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TurnUiTest {
    public InGamePlayer inGamePlayer1 = new InGamePlayer();
    public InGamePlayer inGamePlayer2 = new InGamePlayer();
    public InGameGame inGameGame = new InGameGame();
    TurnUiController turnUiController = new TurnUiController();

    @Before
    public void setUpTest() {
      inGameGame.setId("123");
      inGamePlayer1.setName("Peter");
      inGamePlayer2.setName("Hans");
      InGameController.inGameObjects.put("player1", inGamePlayer1);
      InGameController.inGameObjects.put("player3", inGamePlayer2);
      InGameController.inGameObjects.put("Game", inGameGame);
      turnUiController.updatePlayers();
    }

    @Test
    public void updatePlayerTest() {
        Assert.assertEquals(2, turnUiController.inGamePlayerList.size());
    }

}
