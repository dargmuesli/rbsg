package de.uniks.se1ss19teamb.rbsg.sockets;

import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.util.RequestUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class WebSocketTestsReal {

    @Before
    public void setupTests() {
        RestRequestTestsReal.resetHttpManager();
    }

    @Test
    public void systemSocketTest() throws ParseException, InterruptedException {
        RestRequestTestsReal.loginUser();

        SystemSocket system = new SystemSocket();

        List<String> msg = new ArrayList<>();

        system.registerUserJoinHandler((name) -> msg.add("userJoin|" + name));

        system.registerUserLeftHandler((name) -> msg.add("userLeft|" + name));

        system.registerGameCreateHandler((name, id, neededPlayers)
            -> msg.add("gameCreate|" + name + '|' + id + '|' + neededPlayers));

        system.registerGameDeleteHandler((id) -> msg.add("gameDelete|" + id));

        system.registerPlayerJoinedGameHandler((id, joinedPlayer)
            -> msg.add("playerJoinedGame|" + id + "|" + joinedPlayer));

        system.connect();

        RestRequestTestsReal.loginUser("TeamBTestUser2");

        RestRequestTestsReal.createGame();

        RestRequestTestsReal.joinGame();

        if (!RequestUtil.request(new DeleteGameRequest(RestRequestTestsReal.gameId, LoginController.getUserToken()))) {
            Assert.fail();
        }

        if (!RequestUtil.request(new LogoutUserRequest(LoginController.getUserToken()))) {
            Assert.fail();
        }

        Thread.sleep(2000); //Wait for Msg

        system.disconnect();

        System.out.println(msg);

        Assert.assertTrue(msg.contains("userJoin|TeamBTestUser2"));
        Assert.assertTrue(msg.contains("userLeft|TeamBTestUser2"));
        Assert.assertTrue(msg.contains("gameCreate|TeamBTestUserGame|" + RestRequestTestsReal.gameId + "|2"));
        Assert.assertTrue(msg.contains("gameDelete|" + RestRequestTestsReal.gameId));
        Assert.assertTrue(msg.contains("playerJoinedGame|" + RestRequestTestsReal.gameId + "|1"));
    }

    @Test
    public void chatSocketTest() throws ParseException, InterruptedException {
        RestRequestTestsReal.loginUser();
        String userToken1 = LoginController.getUserToken();

        ChatSocket chat = new ChatSocket("TeamBTestUser", userToken1);

        RestRequestTestsReal.loginUser("TeamBTestUser2");
        String userToken2 = LoginController.getUserToken();

        ChatSocket chat2 = new ChatSocket("TeamBTestUser2", userToken2);

        List<String> msg = new ArrayList<>();

        chat2.registerMessageHandler((message, from, isPrivate, wasEncrypted)
            -> msg.add(message + '|' + from + '|' + isPrivate));

        chat.connect();
        chat2.connect();

        chat.sendMessage("Hello World!");
        chat.sendPrivateMessage("Hello World! Private", "TeamBTestUser2");

        Thread.sleep(2000);

        chat.disconnect();
        chat2.disconnect();

        if (!RequestUtil.request(new LogoutUserRequest(userToken1))) {
            Assert.fail();
        }

        if (!RequestUtil.request(new LogoutUserRequest(userToken2))) {
            Assert.fail();
        }

        System.out.println(msg);

        Assert.assertTrue(msg.contains("Hello World!|TeamBTestUser2|false"));
        Assert.assertTrue(msg.contains("Hello World! Private|TeamBTestUser2|true"));
    }

    @Test
    public void gameSocketDistributorTest() {
        String userKey;
        LoginUserRequest lur = new LoginUserRequest("TeamBTestUser", "qwertz");
        lur.sendRequest();
        userKey = lur.getData();

        //Check for wrong GameSocket request
        GameSocket testGameSocketOne = GameSocketDistributor.getGameSocket(2);
        Assert.assertNull(testGameSocketOne);

        //Create new GameSocket
        CreateGameRequest cgr = new CreateGameRequest("TestGameB", 2, userKey);
        cgr.sendRequest();
        String gameId = cgr.getData();
        GameSocketDistributor.setGameSocket(0, gameId);
        GameSocket testGameSocketTwo = GameSocketDistributor.getGameSocket(0);
        Assert.assertNotNull(testGameSocketTwo);
    }
}
