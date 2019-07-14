package de.uniks.se1ss19teamb.rbsg.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InGameMetadataTest {
    private String id = "abc123";
    private String[] players = {"player1", "player2"};
    private String[] units = {"Tank", "Heavy Tank", "Bazooka Trooper", "Jeep"};
    private InGameMetadata inGameMetadata = new InGameMetadata();
    private String expected = "Id: abc123\nallPlayer: \"player1\" \"player2\" \nAllUnits: \"Tank\" \"Heavy Tank\" "
        + "\"Bazooka Trooper\" \"Jeep\" ";

    @Before
    public void prepareTests() {
        inGameMetadata.setId(id);
        inGameMetadata.setAllPlayer(players);
        inGameMetadata.setAllUnits(units);
    }

    @Test
    public void InGameMetadataToStringTest() {
        Assert.assertEquals(expected, inGameMetadata.toString());
    }
}
