package de.uniks.se1ss19teamb.rbsg.sockets;

import de.uniks.se1ss19teamb.rbsg.request.CreateGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.DeleteGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class WebSocketTestsReal {

    @Test
    public void systemSocketTest() throws ParseException, InterruptedException {
        LoginUserRequest login = new LoginUserRequest("TeamBTestUser", "qwertz");
        login.sendRequest();


        SystemSocket system = new SystemSocket(login.getData());

        List<String> msg = new ArrayList<>();

        system.registerUserJoinHandler((name) -> msg.add("userJoin|" + name));

        system.registerUserLeftHandler((name) -> msg.add("userLeft|" + name));

        system.registerGameCreateHandler((name, id, neededPlayers)
            -> msg.add("gameCreate|" + name + '|' + id + '|' + neededPlayers));

        system.registerGameDeleteHandler((id) -> msg.add("gameDelete|" + id));

        system.connect();

        LoginUserRequest login2 = new LoginUserRequest("TeamBTestUser2", "qwertz");
        login2.sendRequest();

        CreateGameRequest createGame = new CreateGameRequest("TeamBTestUserGame", 2, login.getData());
        createGame.sendRequest();

        DeleteGameRequest deleteGame = new DeleteGameRequest(createGame.getData(), login2.getData());
        deleteGame.sendRequest();

        LogoutUserRequest logout = new LogoutUserRequest(login2.getData());
        logout.sendRequest();

        Thread.sleep(2000); //Wait for Msg

        system.disconnect();

        System.out.println(msg);

        Assert.assertTrue(msg.contains("userJoin|TeamBTestUser2"));
        Assert.assertTrue(msg.contains("userLeft|TeamBTestUser2"));
        Assert.assertTrue(msg.contains("gameCreate|TeamBTestUserGame|" + createGame.getData() + "|2"));
        Assert.assertTrue(msg.contains("gameDelete|" + createGame.getData()));

    }

    @Test
    public void chatSocketTest() throws ParseException, InterruptedException {
        LoginUserRequest login = new LoginUserRequest("TeamBTestUser", "qwertz");
        login.sendRequest();

        ChatSocket chat = new ChatSocket("TeamBTestUser", login.getData());

        LoginUserRequest login2 = new LoginUserRequest("TeamBTestUser2", "qwertz");
        login2.sendRequest();

        ChatSocket chat2 = new ChatSocket("TeamBTestUser2", login2.getData());

        List<String> msg = new ArrayList<>();

        chat2.registerChatMessageHandler((message, from, isPrivate) -> msg.add(message + '|' + from + '|' + isPrivate));

        chat.connect();
        chat2.connect();

        chat.sendMessage("Hello World!");
        chat.sendPrivateMessage("Hello World! Private", "TeamBTestUser2");

        Thread.sleep(2000);

        chat.disconnect();
        chat2.disconnect();

        LogoutUserRequest logout = new LogoutUserRequest(login.getData());
        logout.sendRequest();

        LogoutUserRequest logout2 = new LogoutUserRequest(login2.getData());
        logout2.sendRequest();

        System.out.println(msg);

        Assert.assertTrue(msg.contains("Hello World!|TeamBTestUser|false"));
        Assert.assertTrue(msg.contains("Hello World! Private|TeamBTestUser|true"));
    }

}
