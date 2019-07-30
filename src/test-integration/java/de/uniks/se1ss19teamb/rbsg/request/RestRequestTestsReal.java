package de.uniks.se1ss19teamb.rbsg.request;

import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.GameMeta;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.ui.ArmyManagerController;
import de.uniks.se1ss19teamb.rbsg.util.RequestUtil;

import static org.mockito.Mockito.mock;

import java.util.*;

import org.apache.http.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class RestRequestTestsReal {
    public static List<Unit> unitList;
    public static String gameId;
    public static String userToken;

    private String armyId;

    // HELPERS /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void resetHttpManager() {
        AbstractRestRequest.httpManager = new HttpManager();
    }
    
    @Before
    public void setupTests() {
    	resetHttpManager();
    }
    
    private void createArmy() {
        List<Unit> units = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            units.add(unitList.get(0));
        }

        Optional<String> optional = RequestUtil.request(new CreateArmyRequest("testArmy001", units, userToken));

        if (optional.isPresent()) {
            armyId = optional.get();
        } else {
            Assert.fail();
        }
    }

    public static void createGame() {
        Optional<String> optional = RequestUtil.request(new CreateGameRequest("TeamBTestUserGame", 2, userToken));

        if (optional.isPresent()) {
            gameId = optional.get();
        } else {
            Assert.fail();
        }
    }

    private void deleteArmy(String armyID) {
        if (!RequestUtil.request(new DeleteArmyRequest(armyID, userToken))) {
            Assert.fail();
        }
    }

    public static void loginUser() {
        loginUser("TeamBTestUser");
    }

    public static void loginUser(String username) {
        Optional<String> optional = RequestUtil.request(new LoginUserRequest(username, "qwertz"));

        if (optional.isPresent()) {
            userToken = optional.get();
        } else {
            Assert.fail();
        }
    }

    public static void queryUnits() {
        Optional<ArrayList<Unit>> optional = RequestUtil.request(new QueryUnitsRequest(userToken));

        if (optional.isPresent()) {
            unitList = optional.get();

            ArmyManagerController.availableUnits.clear();

            for (Unit unit : optional.get()) {
                ArmyManagerController.availableUnits.put(unit.getId(), unit);
            }
        } else {
            Assert.fail();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void createArmyRequestTest() {
        loginUser();
        queryUnits();

        List<Unit> units = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            units.add(unitList.get(0));
        }

        Optional<String> optional = RequestUtil.request(new CreateArmyRequest("TestBArmy", units, userToken));

        if (optional.isPresent()) {
            deleteArmy(optional.get());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void createGameTest() throws ParseException {
        loginUser();

        Optional<String> optional;

        optional = RequestUtil.request(
            new CreateGameRequest("TeamBTestUserGame", 2, userToken));

        if (optional.isPresent()) {
            Assert.assertEquals(24, optional.get().length());
        } else {
            Assert.fail();
        }

        optional = RequestUtil.request(
            new CreateGameRequest("TeamBTestUserGame", 2, userToken, 123));

        if (optional.isPresent()) {
            Assert.assertEquals(24, optional.get().length());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void deleteArmyRequestTest() {
        loginUser();
        queryUnits();
        createArmy();

        DeleteArmyRequest request = new DeleteArmyRequest(armyId, userToken);

        if (RequestUtil.request(request)) {
            Assert.assertEquals("Army deleted", request.getMessage());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void deleteGameTest() throws ParseException {
        loginUser();
        createGame();

        DeleteGameRequest request = new DeleteGameRequest(gameId, userToken);

        if (RequestUtil.request(request)) {
            Assert.assertEquals("Game deleted", request.getMessage());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void getSpecificArmyRequestTest() {
        loginUser();
        queryUnits();
        createArmy();

        Optional<Army> optional = RequestUtil.request(new GetSpecificArmyRequest(armyId, userToken));

        if (optional.isPresent()) {
            Assert.assertEquals("testArmy001", optional.get().getName());
            Assert.assertNotNull(optional.get().getUnits().get(0));
            Assert.assertEquals("Infantry", optional.get().getUnits().get(0).getType());
        } else {
            Assert.fail();
        }

        deleteArmy(optional.get().getId());
    }

    @Test
    public void joinGameTest() throws ParseException {
        loginUser();
        createGame();

        JoinGameRequest request =
            new JoinGameRequest(gameId, userToken);

        if (RequestUtil.request(request)) {
            Assert.assertEquals("Game joined, you will be disconnected from the chat and the system socket. "
                + "Please connect to /ws/game?gameId=GAME_ID(&armyId=ARMY_ID)", request.getMessage());

            //Check if we actually joined the game
            Optional<HashMap<String, GameMeta>> optional = RequestUtil.request(new QueryGamesRequest(userToken));

            if (optional.isPresent()) {
                Optional<Map.Entry<String, GameMeta>> optionalEntry = optional.get().entrySet().stream()
                    .filter(stringGameMetaEntry -> stringGameMetaEntry.getKey()
                        .equals(gameId)).findFirst();
                Assert.assertEquals(1L, (long) optionalEntry.map(stringGameMetaEntry
                    -> stringGameMetaEntry.getValue().getJoinedPlayers()).orElse(0L));
            } else {
                Assert.fail();
            }
        } else {
            Assert.fail();
        }
    }

    @Test
    public void loginUserTest() {
        Optional<String> optional = RequestUtil.request(new LoginUserRequest("TeamBTestUser", "qwertz"));

        if (optional.isPresent()) {
            Assert.assertEquals(36, optional.get().length());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void logoutUserTest() throws ParseException {
        loginUser();

        LogoutUserRequest request = new LogoutUserRequest(userToken);

        if (RequestUtil.request(request)) {
            Assert.assertEquals("Logged out", request.getMessage());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void queryArmiesRequestTest() {
        loginUser();
        queryUnits();
        createArmy();

        Optional<ArrayList<Army>> optional = RequestUtil.request(new QueryArmiesRequest(userToken));

        if (optional.isPresent()) {
            ArrayList<Army> armies = optional.get();
            boolean containsArmyID = false;

            for (Army army : armies) {
                if (army.getId().equals(armyId)) {
                    containsArmyID = true;
                    Assert.assertNotNull(army.getUnits().get(0));
                    Assert.assertEquals("Infantry", army.getUnits().get(0).getType());
                }
            }

            Assert.assertTrue(containsArmyID);
        } else {
            Assert.fail();
        }

        deleteArmy(armyId);
    }

    @Test
    public void queryGamesTest() throws ParseException {
        loginUser();
        createGame();

        Optional<HashMap<String, GameMeta>> optional =
            RequestUtil.request(new QueryGamesRequest(userToken));

        if (optional.isPresent()) {
            final boolean[] hasTeamBTestGame = {false};
            optional.get().forEach((s, gameMeta)
                -> hasTeamBTestGame[0] |= gameMeta.getName().equals("TeamBTestUserGame"));
            Assert.assertTrue(hasTeamBTestGame[0]);
        } else {
            Assert.fail();
        }
    }

    @Test
    public void queryUnitsRequestTest() {
        loginUser();

        Optional<ArrayList<Unit>> optional = RequestUtil.request(new QueryUnitsRequest(userToken));

        if (optional.isPresent()) {
            ArrayList<Unit> unitList = optional.get();
            Assert.assertEquals(6, unitList.size());
            Assert.assertEquals("Infantry", unitList.get(5).getCanAttack().get(0));
        } else {
            Assert.fail();
        }
    }

    @Test
    public void queryUsersInLobbyTest() throws ParseException {
        loginUser();

        Optional<ArrayList<String>> optional = RequestUtil.request(
            new QueryUsersInLobbyRequest(userToken));

        if (optional.isPresent()) {
            Assert.assertTrue(optional.get().contains("TeamBTestUser"));
        } else {
            Assert.fail();
        }
    }

    @Test
    public void registerUserTest() {
        RegisterUserRequest request = new RegisterUserRequest("TeamBTestUser", "qwertz");

        if (!RequestUtil.request(request)) {
            Assert.assertEquals("Name already taken", request.getMessage());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void updateArmyRequestTest() {
        loginUser();
        queryUnits();
        createArmy();

        List<Unit> units = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            units.add(unitList.get(1));
        }

        if (RequestUtil.request(new UpdateArmyRequest(new Army(armyId, "changedName", units), userToken))) {
            // TODO what does this endpoint return?!
        } else {
            Assert.fail();
        }
    }

    @After
    public void cleanupGames() throws ParseException {
        loginUser();

        RequestUtil.request(new QueryArmiesRequest(userToken)).ifPresent(armies -> {
            for (Army a : armies) {
                if (!RequestUtil.request(new DeleteArmyRequest(a.getId(), userToken))) {
                    Assert.fail();
                }
            }
        });

        RequestUtil.request(new QueryGamesRequest(userToken)).ifPresent(
            stringGameMetaHashMap -> stringGameMetaHashMap.entrySet().stream().filter(
                stringGameMetaEntry -> stringGameMetaEntry.getValue().getName()
            .equals("TeamBTestUserGame"))
            .forEach(stringGameMetaEntry -> {
                System.out.println("Tidying up Game " + stringGameMetaEntry.getValue().getName()
                    + " with id " + stringGameMetaEntry.getValue().getId() + "...");

                if (!RequestUtil.request(new DeleteGameRequest(stringGameMetaEntry.getValue().getId(), userToken))) {
                    Assert.fail();
                }
            }));
    }
}
