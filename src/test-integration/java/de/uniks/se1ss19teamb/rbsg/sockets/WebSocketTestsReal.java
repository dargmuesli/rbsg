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
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();


        SystemSocket system = new SystemSocket(login.getUserKey());

        List<String> msg = new ArrayList<>();

        system.registerUserJoinHandler((name) -> msg.add("userJoin|" + name));

        system.registerUserLeftHandler((name) -> msg.add("userLeft|" + name));

        system.registerGameCreateHandler((name, id, neededPlayers)
            -> msg.add("gameCreate|" + name + '|' + id + '|' + neededPlayers));

        system.registerGameDeleteHandler((id) -> msg.add("gameDelete|" + id));

        system.connect();

        LoginUserRequest login2 = new LoginUserRequest("testTeamB2", "qwertz");
        login2.sendRequest();

        CreateGameRequest createGame = new CreateGameRequest("testTeamBGame", 2, login.getUserKey());
        createGame.sendRequest();

        DeleteGameRequest deleteGame = new DeleteGameRequest(createGame.getGameId(), login2.getUserKey());
        deleteGame.sendRequest();

        LogoutUserRequest logout = new LogoutUserRequest(login2.getUserKey());
        logout.sendRequest();

        Thread.sleep(2000); //Wait for Msg

        system.disconnect();

        System.out.println(msg);

        Assert.assertTrue(msg.contains("userJoin|testTeamB2"));
        Assert.assertTrue(msg.contains("userLeft|testTeamB2"));
        Assert.assertTrue(msg.contains("gameCreate|testTeamBGame|" + createGame.getGameId() + "|2"));
        Assert.assertTrue(msg.contains("gameDelete|" + createGame.getGameId()));

    }

    @Test
    public void chatSocketTest() throws ParseException, InterruptedException {
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();

        ChatSocket chat = new ChatSocket("testTeamB", login.getUserKey());

        LoginUserRequest login2 = new LoginUserRequest("testTeamB2", "qwertz");
        login2.sendRequest();

        ChatSocket chat2 = new ChatSocket("testTeamB2", login2.getUserKey());

        List<String> msg = new ArrayList<>();

        chat2.registerChatMessageHandler((message, from, isPrivate) -> msg.add(message + '|' + from + '|' + isPrivate));

        chat.connect();
        chat2.connect();

        chat.sendMessage("Hello World!");
        chat.sendPrivateMessage("Hello World! Private", "testTeamB2");

        Thread.sleep(2000);

        chat.disconnect();
        chat2.disconnect();

        LogoutUserRequest logout = new LogoutUserRequest(login.getUserKey());
        logout.sendRequest();

        LogoutUserRequest logout2 = new LogoutUserRequest(login2.getUserKey());
        logout2.sendRequest();

        System.out.println(msg);

        Assert.assertTrue(msg.contains("Hello World!|testTeamB|false"));
        Assert.assertTrue(msg.contains("Hello World! Private|testTeamB|true"));
    }

}
