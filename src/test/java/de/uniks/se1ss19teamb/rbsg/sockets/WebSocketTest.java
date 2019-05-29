package de.uniks.se1ss19teamb.rbsg.sockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.junit.Assert;
import org.junit.Test;

import de.uniks.se1ss19teamb.rbsg.request.CreateGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.DeleteGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;

public class WebSocketTest {
	
	@Test
	public void systemSocketTest() throws ParseException, IOException, InterruptedException {
		LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
		login.sendRequest();

		
		SystemSocket system = new SystemSocket(login.getUserKey());
		
		List<String> msg = new ArrayList<String>();
		
		system.registerUserJoinHandler((name) -> {
			msg.add("userJoin|" + name);
		});
		
		system.registerUserLeftHandler((name) -> {
			msg.add("userLeft|" + name);
		});
		
		system.registerGameCreateHandler((name, id, neededPlayers) -> {
			msg.add("gameCreate|" + name + '|' + id + '|' + neededPlayers);
		});
		
		system.registerGameDeleteHandler((id) -> {
			msg.add("gameDelete|" + id);
		});
		
		system.connect();
		
		LoginUserRequest login2 = new LoginUserRequest("testTeamB2", "qwertz");
		login2.sendRequest();
		
		CreateGameRequest createGame = new CreateGameRequest("testTeamBGame", 2, login.getUserKey());
		createGame.sendRequest();
		
		DeleteGameRequest deleteGame = new DeleteGameRequest(createGame.getGameId(), login2.getUserKey());
		deleteGame.sendRequest();
		
		LogoutUserRequest logout = new LogoutUserRequest(login2.getUserKey());
		logout.sendRequest();
		
		Thread.sleep(250); //Wait for Msg
		
		system.disconnect();
		
		System.out.println(msg);
		
		Assert.assertTrue(msg.contains("userJoin|testTeamB2"));
		Assert.assertTrue(msg.contains("userLeft|testTeamB2"));
		Assert.assertTrue(msg.contains("gameCreate|testTeamBGame|" + createGame.getGameId() + "|2"));
		Assert.assertTrue(msg.contains("gameDelete|" + createGame.getGameId()));
		
	}
	
	
}
