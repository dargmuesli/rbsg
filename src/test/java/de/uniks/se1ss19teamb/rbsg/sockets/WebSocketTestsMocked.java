package de.uniks.se1ss19teamb.rbsg.sockets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WebSocketTestsMocked {
    
    WebSocketClient client;
    
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
    public void systemSocketTest() throws ParseException, IOException, InterruptedException {
        SystemSocket system = new SystemSocket("111111111111111111111111111111111111");
        
        List<String> msg = new ArrayList<>();
        
        system.registerUserJoinHandler((name) -> msg.add("userJoin|" + name));
        
        system.registerUserLeftHandler((name) -> msg.add("userLeft|" + name));
        
        system.registerGameCreateHandler((name, id, neededPlayers)
            -> msg.add("gameCreate|" + name + '|' + id + '|' + neededPlayers));
        
        system.registerGameDeleteHandler((id) -> msg.add("gameDelete|" + id));
        
        setupSocket("{\"action\":\"userJoined\",\"data\":{\"name\":\"testTeamB2\"}}", system);
        system.sendToWebsocket(null);
        
        setupSocket("{\"action\":\"userLeft\",\"data\":{\"name\":\"testTeamB2\"}}", system);
        system.sendToWebsocket(null);
        
        setupSocket("{\"action\":\"gameCreated\",\"data\":{\"name\":\"testTeamBGame\",\""
                + "id\":\"123456789012345678901234\",\"neededPlayer\":2}}", system);
        system.sendToWebsocket(null);
        
        setupSocket("{\"action\":\"gameDeleted\",\"data\":{\"id\":\"123456789012345678901234\"}}", system);
        system.sendToWebsocket(null);
        
        system.disconnect();
        
        System.out.println(msg);
        
        Assert.assertTrue(msg.contains("userJoin|testTeamB2"));
        Assert.assertTrue(msg.contains("userLeft|testTeamB2"));
        Assert.assertTrue(msg.contains("gameCreate|testTeamBGame|" + "123456789012345678901234" + "|2"));
        Assert.assertTrue(msg.contains("gameDelete|" + "123456789012345678901234"));
        
    }
    
    @Test
    public void chatSocketTest() throws ParseException, IOException, InterruptedException {

        ChatSocket chat = new ChatSocket("testTeamB2", "111111111111111111111111111111111111");
        
        List<String> msg = new ArrayList<>();
        
        chat.registerChatMessageHandler((message, from, isPrivate) -> msg.add(message + '|' + from + '|' + isPrivate));
        
        setupSocket("{\"channel\":\"all\",\"from\":\"testTeamB\",\"message\":\"Hello World!\"}", chat);
        chat.sendMessage("Hello World!");

        setupSocket("{\"channel\":\"private\",\"from\":\"testTeamB\",\"message\":\"Hello World! Private\"}", chat);
        chat.sendPrivateMessage("Hello World! Private", "testTeamB2");

        
        Assert.assertTrue(msg.contains("Hello World!|testTeamB|false"));
        Assert.assertTrue(msg.contains("Hello World! Private|testTeamB|true"));
    }
    
}
