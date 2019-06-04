package de.uniks.se1ss19teamb.rbsg.request;

import de.uniks.se1ss19teamb.rbsg.model.Game;
import org.apache.http.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class RESTRequestTestsMocked {

    private HTTPManager httpManager;

    private HTTPRequestResponse getHttpCreateGameResponse() {
        String httpReqRepBodyCreateGame = "{\"status\":\"success\",\"message\":\"test\",\"data\":" +
                "{\"gameId\":\"123456789012345678901234\"}}";
        int status = 200;
        String errorMsg = "test";
        return new HTTPRequestResponse(httpReqRepBodyCreateGame, status, errorMsg);
    }
    
    private HTTPRequestResponse getHttpRegisterUserResponse() {
    	String httpReqRepBody = "{\"status\":\"failure\",\"message\":\"Name already taken\",\"data\":{}}";
        int status = 400;
        String errorMsg = "Bad Request";
        return new HTTPRequestResponse(httpReqRepBody, status, errorMsg);
    }
    
	private HTTPRequestResponse getHttpLoginUserResponse() {
		String httpReqRepBody = "{\"status\":\"success\",\"message\":\"\",\"data\":{\"userKey\":\"111111111111111111111111111111111111\"}}";
        int status = 200;
        String errorMsg = "Bad Request";
        return new HTTPRequestResponse(httpReqRepBody, status, errorMsg);
	}
	
	private HTTPRequestResponse getHttpQueryUsersResponse() {
		String httpReqRepBody = "{\"status\":\"success\",\"message\":\"\",\"data\":[\"testTeamB\"]}";
        int status = 200;
        String errorMsg = "test";
        return new HTTPRequestResponse(httpReqRepBody, status, errorMsg);
	}

	private HTTPRequestResponse getHttpLogoutUserResponse() {
		String httpReqRepBodyLogout = "{\"status\":\"success\",\"message\":\"Logged out\",\"data\":" +
                "{}}";
        int statusLogout = 200;
        String errorMsgLogout = "test";
        return new HTTPRequestResponse(httpReqRepBodyLogout, statusLogout, errorMsgLogout);
	}
	
	private HTTPRequestResponse getHttpQueryGamesResponse() {
		String httpReqRepBodyQueryGames = "{\"status\":\"success\",\"message\":\"test\",\"data\":" +
	            "[{\"id\":\"123456789012345678901234\",\"name\":\"testTeamBGame\",\"neededPlayer\":2,\"joinedPlayer\":0}]}";
        int statusLogout = 200;
        String errorMsgLogout = "test";
        return new HTTPRequestResponse(httpReqRepBodyQueryGames, statusLogout, errorMsgLogout);
	}
	
	private HTTPRequestResponse getHttpDeleteGameResponse() {
		String httpReqRepBody = "{\"status\":\"success\",\"message\":\"Game deleted\",\"data\":" +
                "{\"userKey\":\"111111111111111111111111111111111111\"}}";
        int status = 200;
        String errorMsg = "test";
        return new HTTPRequestResponse(httpReqRepBody, status, errorMsg);
	}
	
	private HTTPRequestResponse getHttpJoinGameResponse() {
		String httpReqRepBody = "{\"status\":\"success\",\"message\":\"Game joined, you will be disconnected from the chat and the system socket. Please connect to /ws/game?gameId=GAME_ID\",\"data\":" +
                "{\"userKey\":\"111111111111111111111111111111111111\"}}";
        int status = 200;
        String errorMsg = "test";
        return new HTTPRequestResponse(httpReqRepBody, status, errorMsg);
	}

    @Before
    public void setupTests() {
        httpManager = mock(HTTPManager.class);
    }

    @Test
    public void registerUserTest() {
    	HTTPRequestResponse httpRequestResponse = getHttpRegisterUserResponse();

        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RegisterUserRequest req = new RegisterUserRequest("testTeamB", "qwertz", httpManager);

        try {
            req.sendRequest();
            
            Assert.assertFalse(req.getSuccessful());
            Assert.assertEquals("Name already taken", req.getMessage());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }

    }

    @Test
    public void loginUserTest() {

        HTTPRequestResponse httpRequestResponse = getHttpLoginUserResponse();

        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginUserRequest req = new LoginUserRequest("testTeamB", "qwertz", httpManager);
        try {
            //Query Request
            req.sendRequest();

            //Test Request Helpers
            Assert.assertTrue(req.getSuccessful());
            Assert.assertEquals(36, req.getUserKey().length());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void queryUsersInLobbyTest() throws IOException, ParseException {

        HTTPRequestResponse httpRequestResponseGet = getHttpQueryUsersResponse();

        try {
            when(httpManager.get(any(), any())).thenReturn(httpRequestResponseGet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        QueryUsersInLobbyRequest req = new QueryUsersInLobbyRequest("111111111111111111111111111111111111", httpManager);
        try {
            req.sendRequest();

            Assert.assertTrue(req.getSuccessful());
            Assert.assertTrue(req.getUsersInLobby().contains("testTeamB"));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void logoutUserTest() throws IOException, ParseException {
        HTTPRequestResponse httpRequestResponseLogout = getHttpLogoutUserResponse();

        try {
            when(httpManager.get(any(), any())).thenReturn(httpRequestResponseLogout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogoutUserRequest req = new LogoutUserRequest("111111111111111111111111111111111111", httpManager);
        try {
            req.sendRequest();

            Assert.assertTrue(req.getSuccessful());
            Assert.assertEquals("Logged out", req.getMessage());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void createGameTest() throws IOException, ParseException {
        HTTPRequestResponse httpRequestResponseCreateGame = getHttpCreateGameResponse();

        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponseCreateGame);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CreateGameRequest req = new CreateGameRequest("testTeamBGame", 2, "111111111111111111111111111111111111", httpManager);
        try {
            req.sendRequest();

            Assert.assertTrue(req.getSuccessful());
            Assert.assertEquals(24, req.getGameId().length());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void queryGamesTest() throws IOException, ParseException {
        HTTPRequestResponse httpRequestResponseQueryGames = getHttpQueryGamesResponse();

        QueryGamesRequest req = new QueryGamesRequest("111111111111111111111111111111111111", httpManager);
        try {
            when(httpManager.get(any(), any())).thenReturn(httpRequestResponseQueryGames);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            req.sendRequest();

            Assert.assertTrue(req.getSuccessful());

            boolean hasTeamBTestGame = false;
            for (Game game : req.getGames()) {
                hasTeamBTestGame |= game.getName().equals("testTeamBGame");
            }
            Assert.assertTrue(hasTeamBTestGame);
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void deleteGameTest() throws IOException, ParseException {
        DeleteGameRequest req = new DeleteGameRequest("123456789012345678901234", "111111111111111111111111111111111111", httpManager);

        HTTPRequestResponse httpRequestResponseDelete = getHttpDeleteGameResponse();

        try {
            when(httpManager.delete(any(), any(), any())).thenReturn(httpRequestResponseDelete);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            req.sendRequest();

            Assert.assertTrue(req.getSuccessful());
            Assert.assertEquals("Game deleted", req.getMessage());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void joinGameTest() throws IOException, ParseException {
        JoinGameRequest req = new JoinGameRequest("123456789012345678901234", "111111111111111111111111111111111111", httpManager);

        HTTPRequestResponse httpRequestResponseJoinGame = getHttpJoinGameResponse();

        try {
            when(httpManager.get(any(), any())).thenReturn(httpRequestResponseJoinGame);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            req.sendRequest();

            Assert.assertTrue(req.getSuccessful());
            Assert.assertEquals("Game joined, you will be disconnected from the chat and the system socket. Please connect to /ws/game?gameId=GAME_ID", req.getMessage());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }
}
