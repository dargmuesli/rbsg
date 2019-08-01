package de.uniks.se1ss19teamb.rbsg.sockets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class GameSocketTestsMocked {
    private final String gameId = "myGameId";
    private final String armyId = "myArmyId";
    private final boolean spectator = false;

    private ArgumentCaptor<JsonObject> argumentCaptor = ArgumentCaptor.forClass(JsonObject.class);
    private GameSocket gameSocket = spy(new GameSocket(gameId, armyId, spectator));

    @Before
    public void prepareTests() {
        doNothing().when(gameSocket).sendToWebsocket(any());
    }

    @Test
    public void getEndpointTest() {
        Assert.assertEquals("/game?gameId=myGameId&armyId=myArmyId&spectator=false", gameSocket.getEndpoint());

        GameSocket gameSocketSpectator = spy(new GameSocket(gameId, armyId, true));

        Assert.assertEquals("/game?gameId=myGameId&spectator=true", gameSocketSpectator.getEndpoint());
    }

    @Test
    public void changeArmyTest() {
        gameSocket.changeArmy("123456789");
        verify(gameSocket).sendToWebsocket(argumentCaptor.capture());
        Assert.assertEquals(
            "{\"messageType\":\"command\",\"action\":\"changeArmy\",\"data\":\"123456789\"}",
            argumentCaptor.getValue().toString());
    }

    @Test
    public void leaveGameTest() {
        gameSocket.leaveGame();
        verify(gameSocket).sendToWebsocket(argumentCaptor.capture());
        Assert.assertEquals(
            "{\"messageType\":\"command\",\"action\":\"leaveGame\"}",
            argumentCaptor.getValue().toString());
    }

    @Test
    public void readyToPlayTest() {
        gameSocket.readyToPlay();
        verify(gameSocket).sendToWebsocket(argumentCaptor.capture());
        Assert.assertEquals(
            "{\"messageType\":\"command\",\"action\":\"readyToPlay\"}",
            argumentCaptor.getValue().toString());
    }

    @Test
    public void startGameTest() {
        gameSocket.startGame();
        verify(gameSocket).sendToWebsocket(argumentCaptor.capture());
        Assert.assertEquals(
            "{\"messageType\":\"command\",\"action\":\"startGame\"}",
            argumentCaptor.getValue().toString());
    }

    @Test
    public void moveUnitTest() {
        gameSocket.moveUnit("123456789", new String[]{"987", "654", "321"});
        verify(gameSocket).sendToWebsocket(argumentCaptor.capture());
        Assert.assertEquals(
            "{\"messageType\":\"command\",\"action\":\"moveUnit\","
                + "\"data\":{\"unitId\":\"123456789\",\"path\":[\"987\",\"654\",\"321\"]}}",
            argumentCaptor.getValue().toString());
    }

    @Test
    public void attackUnitTest() {
        gameSocket.attackUnit("123456789", "987654321");
        verify(gameSocket).sendToWebsocket(argumentCaptor.capture());
        Assert.assertEquals(
            "{\"messageType\":\"command\",\"action\":\"attackUnit\","
                + "\"data\":{\"unitId\":\"123456789\",\"toAttackId\":\"987654321\"}}",
            argumentCaptor.getValue().toString());
    }

    @Test
    public void nextPhaseTest() {
        gameSocket.nextPhase();
        verify(gameSocket).sendToWebsocket(argumentCaptor.capture());
        Assert.assertEquals(
            "{\"messageType\":\"command\",\"action\":\"nextPhase\"}",
            argumentCaptor.getValue().toString());
    }

    @Test
    public void sendMessageTest() {
        gameSocket.sendMessage("hello");
        verify(gameSocket).sendToWebsocket(argumentCaptor.capture());
        Assert.assertEquals(
            "{\"messageType\":\"chat\",\"channel\":\"all\",\"message\":\"hello\"}",
            argumentCaptor.getValue().toString());
    }

    @Test
    public void sendPrivateMessageTest() {
        gameSocket.sendPrivateMessage("hello", "toYou");
        verify(gameSocket).sendToWebsocket(argumentCaptor.capture());
        Assert.assertEquals(
            "{\"messageType\":\"chat\",\"channel\":\"private\",\"to\":\"toYou\",\"message\":\"hello\"}",
            argumentCaptor.getValue().toString());
    }
}
