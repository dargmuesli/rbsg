package de.uniks.se1ss19teamb.rbsg.request;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class RESTRequestTests {

	@Test
	public void registerUserTest() {
		
		RegisterUserRequest req = new RegisterUserRequest("testTeamB", "qwertz");
		try {
			//Test Returning JSON
			//Demonstration on how to directly process JSON with Lambda
			req.sendRequest((response) -> {
				Assert.assertEquals("failure", response.get("status"));
				Assert.assertEquals("Name already taken", response.get("message"));
			});
			
			//Test Request Helpers
			//This is the way one should query information from the Request Handlers
			//ALWAYS!!! check getSuccessful first, since if it returns false, all other methods except getMessage have undefined behaviour or might throw Exceptions
			Assert.assertEquals(false, req.getSuccessful());
			Assert.assertEquals("Name already taken", req.getMessage());
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		
	}
	
	@Test
	public void loginUserTest() {

		LoginUserRequest req = new LoginUserRequest("testTeamB", "qwertz");
		try {
			//Query Request
			req.sendRequest();
			
			//Test Request Helpers
			Assert.assertEquals(true, req.getSuccessful());
			Assert.assertEquals(36, req.getUserKey().length());
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
		
		
	}
	
	@Test
	public void queryUsersInLobbyTest() throws IOException, ParseException {
		LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
		login.sendRequest();
		
		QueryUsersInLobbyRequest req = new QueryUsersInLobbyRequest(login.getUserKey());
		try {
			req.sendRequest();
			
			Assert.assertTrue(req.getUsersInLobby().contains("testTeamB"));
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
	}
	
	@Test
	public void logoutUserTest() throws IOException, ParseException {
		LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
		login.sendRequest();
		
		LogoutUserRequest req = new LogoutUserRequest(login.getUserKey());
		try {
			req.sendRequest();
			
			Assert.assertEquals(true, req.getSuccessful());
			Assert.assertEquals("Logged out", req.getMessage());
		} catch (Exception e) {
			Assert.fail(e.toString());
		}
	}
}
