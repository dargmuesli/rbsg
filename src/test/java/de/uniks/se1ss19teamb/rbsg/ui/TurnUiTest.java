package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.ingame.InGameGame;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class TurnUiTest {

    private InGameGame inGameGame = new InGameGame();
    private InGamePlayer inGamePlayer1 = new InGamePlayer();
    private InGamePlayer inGamePlayer2 = new InGamePlayer();
    private TurnUiController turnUiController = new TurnUiController();

    @Test
    void updatePlayersTest()  {
        InGameController.inGameObjects.clear();
        Assert.assertEquals(0, turnUiController.inGamePlayerList.size());
        InGameController.inGameObjects.put("Game", inGameGame);
        turnUiController.updatePlayers();
        Assert.assertEquals(0, turnUiController.inGamePlayerList.size());
    }

    @Test
    void showTurnTest() {
        inGameGame.setId("123");
        inGamePlayer1.setName("Peter");
        inGamePlayer1.setId("blub2");
        inGamePlayer2.setName("Hans");
        inGamePlayer2.setId("blub");
        turnUiController.inGamePlayerList.add(0, inGamePlayer1);
        turnUiController.inGamePlayerList.add(1, inGamePlayer2);
        turnUiController.showTurn("123");
        Assert.assertEquals(2, turnUiController.inGamePlayerList.size());
    }
}
