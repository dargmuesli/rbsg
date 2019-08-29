package de.uniks.se1ss19teamb.rbsg.request;

import static org.mockito.Mockito.*;

import de.uniks.se1ss19teamb.rbsg.Main;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.GameMeta;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.ui.ArmyManagerController;
import de.uniks.se1ss19teamb.rbsg.util.RequestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.http.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;


@ExtendWith(ApplicationExtension.class)
class RestRequestTestsMocked {

    private HttpManager httpManager = mock(HttpManager.class);
    private String fakeUserKey = "dca2a697-ecfb-4987-ae95-2fdfe9f4a731";
    private String fakeArmyId = "5d11fad12c945100017660ee";

    @Start
    public void start(Stage stage) {
        AnchorPane apn = new AnchorPane();
        apn.setId("apnFade");
        stage.setScene(new Scene(apn));
        Main.PRIMARY_STAGE = stage;

        AbstractRestRequest.httpManager = httpManager;
        ArmyManagerController.availableUnits.put("5cc051bd62083600017db3b6",
            new Unit("5cc051bd62083600017db3b6", "Infantry", 3, 3, new ArrayList<>()));
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

    @Test
    void createArmyRequestTest() {
        try {
            when(httpManager.post(any(), any(), any())).thenReturn(getCreateArmyRequestResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<String> optional = RequestUtil.request(new CreateArmyRequest("", new ArrayList<>(), fakeUserKey));

        if (optional.isPresent()) {
            Assert.assertEquals("5d11fad12c945100017660ee", optional.get());
        } else {
            Assert.fail();
        }
    }

    private HttpRequestResponse getCreateGameResponse() {
        String httpReqRepBodyCreateGame = "{\"status\":\"success\",\"message\":\"test\",\"data\":{\"gameId\":"
            + "\"123456789012345678901234\"}}";
        int status = 200;
        String errorMsg = "test";
        return new HttpRequestResponse(httpReqRepBodyCreateGame, status, errorMsg);
    }

    @Test
    void createGameTest() throws ParseException {
        try {
            when(httpManager.post(any(), any(), any())).thenReturn(getCreateGameResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<String> optional;

        optional = RequestUtil.request(
            new CreateGameRequest("TeamBTestUserGame", 2, "111111111111111111111111111111111111"));

        if (optional.isPresent()) {
            Assert.assertEquals(24, optional.get().length());
        } else {
            Assert.fail();
        }

        optional = RequestUtil.request(
            new CreateGameRequest("TeamBTestUserGame", 2, 123, "111111111111111111111111111111111111"));

        if (optional.isPresent()) {
            Assert.assertEquals(24, optional.get().length());
        } else {
            Assert.fail();
        }
    }

    private HttpRequestResponse getDeleteArmyRequestResponse() {
        String httpReqRepBodyDeleteArmy = "{\"status\":\"success\",\"message\":\"Army deleted\",\"data\":{}}";
        int status = 200;
        String errorMsg = "";
        return new HttpRequestResponse(httpReqRepBodyDeleteArmy, status, errorMsg);
    }

    @Test
    void deleteArmyRequestTest() {
        try {
            when(httpManager.delete(any(), any(), any())).thenReturn(getDeleteArmyRequestResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        DeleteArmyRequest request = new DeleteArmyRequest(fakeArmyId, fakeUserKey);

        if (RequestUtil.request(request)) {
            Assert.assertEquals("Army deleted", request.getMessage());
        } else {
            Assert.fail();
        }
    }

    private HttpRequestResponse getDeleteGameResponse() {
        String httpReqRepBody = "{\"status\":\"success\",\"message\":\"Game deleted\",\"data\":{\"userKey\":"
            + "\"111111111111111111111111111111111111\"}}";
        int status = 200;
        String errorMsg = "test";
        return new HttpRequestResponse(httpReqRepBody, status, errorMsg);
    }

    @Test
    void deleteGameTest() throws ParseException {
        try {
            when(httpManager.delete(any(), any(), any())).thenReturn(getDeleteGameResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        DeleteGameRequest request = new DeleteGameRequest("123456789012345678901234",
            "111111111111111111111111111111111111");

        if (RequestUtil.request(request)) {
            Assert.assertEquals("Game deleted", request.getMessage());
        } else {
            Assert.fail();
        }
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

    @Test
    void getSpecificArmyRequestTest() {
        try {
            when(httpManager.get(any(), any())).thenReturn(getGetSpecificArmyResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<Army> optional = RequestUtil.request(new GetSpecificArmyRequest(fakeArmyId, fakeUserKey));

        if (optional.isPresent()) {
            Assert.assertEquals("testArmy001", optional.get().getName());
            Assert.assertNotNull(optional.get().getUnits().get(0));
            Assert.assertEquals("Infantry", optional.get().getUnits().get(0).getType());
        } else {
            Assert.fail();
        }
    }

    private HttpRequestResponse getJoinGameResponse() {
        String httpReqRepBody = "{\"status\":\"success\",\"message\":\"Game joined, you will be disconnected "
            + "from the chat and the system socket. Please connect to /ws/game?gameId=GAME_ID\",\"data\":"
            + "{\"userKey\":\"111111111111111111111111111111111111\"}}";
        int status = 200;
        String errorMsg = "test";
        return new HttpRequestResponse(httpReqRepBody, status, errorMsg);
    }

    @Test
    void joinGameTest() throws ParseException {
        try {
            when(httpManager.get(any(), any())).thenReturn(getJoinGameResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JoinGameRequest request =
            new JoinGameRequest("123456789012345678901234", "111111111111111111111111111111111111");

        if (RequestUtil.request(request)) {
            Assert.assertEquals("Game joined, you will be disconnected from the chat and the system socket. "
                + "Please connect to /ws/game?gameId=GAME_ID", request.getMessage());
        } else {
            Assert.fail();
        }
    }

    private HttpRequestResponse getLoginUserResponse() {
        String httpReqRepBody = "{\"status\":\"success\",\"message\":\"\",\"data\":{\"userKey\":"
            + "\"111111111111111111111111111111111111\"}}";
        int status = 200;
        String errorMsg = "Bad Request";
        return new HttpRequestResponse(httpReqRepBody, status, errorMsg);
    }

    @Test
    void loginUserTest() {
        try {
            when(httpManager.post(any(), any(), any())).thenReturn(getLoginUserResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<String> optional = RequestUtil.request(new LoginUserRequest("TeamBTestUser", "qwertz"));

        if (optional.isPresent()) {
            Assert.assertEquals(36, optional.get().length());
        } else {
            Assert.fail();
        }
    }

    private HttpRequestResponse getLogoutUserResponse() {
        String httpReqRepBodyLogout = "{\"status\":\"success\",\"message\":\"Logged out\",\"data\":{}}";
        int statusLogout = 200;
        String errorMsgLogout = "test";
        return new HttpRequestResponse(httpReqRepBodyLogout, statusLogout, errorMsgLogout);
    }

    @Test
    void logoutUserTest() throws ParseException {
        try {
            when(httpManager.get(any(), any())).thenReturn(getLogoutUserResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogoutUserRequest request = new LogoutUserRequest("111111111111111111111111111111111111");

        if (RequestUtil.request(request)) {
            Assert.assertEquals("Logged out", request.getMessage());
        } else {
            Assert.fail();
        }
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

    @Test
    void queryArmiesRequestTest() {
        try {
            when(httpManager.get(any(), any())).thenReturn(getQueryArmiesRequestResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<ArrayList<Army>> optional = RequestUtil.request(new QueryArmiesRequest(fakeUserKey));

        if (optional.isPresent()) {
            ArrayList<Army> armies = optional.get();
            boolean containsArmyID = false;

            for (Army army : armies) {
                if (army.getId().equals(fakeArmyId)) {
                    containsArmyID = true;
                    Assert.assertNotNull(army.getUnits().get(0));
                    Assert.assertEquals("Infantry", army.getUnits().get(0).getType());
                }
            }

            Assert.assertTrue(containsArmyID);
        } else {
            Assert.fail();
        }
    }

    private HttpRequestResponse getQueryGamesResponse() {
        String httpReqRepBodyQueryGames = "{\"status\":\"success\",\"message\":\"test\",\"data\":"
            + "[{\"id\":\"123456789012345678901234\",\"name\":\"TeamBTestUserGame\",\"neededPlayer\":2,"
            + "\"joinedPlayer\":0}]}";
        int statusLogout = 200;
        String errorMsgLogout = "test";
        return new HttpRequestResponse(httpReqRepBodyQueryGames, statusLogout, errorMsgLogout);
    }

    @Test
    void queryGamesTest() throws ParseException {
        try {
            when(httpManager.get(any(), any())).thenReturn(getQueryGamesResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<HashMap<String, GameMeta>> optional =
            RequestUtil.request(new QueryGamesRequest("111111111111111111111111111111111111"));

        if (optional.isPresent()) {
            final boolean[] hasTeamBTestGame = {false};
            optional.get().forEach((s, gameMeta)
                -> hasTeamBTestGame[0] |= gameMeta.getName().equals("TeamBTestUserGame"));
            Assert.assertTrue(hasTeamBTestGame[0]);
        } else {
            Assert.fail();
        }
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

    @Test
    void queryUnitsRequestTest() {
        try {
            when(httpManager.get(any(), any())).thenReturn(getQueryUnitsRequestTestResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<ArrayList<Unit>> optional = RequestUtil.request(new QueryUnitsRequest(fakeUserKey));

        if (optional.isPresent()) {
            ArrayList<Unit> unitList = optional.get();
            Assert.assertEquals(6, unitList.size());
            Assert.assertEquals("Infantry", unitList.get(5).getCanAttack().get(0));
        } else {
            Assert.fail();
        }
    }

    private HttpRequestResponse getQueryUsersResponse() {
        String httpReqRepBody = "{\"status\":\"success\",\"message\":\"\",\"data\":[\"TeamBTestUser\"]}";
        int status = 200;
        String errorMsg = "test";
        return new HttpRequestResponse(httpReqRepBody, status, errorMsg);
    }

    @Test
    void queryUsersInLobbyTest() throws ParseException {
        try {
            when(httpManager.get(any(), any())).thenReturn(getQueryUsersResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<ArrayList<String>> optional = RequestUtil.request(
            new QueryUsersInLobbyRequest("111111111111111111111111111111111111"));

        if (optional.isPresent()) {
            Assert.assertTrue(optional.get().contains("TeamBTestUser"));
        } else {
            Assert.fail();
        }
    }

    private HttpRequestResponse getRegisterUserResponse() {
        String httpReqRepBody = "{\"status\":\"failure\",\"message\":\"Name already taken\",\"data\":{}}";
        int status = 400;
        String errorMsg = "Bad Request";
        return new HttpRequestResponse(httpReqRepBody, status, errorMsg);
    }

    @Test
    void registerUserTest() {
        try {
            when(httpManager.post(any(), any(), any())).thenReturn(getRegisterUserResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        RegisterUserRequest request = new RegisterUserRequest("TeamBTestUser", "qwertz");

        if (!RequestUtil.request(request)) {
            Assert.assertEquals("Name already taken", request.getMessage());
        } else {
            Assert.fail();
        }
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
    void updateArmyRequestTest() {
        try {
            when(httpManager.put(any(), any(), any())).thenReturn(getUpdateArmyRequestResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Unit> units = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            units.add(new Unit("5cc051bd62083600017db3b7"));
        }

        if (RequestUtil.request(new UpdateArmyRequest(new Army(fakeArmyId, "changedName", units), fakeUserKey))) {
            // TODO what does this endpoint return?!
        } else {
            Assert.fail();
        }
    }
}
