package de.uniks.se1ss19teamb.rbsg.sockets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.uniks.se1ss19teamb.rbsg.ui.InGameController;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class WebSocketTestsMocked {

    private WebSocketClient client = mock(WebSocketClient.class);
    private GameSocket gameSocket = new GameSocket("54321", "12543", false);

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
    void systemSocketTest() throws ParseException {
        SystemSocket system = new SystemSocket();

        List<String> msg = new ArrayList<>();

        system.registerUserJoinHandler((name) -> msg.add("userJoin|" + name));

        system.registerUserLeftHandler((name) -> msg.add("userLeft|" + name));

        system.registerGameCreateHandler((name, id, neededPlayers)
            -> msg.add("gameCreate|" + name + '|' + id + '|' + neededPlayers));

        system.registerGameDeleteHandler((id) -> msg.add("gameDelete|" + id));

        system.registerPlayerJoinedGameHandler((id, joinedPlayer)
            -> msg.add("playerJoinedGame|" + id + "|" + joinedPlayer));

        setupSocket("{\"action\":\"userJoined\",\"data\":{\"name\":\"TeamBTestUser2\"}}", system);
        system.sendToWebsocket(null);

        setupSocket("{\"action\":\"userLeft\",\"data\":{\"name\":\"TeamBTestUser2\"}}", system);
        system.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameCreated\",\"data\":{\"name\":\"TeamBTestUserGame\",\""
            + "id\":\"123456789012345678901234\",\"neededPlayer\":2}}", system);
        system.sendToWebsocket(null);

        setupSocket("{\"action\":\"playerJoinedGame\","
            + "\"data\":{\"id\":\"123456789012345678901234\", \"joinedPlayer\":2}}", system);
        system.sendToWebsocket(null);

        setupSocket("{\"action\":\"playerLeftGame\",\"data\":{\"id\":\"123456789012345678901234\", "
            + "\"joinedPlayer\":0}}", system);
        system.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameDeleted\",\"data\":{\"id\":\"123456789012345678901234\"}}",
            system);
        system.sendToWebsocket(null);

        system.disconnect();

        System.out.println(msg);

        Assert.assertTrue(msg.contains("userJoin|TeamBTestUser2"));
        Assert.assertTrue(msg.contains("userLeft|TeamBTestUser2"));
        Assert.assertTrue(msg.contains("gameCreate|TeamBTestUserGame|" + "123456789012345678901234" + "|2"));
        Assert.assertTrue(msg.contains("gameDelete|" + "123456789012345678901234"));
        Assert.assertTrue(msg.contains("playerJoinedGame|" + "123456789012345678901234" + "|2"));
        Assert.assertTrue(msg.contains("playerJoinedGame|" + "123456789012345678901234" + "|0"));

    }

    @Test
    void gameSocketTest() {

        List<String> gameMsg = new ArrayList<>();

        gameSocket.registerGameRemoveObject((type -> gameMsg.add("removed|" + type)));

        gameSocket.registerGameChangeObject((type -> gameMsg.add("changed|" + type)));

        //info
        setupSocket("{\"action\":\"info\",\"data\":{"
            + "\"message\":\"You have no army with the given id.\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"info\",\"data\":{"
            + "\"message\":\"Initialize game, sending start situation...\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"info\",\"data\":{\"message\":\"You already joined a game.\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"info\",\"data\":{\"message\":\"lolol\n\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"inGameError\",\"data\":\"lolol\n\"}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"msg\":\"lolol\n\"}", gameSocket);
        gameSocket.sendToWebsocket(null);

        //gameInitObject
        setupSocket("{\"action\":\"gameStarts\",\"data\":{\"message\":\"lolol\n\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameInitObject\",\"data\":{\"id\":\"Game@792cd9ce\","
            + "\"allPlayer\":[\"Player@7c047350\"]}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameInitObject\",\"data\":{\"id\":\"Grass@258b12ff\","
            + "\"game\":\"Game@792cd9ce\",\"x\":\"0\",\"y\":\"0\",\"isPassable\":\"true\","
            + "\"right\":\"Grass@1c4ac197\",\"bottom\":\"Grass@534e9ecc\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameInitObject\",\"data\":{\"id\":\"Player@7c047350\","
            + "\"name\":\"TeamBTestUser\",\"color\":\"RED\","
            + "\"isReady\":\"false\",\"currentGame\":\"Game@792cd9ce\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameInitObject\",\"data\":{\"id\":\"Trash@258b12ff\","
            + "\"game\":\"Trash@792cd9ce\",\"x\":\"1\",\"y\":\"1\",\"isPassable\":\"true\","
            + "\"right\":\"Trash@1c4ac197\",\"bottom\":\"Trash@534e9ecc\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        //TODO: Fix Platform.runLater for Travis
        //setupSocket("{\"action\":\"gameInitFinished\",\"data\":{}}", gameSocket);
        //gameSocket.sendToWebsocket(null);

        //gameNewObject

        setupSocket("{\"action\":\"gameStarts\",\"data\":{}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameNewObject\",\"data\":{\"id\":\"Unit@1923617\","
            + "\"type\":\"Chopper\",\"mp\":\"6\",\"hp\":\"10\",\"canAttack\":[\"Infanctry\","
            + "\"Bazooka Trooper\",\"Jeep\",\"Light Tank\",\"Heavy Tank\"],"
            + "\"game\":\"Game@52f9b3f7\",\"leader\":\"Player@1c5f2a09\","
            + "\"position\":\"Grass@258b12ff\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameNewObject\",\"data\":{\"id\":\"Player@1923617\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameNewObject\",\"data\":{\"id\":\"damnTrashcan\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        //gameChangeObject
        setupSocket("{\"action\":\"gameChangeObject\",\"data\":{\"id\":\"Unit@1923617\",\"fieldName\":\"hp\",\""
            + "newValue\":\"5\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameChangeObject\",\"data\":{\"id\":\"Unit@1923617\",\"fieldName\":\"position\",\""
            + "newValue\":\"Grass@258b12ff\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameChangeObject\",\"data\":{\"id\":\"Player@7c047350\",\"fieldName\":\"isReady\",\""
            + "newValue\":\"true\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);


        //TODO: Fix Platform.runLater for Travis
        /*setupSocket("{\"action\":\"gameChangeObject\",\"data\":{\"id\":\"Game@29f70a3b\", "
            + "\"fieldName\":\"phase\",\"newValue\":\"movePhase\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameChangeObject\",\"data\":{\"id\":\"Game@29f70a3b\", "
            + "\"fieldName\":\"currentPlayer\",\"newValue\":\"Player@7c047350\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);*/

        setupSocket("{\"action\":\"gameChangeObject\",\"data\":{\"id\":\"OtherOther@29f70a3b\","
            + "\"fieldName\":\"position\",\"newValue\":\"5\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        //gameRemoveObject

        setupSocket("{\"action\":\"gameRemoveObject\",\"data\":{\"id\":\"Unit@111\",\"from\":\"Game@37392bfa\",\""
            + "fieldName\":\"allUnits\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameRemoveObject\",\"data\":{\"id\":\"OtherOther@29f70a3b\",\"from\":\""
            + "Game@37392bfa\",\"fieldName\":\"allUnits\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        setupSocket("{\"action\":\"gameRemoveObject\",\"data\":{\"id\":\"Player@12a35f8e\",\"from\":\""
            + "Game@37392bfa\",\"fieldName\":\"allUnits\"}}", gameSocket);
        gameSocket.sendToWebsocket(null);

        Assert.assertTrue(gameMsg.contains("removed|Player"));
        Assert.assertTrue(gameMsg.contains("removed|Unit"));
        Assert.assertTrue(gameMsg.contains("removed|Unit"));
        Assert.assertTrue(gameMsg.contains("changed|Unit"));
        Assert.assertTrue(gameMsg.contains("changed|Unit"));
        Assert.assertTrue(gameMsg.contains("changed|Player"));
        Assert.assertTrue(gameMsg.contains("changed|Player"));
        //Assert.assertTrue(gameMsg.contains("changed|Game"));
        Assert.assertFalse(InGameController.environmentTiles.isEmpty());
        Assert.assertFalse(InGameController.unitTiles.isEmpty());
        Assert.assertEquals("Grass@258b12ff", InGameController.unitTiles.get(0).getPosition());

    }

    @Test
    void chatSocketTest() throws ParseException {

        ChatSocket chat = new ChatSocket("TeamBTestUser2", "111111111111111111111111111111111111");

        List<String> msg = new ArrayList<>();

        chat.registerMessageHandler((message, from, isPrivate) -> msg.add(message + '|' + from + '|' + isPrivate));

        setupSocket("{\"channel\":\"all\",\"from\":\"TeamBTestUser\",\"message\":\"Hello World!\"}", chat);
        chat.sendMessage("Hello World!");

        setupSocket("{\"channel\":\"private\",\"from\":\"TeamBTestUser\",\"message\":\"Hello World! Private\"}", chat);
        chat.sendPrivateMessage("Hello World! Private", "TeamBTestUser2");


        Assert.assertTrue(msg.contains("Hello World!|TeamBTestUser|false"));
        Assert.assertTrue(msg.contains("Hello World! Private|TeamBTestUser|true"));
    }

    @Test
    void gameChatSocketTest() {

        List<String> gameMsg = new ArrayList<>();

        gameSocket.registerMessageHandler((message, from, isPrivate) -> gameMsg.add(message + '|' + from + '|'
            + isPrivate));

        setupSocket("{\"action\":\"gameChat\",\"data\":{\"channel\":\"all\",\"message\":\"Hello World!\",\""
            + "from\":\"TeamBTestUser2\"}}", gameSocket);
        gameSocket.sendMessage("Hello World!");

        setupSocket("{\"action\":\"gameChat\",\"data\":{\"channel\":\"private\",\"to\":\"TeamBTestUser2\",\""
            + "message\":\"Hello World! Private\",\"from\":\"TeamBTestUser2\"}}", gameSocket);
        gameSocket.sendPrivateMessage("Hello World! Private", "TeamBTestUser");


        Assert.assertTrue(gameMsg.contains("Hello World!|TeamBTestUser2|false"));
        Assert.assertTrue(gameMsg.contains("Hello World! Private|TeamBTestUser2|true"));
    }

}
