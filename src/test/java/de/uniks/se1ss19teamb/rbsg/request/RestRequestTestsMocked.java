package de.uniks.se1ss19teamb.rbsg.request;

import static org.mockito.Mockito.*;

import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Game;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import org.apache.http.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class RestRequestTestsMocked {

    private HttpManager httpManager;
    private String fakeUserKey = "dca2a697-ecfb-4987-ae95-2fdfe9f4a731";
    private String fakeArmyId = "5d11fad12c945100017660ee";

    private HttpRequestResponse getHttpCreateGameResponse() {
        String httpReqRepBodyCreateGame = "{\"status\":\"success\",\"message\":\"test\",\"data\":{\"gameId\":"
            + "\"123456789012345678901234\"}}";
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
        String httpReqRepBody = "{\"status\":\"success\",\"message\":\"\",\"data\":{\"userKey\":"
            + "\"111111111111111111111111111111111111\"}}";
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
        String httpReqRepBodyLogout = "{\"status\":\"success\",\"message\":\"Logged out\",\"data\":{}}";
        int statusLogout = 200;
        String errorMsgLogout = "test";
        return new HttpRequestResponse(httpReqRepBodyLogout, statusLogout, errorMsgLogout);
    }

    private HttpRequestResponse getHttpQueryGamesResponse() {
        String httpReqRepBodyQueryGames = "{\"status\":\"success\",\"message\":\"test\",\"data\":"
            + "[{\"id\":\"123456789012345678901234\",\"name\":\"testTeamBGame\",\"neededPlayer\":2,"
            + "\"joinedPlayer\":0}]}";
        int statusLogout = 200;
        String errorMsgLogout = "test";
        return new HttpRequestResponse(httpReqRepBodyQueryGames, statusLogout, errorMsgLogout);
    }

    private HttpRequestResponse getHttpDeleteGameResponse() {
        String httpReqRepBody = "{\"status\":\"success\",\"message\":\"Game deleted\",\"data\":{\"userKey\":"
            + "\"111111111111111111111111111111111111\"}}";
        int status = 200;
        String errorMsg = "test";
        return new HttpRequestResponse(httpReqRepBody, status, errorMsg);
    }

    private HttpRequestResponse getHttpJoinGameResponse() {
        String httpReqRepBody = "{\"status\":\"success\",\"message\":\"Game joined, you will be disconnected "
            + "from the chat and the system socket. Please connect to /ws/game?gameId=GAME_ID\",\"data\":"
            + "{\"userKey\":\"111111111111111111111111111111111111\"}}";
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
    public void queryUsersInLobbyTest() throws ParseException {

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
    public void logoutUserTest() throws ParseException {
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
    public void createGameTest() throws ParseException {
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
    public void queryGamesTest() throws ParseException {
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
    public void deleteGameTest() throws ParseException {
        DeleteGameRequest req = new DeleteGameRequest("123456789012345678901234",
            "111111111111111111111111111111111111");

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
    public void joinGameTest() throws ParseException {
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
            Assert.assertEquals("Game joined, you will be disconnected from the chat and the system socket. "
                + "Please connect to /ws/game?gameId=GAME_ID", req.getMessage());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    /*
    private CreateArmyRequest createArmy() {
        String armyName = "testArmy001";
        ArrayList<String> unitIDs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            unitIDs.add("5cc051bd62083600017db3b6");
        }
        CreateArmyRequest createArmyRequest = new CreateArmyRequest(armyName, unitIDs, userKey);
        createArmyRequest.sendRequest();
        return createArmyRequest;
    }

    private void deleteArmy(String armyID) {
        DeleteArmyRequest deleteArmyRequest = new DeleteArmyRequest(armyID, userKey);
        deleteArmyRequest.sendRequest();
    }

    private void deleteAllArmies() {
        loginUser();
        QueryArmiesRequest queryArmiesRequest = new QueryArmiesRequest(userKey);
        queryArmiesRequest.sendRequest();
        for (Army a : queryArmiesRequest.getArmies()) {
            DeleteArmyRequest deleteArmyRequest = new DeleteArmyRequest(a.getId(), userKey);
            deleteArmyRequest.sendRequest();
        }
    }

     */

    private HttpRequestResponse getCreateArmyRequestResponse() {
        String httpReqRepBodyCreateGame = "{\"status\":\"success\",\"message\":\"\",\"data\":{\"id\":"
            + "\"5d11fad12c945100017660ee\",\"name\":\"testArmy\",\"units\":["
            + "\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\""
            + "]}}";
        int status = 200;
        String errorMsg = "";
        return new HttpRequestResponse(httpReqRepBodyCreateGame, status, errorMsg);
    }

    private HttpRequestResponse getDeleteArmyRequestResponse() {
        String httpReqRepBodyCreateGame = "{\"status\":\"success\",\"message\":\"Army deleted\",\"data\":{}}";
        int status = 200;
        String errorMsg = "";
        return new HttpRequestResponse(httpReqRepBodyCreateGame, status, errorMsg);
    }


    @Test
    public void createArmyRequestTest() {
        String name = "TestBArmy";

        ArrayList<String> unitIDs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            unitIDs.add("5cc051bd62083600017db3b6");
        }
        try {
            when(httpManager.post(any(), any(), any())).thenReturn(getCreateArmyRequestResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
        CreateArmyRequest req = new CreateArmyRequest(name, unitIDs, fakeUserKey);

        req.sendRequest();
        Assert.assertTrue(req.getSuccessful());
    }

    @Test
    public void deleteArmyRequestTest() {
        try {
            when(httpManager.delete(any(), any(), any())).thenReturn(getDeleteArmyRequestResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
        DeleteArmyRequest req = new DeleteArmyRequest(fakeArmyId, fakeUserKey);
        req.sendRequest();
        try {
            Assert.assertTrue(req.getSuccessful());
            Assert.assertEquals("Army deleted", req.getMessage());
        } catch (AssertionError e) {
            System.out.println("Check if there aren't too many armies for this player.");
            throw e;
        }

    }

    /*

    private LoginUserRequest loginUser() {
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();
        userKey = login.getUserKey();
        return login;
    }

    @Test
    public void getSpecificArmyRequestTest() {
        loginUser();
        CreateArmyRequest createArmyRequest = createArmy();
        GetSpecificArmyRequest req = new GetSpecificArmyRequest(createArmyRequest.getArmyID(), userKey);
        req.sendRequest();
        Army reqArmy = req.getRequestedArmy();
        Assert.assertTrue(req.getSuccessful());
        Assert.assertEquals("testArmy001", reqArmy.getName());
        deleteArmy(reqArmy.getId());

    }

    @Test
    public void queryArmiesRequestTest() {
        loginUser();
        CreateArmyRequest createArmyRequest = createArmy();
        QueryArmiesRequest req = new QueryArmiesRequest(userKey);
        req.sendRequest();
        ArrayList<Army> armies = req.getArmies();
        Assert.assertTrue(req.getSuccessful());
        boolean containsArmyID = false;
        for (Army army : armies) {
            if (army.getId().equals(createArmyRequest.getArmyID())) {
                containsArmyID = true;
            }
        }
        Assert.assertTrue(containsArmyID);
        deleteArmy(createArmyRequest.getArmyID());

    }

    @Test
    public void queryUnitsRequestTest() {
        loginUser();
        QueryUnitsRequest req = new QueryUnitsRequest(userKey);
        req.sendRequest();
        ArrayList<Unit> unitList = req.getUnits();
        Assert.assertTrue(req.getSuccessful());
        Assert.assertEquals(6, unitList.size());
        Assert.assertEquals("5cc051bd62083600017db3b6", unitList.get(0).getId());
        Assert.assertEquals("5cc051bd62083600017db3b7", unitList.get(1).getId());
        Assert.assertEquals("5cc051bd62083600017db3b8", unitList.get(2).getId());
        Assert.assertEquals("5cc051bd62083600017db3b9", unitList.get(3).getId());
        Assert.assertEquals("5cc051bd62083600017db3ba", unitList.get(4).getId());
        Assert.assertEquals("5cc051bd62083600017db3bb", unitList.get(5).getId());
        Assert.assertEquals("Infantry", unitList.get(5).getCanAttack().get(0));
    }

    @Test
    public void updateArmyRequestTest() {
        loginUser();
        CreateArmyRequest createArmyRequest = createArmy();
        Army testArmy = new Army();
        testArmy.setId(createArmyRequest.getArmyID());
        ArrayList<String> unitIDs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            unitIDs.add("5cc051bd62083600017db3b7");
        }
        testArmy.setUnits(unitIDs);
        testArmy.setName("changedName");
        UpdateArmyRequest req = new UpdateArmyRequest(testArmy, userKey);
        req.sendRequest();
        Assert.assertTrue(req.getSuccessful());

    }

    @After
    public void cleanupGames() throws ParseException {
        deleteAllArmies();
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();

        QueryGamesRequest query = new QueryGamesRequest(login.getUserKey());
        query.sendRequest();

        query.getGames().stream().filter((game) -> game.getName().equals("testTeamBGame"))
            .forEach((game) -> {
                System.out.println("Tidying up Game " + game.getName() + " with id " + game.getId() + "...");
                DeleteGameRequest req = new DeleteGameRequest(game.getId(), login.getUserKey());
                try {
                    req.sendRequest();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
    }

     */
}
