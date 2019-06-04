package de.uniks.se1ss19teamb.rbsg.request;

import de.uniks.se1ss19teamb.rbsg.model.Game;
import org.apache.http.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class RestRequestTestsMocked {

    private HttpManager httpManager;

    private HttpRequestResponse getHttpCreateGameResponse() {
        String httpReqRepBodyCreateGame = "{\"status\":\"success\",\"message\":\"test\",\"data\":" +
                "{\"gameId\":\"123456789012345678901234\"}}";
        int status = 200;
        String errorMsg = "test";
        return new HttpRequestResponse(httpReqRepBodyCreateGame, status, errorMsg);
    }
    
    private HttpRequestResponse getHttpRegisterUserResponse() {
    	String httpReqRepBody = "{\"status\":\"failure\",\"message\":\"Name already taken\",\"data\":{}}";
        int status = 400;
        String errorMsg = "Bad Request";
        return new HttpRequestResponse(httpReqRepBody, status, errorMsg);
    }
    
	private HttpRequestResponse getHttpLoginUserResponse() {
		String httpReqRepBody = "{\"status\":\"success\",\"message\":\"\",\"data\":{\"userKey\":\"111111111111111111111111111111111111\"}}";
        int status = 200;
        String errorMsg = "Bad Request";
        return new HttpRequestResponse(httpReqRepBody, status, errorMsg);
	}
	
	private HttpRequestResponse getHttpQueryUsersResponse() {
		String httpReqRepBody = "{\"status\":\"success\",\"message\":\"\",\"data\":[\"testTeamB\"]}";
        int status = 200;
        String errorMsg = "test";
        return new HttpRequestResponse(httpReqRepBody, status, errorMsg);
	}

	private HttpRequestResponse getHttpLogoutUserResponse() {
		String httpReqRepBodyLogout = "{\"status\":\"success\",\"message\":\"Logged out\",\"data\":" +
                "{}}";
        int statusLogout = 200;
        String errorMsgLogout = "test";
        return new HttpRequestResponse(httpReqRepBodyLogout, statusLogout, errorMsgLogout);
	}
	
	private HttpRequestResponse getHttpQueryGamesResponse() {
		String httpReqRepBodyQueryGames = "{\"status\":\"success\",\"message\":\"test\",\"data\":" +
	            "[{\"id\":\"123456789012345678901234\",\"name\":\"testTeamBGame\",\"neededPlayer\":2,\"joinedPlayer\":0}]}";
        int statusLogout = 200;
        String errorMsgLogout = "test";
        return new HttpRequestResponse(httpReqRepBodyQueryGames, statusLogout, errorMsgLogout);
	}
	
	private HttpRequestResponse getHttpDeleteGameResponse() {
		String httpReqRepBody = "{\"status\":\"success\",\"message\":\"Game deleted\",\"data\":" +
                "{\"userKey\":\"111111111111111111111111111111111111\"}}";
        int status = 200;
        String errorMsg = "test";
        return new HttpRequestResponse(httpReqRepBody, status, errorMsg);
	}
	
	private HttpRequestResponse getHttpJoinGameResponse() {
		String httpReqRepBody = "{\"status\":\"success\",\"message\":\"Game joined, you will be disconnected from the chat and the system socket. Please connect to /ws/game?gameId=GAME_ID\",\"data\":" +
                "{\"userKey\":\"111111111111111111111111111111111111\"}}";
        int status = 200;
        String errorMsg = "test";
        return new HttpRequestResponse(httpReqRepBody, status, errorMsg);
	}

    @Before
    public void setupTests() {
        httpManager = mock(HttpManager.class);
        AbstractRestRequest.httpManager = httpManager;
    }

    @Test
    public void registerUserTest() {
    	HttpRequestResponse httpRequestResponse = getHttpRegisterUserResponse();

        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RegisterUserRequest req = new RegisterUserRequest("testTeamB", "qwertz");

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

        HttpRequestResponse httpRequestResponse = getHttpLoginUserResponse();

        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginUserRequest req = new LoginUserRequest("testTeamB", "qwertz");
        
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

        HttpRequestResponse httpRequestResponseGet = getHttpQueryUsersResponse();

        try {
            when(httpManager.get(any(), any())).thenReturn(httpRequestResponseGet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        QueryUsersInLobbyRequest req = new QueryUsersInLobbyRequest("111111111111111111111111111111111111");
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
        HttpRequestResponse httpRequestResponseLogout = getHttpLogoutUserResponse();

        try {
            when(httpManager.get(any(), any())).thenReturn(httpRequestResponseLogout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogoutUserRequest req = new LogoutUserRequest("111111111111111111111111111111111111");
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
        HttpRequestResponse httpRequestResponseCreateGame = getHttpCreateGameResponse();

        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponseCreateGame);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CreateGameRequest req = new CreateGameRequest("testTeamBGame", 2, "111111111111111111111111111111111111");
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
        HttpRequestResponse httpRequestResponseQueryGames = getHttpQueryGamesResponse();

        QueryGamesRequest req = new QueryGamesRequest("111111111111111111111111111111111111");
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
        DeleteGameRequest req = new DeleteGameRequest("123456789012345678901234", "111111111111111111111111111111111111");

        HttpRequestResponse httpRequestResponseDelete = getHttpDeleteGameResponse();

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
        JoinGameRequest req = new JoinGameRequest("123456789012345678901234", "111111111111111111111111111111111111");

        HttpRequestResponse httpRequestResponseJoinGame = getHttpJoinGameResponse();

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
