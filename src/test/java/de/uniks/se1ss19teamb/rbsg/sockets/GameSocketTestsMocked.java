package de.uniks.se1ss19teamb.rbsg.sockets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameSocketTestsMocked {

    private WebSocketClient client;

    @Before
    public void prepareClient() {
        client = mock(WebSocketClient.class);
    }

    private void setupSocket(String answer, AbstractWebSocket socket) {
        try {
            doAnswer(invocation -> {
                JsonParser parser = new JsonParser();
                JsonObject response = (JsonObject) parser.parse(answer);

                for (WebSocketMessageHandler handler : socket.handlers) {
                    handler.handle(response);
                }
                return null;
            }).when(client).sendMessage(any());
        } catch (Exception e) {
            e.printStackTrace();
        }

        socket.websocket = client;
    }

    @Test
    public void commandTest() throws ParseException {
        // TODO
    }

    @Test
    public void chatTest() throws ParseException {
        // TODO
        /* GameSocket gameSocket = new GameSocket("userKey", "gameId", "armyId");

        List<String> msg = new ArrayList<>();

        gameSocket.registerGameMessageHandler((message, from, isPrivate)
            -> msg.add(message + '|' + from + '|' + isPrivate));

        setupSocket("{\"channel\":\"all\",\"from\":\"TeamBTestUser\",\"message\":\"Hello World!\"}", gameSocket);
        gameSocket.sendMessage("Hello World!");

        setupSocket("{\"channel\":\"private\",\"from\":\"TeamBTestUser\",\"message\":\"Hello World! Private\"}",
            gameSocket);
        gameSocket.sendPrivateMessage("Hello World! Private", "TeamBTestUser2");

        Assert.assertTrue(msg.contains("Hello World!|TeamBTestUser|false"));
        Assert.assertTrue(msg.contains("Hello World! Private|TeamBTestUser|true")); */
    }

}
