package de.uniks.se1ss19teamb.rbsg.request;

import de.uniks.se1ss19teamb.rbsg.model.Game;
import org.apache.http.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class RESTRequestTests {

    HTTPManager httpManager;

    private HTTPRequestResponse getHttpLoginResponse() {
        String httpReqRepBody = "{\"status\":\"success\",\"message\":\"test\",\"data\":" +
                "{\"userKey\":\"111111111111111111111111111111111111\"}}";
        int status = 200;
        String errorMsg = "test";
        HTTPRequestResponse httpRequestResponseLogin = new HTTPRequestResponse(httpReqRepBody, status, errorMsg);
        return httpRequestResponseLogin;
    }

    @Before
    public void setupTests() {
        httpManager = mock(HTTPManager.class);
        String httpReqRepBody = "{\"status\":\"failure\",\"message\":\"Name already taken\",\"data\":{}}";
        int status = 400;
        String errorMsg = "Bad Request";
        HTTPRequestResponse httpRequestResponse = new HTTPRequestResponse(httpReqRepBody, status, errorMsg);

        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void registerUserTest() {

        RegisterUserRequest req = new RegisterUserRequest("testTeamB", "qwertz", httpManager);

        try {
            //Test Returning Json
            //Demonstration on how to directly process Json with Lambda
            req.sendRequest((response) -> {
                Assert.assertEquals("failure", response.get("status").getAsString());
                Assert.assertEquals("Name already taken", response.get("message").getAsString());
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

        String httpReqRepBody = "{\"status\":\"success\",\"message\":\"Name already taken\",\"data\":" +
                "{\"userKey\":\"111111111111111111111111111111111111\"}}";
        int status = 400;
        String errorMsg = "Bad Request";
        HTTPRequestResponse httpRequestResponse = new HTTPRequestResponse(httpReqRepBody, status, errorMsg);

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
            Assert.assertEquals(true, req.getSuccessful());
            Assert.assertEquals(36, req.getUserKey().length());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void queryUsersInLobbyTest() throws IOException, ParseException {

        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz", httpManager);

        String httpReqRepBody = "{\"status\":\"success\",\"message\":\"\",\"data\":[\"testTeamB\"]}";
        int status = 400;
        String errorMsg = "Bad Request";
        HTTPRequestResponse httpRequestResponseGet = new HTTPRequestResponse(httpReqRepBody, status, errorMsg);

        HTTPRequestResponse httpRequestResponseLogin = getHttpLoginResponse();

        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponseLogin);
            when(httpManager.get(any(), any())).thenReturn(httpRequestResponseGet);
        } catch (Exception e) {
            e.printStackTrace();
        }


        login.sendRequest();

        QueryUsersInLobbyRequest req = new QueryUsersInLobbyRequest(login.getUserKey(), httpManager);
        try {
            req.sendRequest();

            Assert.assertEquals(true, req.getSuccessful());
            Assert.assertTrue(req.getUsersInLobby().contains("testTeamB"));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void logoutUserTest() throws IOException, ParseException {
        HTTPRequestResponse httpRequestResponseLogin = getHttpLoginResponse();

        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz", httpManager);
        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponseLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        login.sendRequest();

        String httpReqRepBodyLogout = "{\"status\":\"success\",\"message\":\"Logged out\",\"data\":" +
                "{}}";
        int statusLogout = 200;
        String errorMsgLogout = "test";
        HTTPRequestResponse httpRequestResponseLogout = new HTTPRequestResponse(httpReqRepBodyLogout, statusLogout, errorMsgLogout);

        try {
            when(httpManager.get(any(), any())).thenReturn(httpRequestResponseLogout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogoutUserRequest req = new LogoutUserRequest(login.getUserKey(), httpManager);
        try {
            req.sendRequest();

            Assert.assertEquals(true, req.getSuccessful());
            Assert.assertEquals("Logged out", req.getMessage());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void createGameTest() throws IOException, ParseException {
        int status = 200;
        String errorMsg = "test";
        HTTPRequestResponse httpRequestResponseLogin = getHttpLoginResponse();

        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz", httpManager);
        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponseLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        login.sendRequest();

        String httpReqRepBodyCreateGame = "{\"status\":\"success\",\"message\":\"test\",\"data\":" +
                "{\"gameId\":\"123456789012345678901234\"}}";
        HTTPRequestResponse httpRequestResponseCreateGame = new HTTPRequestResponse(httpReqRepBodyCreateGame, status, errorMsg);

        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponseCreateGame);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CreateGameRequest req = new CreateGameRequest("testTeamBGame", 2, login.getUserKey(), httpManager);
        try {
            //Query Request
            req.sendRequest();

            Assert.assertEquals(true, req.getSuccessful());
            Assert.assertEquals(24, req.getGameId().length());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void queryGamesTest() throws IOException, ParseException {
        HTTPRequestResponse httpRequestResponseLogin = getHttpLoginResponse();

        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz", httpManager);

        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponseLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        login.sendRequest();

        String httpReqRepBodyCreateGame = "{\"status\":\"success\",\"message\":\"test\",\"data\":" +
                "[{\"id\":\"123456789012345678901234\",\"name\":\"testTeamBGame\",\"neededPlayer\":2,\"joinedPlayer\":0}]}";
        HTTPRequestResponse httpRequestResponseCreateGame = new HTTPRequestResponse(httpReqRepBodyCreateGame, 200, "test");

        CreateGameRequest createGame = new CreateGameRequest("testTeamBGame", 2, login.getUserKey(), httpManager);
        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponseCreateGame);
        } catch (Exception e) {
            e.printStackTrace();
        }

        createGame.sendRequest();

        QueryGamesRequest req = new QueryGamesRequest(login.getUserKey(), httpManager);
        try {
            when(httpManager.get(any(), any())).thenReturn(httpRequestResponseCreateGame);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            req.sendRequest();

            Assert.assertEquals(true, req.getSuccessful());

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
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();

        CreateGameRequest createGame = new CreateGameRequest("testTeamBGame", 2, login.getUserKey());
        createGame.sendRequest();

        DeleteGameRequest req = new DeleteGameRequest(createGame.getGameId(), login.getUserKey());
        try {
            req.sendRequest();

            Assert.assertEquals(true, req.getSuccessful());
            Assert.assertEquals("Game deleted", req.getMessage());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void joinGameTest() throws IOException, ParseException {
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();

        CreateGameRequest createGame = new CreateGameRequest("testTeamBGame", 2, login.getUserKey());
        createGame.sendRequest();

        JoinGameRequest req = new JoinGameRequest(createGame.getGameId(), login.getUserKey());
        try {
            req.sendRequest();

            Assert.assertEquals(true, req.getSuccessful());
            Assert.assertEquals("Game joined, you will be disconnected from the chat and the system socket. Please connect to /ws/game?gameId=GAME_ID", req.getMessage());

            //Check if we actually joined the game
            QueryGamesRequest query = new QueryGamesRequest(login.getUserKey());
            query.sendRequest();

            Assert.assertEquals(1, query.getGames().stream().filter((game) -> game.getId().equals(createGame.getGameId())).findFirst().get().getJoinedPlayers());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }
    
    /*
    @After
    public void cleanupGames() throws IOException, ParseException {
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();
        
        QueryGamesRequest query = new QueryGamesRequest(login.getUserKey());
        query.sendRequest();
        
        query.getGames().stream().filter((game) -> game.getName().equals("testTeamBGame")).forEach((game) -> {
            System.out.println("Tidying up Game " + game.getName() + " with id " + game.getId() + "...");
            DeleteGameRequest req = new DeleteGameRequest(game.getId(), login.getUserKey());
            try {
                req.sendRequest();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        });
    }
     */
}
