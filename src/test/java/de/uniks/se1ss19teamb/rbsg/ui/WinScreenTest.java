package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.ingame.InGameGame;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class WinScreenTest {
    private InGameGame inGameGame = new InGameGame();
    private InGamePlayer inGamePlayer1 = new InGamePlayer();
    private InGamePlayer inGamePlayer2 = new InGamePlayer();
    private WinScreenController winScreenController = new WinScreenController();

    @Test
    void updatePlayersTest()  {
        InGameController.inGameObjects.put("Game", inGameGame);
        InGameController.inGameObjects.put("player1", inGamePlayer1);
        InGameController.inGameObjects.put("player2", inGamePlayer2);
        Assert.assertEquals(0, winScreenController.inGamePlayerList.size());
        Assert.assertEquals(WinScreenController.getInstance(), WinScreenController.getInstance());
        winScreenController.updatePlayers();
        Assert.assertEquals(2, winScreenController.inGamePlayerList.size());
    }

    @Test
    void calculateWinner() {
        InGameController.inGameObjects.put("Game", inGameGame);
        InGameController.inGameObjects.put("player1", inGamePlayer1);
        InGameController.inGameObjects.put("player2", inGamePlayer2);
        inGamePlayer1.setId("loser");
        inGamePlayer2.setId("loser");
        winScreenController.updatePlayers();
        winScreenController.calculateWinner("winner");
    }
}
