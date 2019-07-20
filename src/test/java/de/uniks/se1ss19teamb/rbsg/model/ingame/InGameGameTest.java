package de.uniks.se1ss19teamb.rbsg.model.ingame;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InGameGameTest {
    private String id = "abc123";
    private String[] players = {"player1", "player2"};
    private String[] playersNew = {"player2", "player1"};
    private InGameGame inGameGame = new InGameGame();

    @Before
    public void prepareTests() {
        inGameGame.setId(id);
        inGameGame.setAllPlayer(players);
    }

    @Test
    public void inGameMetadataToStringTest() {
        Assert.assertArrayEquals(players, inGameGame.getAllPlayer());

        inGameGame.setAllPlayer(playersNew);

        Assert.assertArrayEquals(playersNew, inGameGame.getAllPlayer());
    }
}
