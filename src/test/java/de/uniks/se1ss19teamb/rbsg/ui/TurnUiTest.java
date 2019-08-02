package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.ingame.InGameGame;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
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
      InGameController.inGameObjects.put("player", inGamePlayer1);
      InGameController.inGameObjects.put("player", inGamePlayer2);
      InGameController.inGameObjects.put("game", inGameGame);
    }

    @Test
    public void updatePlayerTest() {
        turnUiController.updatePlayers();
        Assert.assertEquals(2, turnUiController.inGamePlayerList.size());
    }

}
