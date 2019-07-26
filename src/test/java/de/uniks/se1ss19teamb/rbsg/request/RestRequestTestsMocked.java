package de.uniks.se1ss19teamb.rbsg.request;

import static org.mockito.Mockito.*;

import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.ui.ArmyManagerController;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


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
        String httpReqRepBody = "{\"status\":\"success\",\"message\":\"\",\"data\":[\"TeamBTestUser\"]}";
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
            + "[{\"id\":\"123456789012345678901234\",\"name\":\"TeamBTestUserGame\",\"neededPlayer\":2,"
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
        ArmyManagerController.availableUnits.put("5cc051bd62083600017db3b6",
            new Unit("5cc051bd62083600017db3b6", "Infantry", 3, 3, new ArrayList<>()));
    }

    @Test
    public void registerUserTest() {
        HttpRequestResponse httpRequestResponse = getHttpRegisterUserResponse();

        try {
            when(httpManager.post(any(), any(), any())).thenReturn(httpRequestResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RegisterUserRequest req = new RegisterUserRequest("TeamBTestUser", "qwertz");

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
        LoginUserRequest req = new LoginUserRequest("TeamBTestUser", "qwertz");

        try {
            //Query Request
            req.sendRequest();

            //Test Request Helpers
            Assert.assertTrue(req.getSuccessful());
            Assert.assertEquals(36, req.getData().length());
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
            Assert.assertTrue(req.getData().contains("TeamBTestUser"));
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

        CreateGameRequest req;

        req = new CreateGameRequest("TeamBTestUserGame", 2, "111111111111111111111111111111111111");
        req.sendRequest();

        Assert.assertTrue(req.getSuccessful());
        Assert.assertEquals(24, req.getData().length());

        req = new CreateGameRequest("TeamBTestUserGame", 2, "111111111111111111111111111111111111", 123);
        req.sendRequest();

        Assert.assertTrue(req.getSuccessful());
        Assert.assertEquals(24, req.getData().length());
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

            final boolean[] hasTeamBTestGame = {false};
            req.getData().forEach((s, gameMeta)
                -> hasTeamBTestGame[0] |= gameMeta.getName().equals("TeamBTestUserGame"));
            Assert.assertTrue(hasTeamBTestGame[0]);
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

    private HttpRequestResponse getCreateArmyRequestResponse() {
        String httpReqRepBodyCreateArmy = "{\"status\":\"success\",\"message\":\"\",\"data\":{\"id\":"
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
        return new HttpRequestResponse(httpReqRepBodyCreateArmy, status, errorMsg);
    }

    private HttpRequestResponse getDeleteArmyRequestResponse() {
        String httpReqRepBodyDeleteArmy = "{\"status\":\"success\",\"message\":\"Army deleted\",\"data\":{}}";
        int status = 200;
        String errorMsg = "";
        return new HttpRequestResponse(httpReqRepBodyDeleteArmy, status, errorMsg);
    }

    private HttpRequestResponse getGetSpecificArmyResponse() {
        String httpReqRepBodySpecificArmy = "{\"status\":\"success\",\"message\":\"\",\"data\":{\"id\":"
            + "\"5d11fad12c945100017660ee\",\"name\":\"testArmy001\",\"units\":[\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\",\"5cc051bd62083600017db3b6\",\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\",\"5cc051bd62083600017db3b6\",\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\",\"5cc051bd62083600017db3b6\",\"5cc051bd62083600017db3b6\"]}}";
        int status = 200;
        String errorMsg = "";
        return new HttpRequestResponse(httpReqRepBodySpecificArmy, status, errorMsg);
    }

    private HttpRequestResponse getQueryArmiesRequestResponse() {
        String httpReqRepBodyQueryArimes = "{\"status\":\"success\",\"message\":\"\",\"data\":[{\"id\":"
            + "\"5d0d2454a2ef7800015af0bd\",\"name\":\"hello\",\"units\":[\"5cc051bd62083600017db3b7\","
            + "\"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\","
            + "\"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3b7\",\"5cc051bd62083600017db3bb\","
            + "\"5cc051bd62083600017db3bb\",\"5cc051bd62083600017db3bb\",\"5cc051bd62083600017db3bb\"]},{\"id\":"
            + "\"5d11fad12c945100017660ee\",\"name\":\"testArmy0\",\"units\":[\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\",\"5cc051bd62083600017db3b6\",\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\",\"5cc051bd62083600017db3b6\",\"5cc051bd62083600017db3b6\","
            + "\"5cc051bd62083600017db3b6\",\"5cc051bd62083600017db3b6\",\"5cc051bd62083600017db3b6\"]}]}";
        int status = 200;
        String errorMsg = "";
        return new HttpRequestResponse(httpReqRepBodyQueryArimes, status, errorMsg);
    }

    private HttpRequestResponse getQueryUnitsRequestTestResponse() {
        String httpReqRepBodyQueryUnits = "{\"status\":\"success\",\"message\":\"\",\"data\":[{\"id\":"
            + "\"5cc051bd62083600017db3b6\",\"type\":\"Infantry\",\"mp\":3,\"hp\":10,\"canAttack\":[\"Infantry\","
            + "\"Bazooka Trooper\",\"Jeep\",\"Light Tank\",\"Heavy Tank\"]},{\"id\":\"5cc051bd62083600017db3b7\","
            + "\"type\":\"Bazooka Trooper\",\"mp\":2,\"hp\":10,\"canAttack\":[\"Jeep\",\"Light Tank\",\"Heavy Tank\","
            + "\"Chopper\"]},{\"id\":\"5cc051bd62083600017db3b8\",\"type\":\"Jeep\",\"mp\":8,\"hp\":10,\"canAttack\":"
            + "[\"Infantry\",\"Bazooka Trooper\",\"Jeep\"]},{\"id\":\"5cc051bd62083600017db3b9\",\"type\":"
            + "\"Light Tank\",\"mp\":6,\"hp\":10,\"canAttack\":[\"Infantry\",\"Bazooka Trooper\",\"Jeep\","
            + "\"Light Tank\","
            + "\"Heavy Tank\"]},{\"id\":\"5cc051bd62083600017db3ba\",\"type\":\"Heavy Tank\",\"mp\":4,\"hp\":10,"
            + "\"canAttack\":[\"Infantry\",\"Bazooka Trooper\",\"Jeep\",\"Light Tank\",\"Heavy Tank\",\"Chopper\"]},"
            + "{\"id\":\"5cc051bd62083600017db3bb\",\"type\":\"Chopper\",\"mp\":6,\"hp\":10,\"canAttack\":"
            + "[\"Infantry\",\"Bazooka Trooper\",\"Jeep\",\"Light Tank\",\"Heavy Tank\"]}]}";
        int status = 200;
        String errorMsg = "";
        return new HttpRequestResponse(httpReqRepBodyQueryUnits, status, errorMsg);
    }

    private HttpRequestResponse getUpdateArmyRequestResponse() {
        String httpReqRepBodyUpdateArmy = "{\"status\":\"success\",\"message\":\"{\\\"id\\\":"
            + "\\\"5d11fad12c945100017660ee\\\",\\\"name\\\":\\\"hello\\\",\\\"units\\\":["
            + "\\\"5cc051bd62083600017db3b7\\\",\\\"5cc051bd62083600017db3b7\\\",\\\"5cc051bd62083600017db3b7\\\","
            + "\\\"5cc051bd62083600017db3b7\\\",\\\"5cc051bd62083600017db3b7\\\",\\\"5cc051bd62083600017db3b7\\\","
            + "\\\"5cc051bd62083600017db3b7\\\",\\\"5cc051bd62083600017db3b7\\\",\\\"5cc051bd62083600017db3b7\\\","
            + "\\\"5cc051bd62083600017db3b7\\\"]}\",\"data\":{}}";
        int status = 200;
        String errorMsg = "";
        return new HttpRequestResponse(httpReqRepBodyUpdateArmy, status, errorMsg);
    }


    @Test
    public void createArmyRequestTest() {
        String name = "TestBArmy";

        List<Unit> units = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            units.add(new Unit("5cc051bd62083600017db3b6"));
        }
        try {
            when(httpManager.post(any(), any(), any())).thenReturn(getCreateArmyRequestResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
        CreateArmyRequest req = new CreateArmyRequest(name, units, fakeUserKey);

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

    @Test
    public void getSpecificArmyRequestTest() {
        try {
            when(httpManager.get(any(), any())).thenReturn(getGetSpecificArmyResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetSpecificArmyRequest req = new GetSpecificArmyRequest(fakeArmyId, fakeUserKey);
        req.sendRequest();
        Army reqArmy = req.getData();
        Assert.assertTrue(req.getSuccessful());
        Assert.assertEquals("testArmy001", reqArmy.getName());
        Assert.assertNotNull(reqArmy.getUnits().get(0));
        Assert.assertEquals("Infantry", reqArmy.getUnits().get(0).getType());
    }

    @Test
    public void queryArmiesRequestTest() {
        try {
            when(httpManager.get(any(), any())).thenReturn(getQueryArmiesRequestResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
        QueryArmiesRequest req = new QueryArmiesRequest(fakeUserKey);
        req.sendRequest();
        ArrayList<Army> armies = req.getData();
        Assert.assertTrue(req.getSuccessful());
        boolean containsArmyID = false;
        for (Army army : armies) {
            if (army.getId().equals(fakeArmyId)) {
                containsArmyID = true;
                Assert.assertNotNull(army.getUnits().get(0));
                Assert.assertEquals("Infantry", army.getUnits().get(0).getType());
            }
        }
        Assert.assertTrue(containsArmyID);
    }

    @Test
    public void queryUnitsRequestTest() {
        try {
            when(httpManager.get(any(), any())).thenReturn(getQueryUnitsRequestTestResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
        QueryUnitsRequest req = new QueryUnitsRequest(fakeUserKey);
        req.sendRequest();
        ArrayList<Unit> unitList = req.getData();
        Assert.assertTrue(req.getSuccessful());
        Assert.assertEquals(6, unitList.size());
        Assert.assertEquals("Infantry", unitList.get(5).getCanAttack().get(0));
    }


    @Test
    public void updateArmyRequestTest() {
        try {
            when(httpManager.put(any(), any(), any())).thenReturn(getUpdateArmyRequestResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Army testArmy = new Army(null, null, null);
        testArmy.setId(fakeArmyId);
        List<Unit> units = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            units.add(new Unit("5cc051bd62083600017db3b7"));
        }

        testArmy.setUnits(units);
        testArmy.setName("changedName");
        UpdateArmyRequest req = new UpdateArmyRequest(testArmy, fakeUserKey);
        req.sendRequest();
        Assert.assertTrue(req.getSuccessful());
        UpdateArmyRequest req2 = new UpdateArmyRequest(testArmy.getId(), testArmy.getName(),
            testArmy.getUnits(), fakeUserKey);
        req2.sendRequest();
        Assert.assertTrue(req2.getSuccessful());

    }
}
