package de.uniks.se1ss19teamb.rbsg.request;

import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.GameMeta;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.ui.ArmyManagerController;
import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.util.RequestUtil;

import java.util.*;

import org.apache.http.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Rest request tests against the real gameserver.
 */
public class RestRequestTestsReal {

    /**
     * A list of units that exist on the gameserver as retrieved by {@link #queryUnits}.
     */
    public static List<Unit> unitList;

    /**
     * A game id of the game that is used for the current test as retrieved by {@link #createGame}.
     */
    public static String gameId;

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

        Optional<String> optional = RequestUtil.request(
            new CreateArmyRequest("testArmy001", units, LoginController.getUserToken()));

        if (optional.isPresent()) {
            armyId = optional.get();
        } else {
            Assert.fail();
        }
    }

    /**
     * Creates a game named "TeamBTestUserGame" for two players using {@link RestRequestTestsReal}'s user token.
     * The id of the created game is saved in {@link #gameId}.
     * Fails {@link Assert} if unsuccessful.
     */
    public static void createGame() {
        Optional<String> optional = RequestUtil.request(
            new CreateGameRequest("TeamBTestUserGame", 2, LoginController.getUserToken()));

        if (optional.isPresent()) {
            gameId = optional.get();
        } else {
            Assert.fail();
        }
    }

    private void deleteArmy(String armyID) {
        if (!RequestUtil.request(new DeleteArmyRequest(armyID, LoginController.getUserToken()))) {
            Assert.fail();
        }
    }

    /**
     * Logs in the user named "TeamBTestUser".
     */
    public static void loginUser() {
        loginUser("TeamBTestUser");
    }

    /**
     * Logs the specified user in, using the password "qwertz".
     * The returned user token is saved in {@link LoginController#getUserToken()}.
     * Fails {@link Assert} if unsuccessful.
     *
     * @param username The username to log in with.
     */
    public static void loginUser(String username) {
        Optional<String> optional = RequestUtil.request(new LoginUserRequest(username, "qwertz"));

        if (optional.isPresent()) {
            LoginController.setUserName(username);
            LoginController.setUserToken(optional.get());
        } else {
            Assert.fail();
        }
    }

    /**
     * Queries the server's units and updates {@link ArmyManagerController#availableUnits}.
     * The units returned are saved in {@link #unitList}.
     * Fails {@link Assert} if unsuccessful.
     */
    public static void queryUnits() {
        Optional<ArrayList<Unit>> optional = RequestUtil.request(new QueryUnitsRequest(LoginController.getUserToken()));

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

    /**
     * Sends an {@link JoinGameRequest} using the {@link #gameId} and the {@link LoginController#getUserToken()}.
     * Fails {@link Assert} if unsuccessful.
     */
    public static void joinGame() {
        if (!RequestUtil.request(new JoinGameRequest(gameId, LoginController.getUserToken()))) {
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

        Optional<String> optional = RequestUtil.request(
            new CreateArmyRequest("TestBArmy", units, LoginController.getUserToken()));

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
            new CreateGameRequest("TeamBTestUserGame", 2, LoginController.getUserToken()));

        if (optional.isPresent()) {
            Assert.assertEquals(24, optional.get().length());
        } else {
            Assert.fail();
        }

        optional = RequestUtil.request(
            new CreateGameRequest("TeamBTestUserGame", 2, LoginController.getUserToken(), 123));

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

        DeleteArmyRequest request = new DeleteArmyRequest(armyId, LoginController.getUserToken());

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

        DeleteGameRequest request = new DeleteGameRequest(gameId, LoginController.getUserToken());

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

        Optional<Army> optional = RequestUtil.request(
            new GetSpecificArmyRequest(armyId, LoginController.getUserToken()));

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
            new JoinGameRequest(gameId, LoginController.getUserToken());

        if (RequestUtil.request(request)) {
            Assert.assertEquals("Game joined, you will be disconnected from the chat and the system socket. "
                + "Please connect to /ws/game?gameId=GAME_ID(&armyId=ARMY_ID)", request.getMessage());

            //Check if we actually joined the game
            Optional<HashMap<String, GameMeta>> optional = RequestUtil.request(
                new QueryGamesRequest(LoginController.getUserToken()));

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
        Optional<String> optional = RequestUtil.request(
            new LoginUserRequest("TeamBTestUser", "qwertz"));

        if (optional.isPresent()) {
            Assert.assertEquals(36, optional.get().length());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void logoutUserTest() throws ParseException {
        loginUser();

        LogoutUserRequest request = new LogoutUserRequest(LoginController.getUserToken());

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

        Optional<ArrayList<Army>> optional = RequestUtil.request(
            new QueryArmiesRequest(LoginController.getUserToken()));

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
            RequestUtil.request(new QueryGamesRequest(LoginController.getUserToken()));

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

        Optional<ArrayList<Unit>> optional = RequestUtil.request(new QueryUnitsRequest(LoginController.getUserToken()));

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
            new QueryUsersInLobbyRequest(LoginController.getUserToken()));

        if (optional.isPresent()) {
            Assert.assertTrue(optional.get().contains("<"));
            //TODO < to TeamBTestUser when server fixes the problem
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

        if (RequestUtil.request(new UpdateArmyRequest(
            new Army(armyId, "changedName", units), LoginController.getUserToken()))) {
            // TODO what does this endpoint return?!
        } else {
            Assert.fail();
        }
    }

    /**
     * Logs in with {@link #loginUser}, requests and deletes all armies, requests all games and deletes all own games.
     */
    @After
    public void cleanupGames() {
        loginUser();

        RequestUtil.request(new QueryArmiesRequest(LoginController.getUserToken())).ifPresent(armies -> {
            for (Army a : armies) {
                if (!RequestUtil.request(new DeleteArmyRequest(a.getId(), LoginController.getUserToken()))) {
                    Assert.fail();
                }
            }
        });

        RequestUtil.request(new QueryGamesRequest(LoginController.getUserToken())).ifPresent(
            stringGameMetaHashMap -> stringGameMetaHashMap.entrySet().stream().filter(
                stringGameMetaEntry -> stringGameMetaEntry.getValue().getName()
                    .equals("TeamBTestUserGame"))
                .forEach(stringGameMetaEntry -> {
                    System.out.println("Tidying up Game " + stringGameMetaEntry.getValue().getName()
                        + " with id " + stringGameMetaEntry.getValue().getId() + "...");

                    if (!RequestUtil.request(new DeleteGameRequest(stringGameMetaEntry.getValue().getId(),
                        LoginController.getUserToken()))) {

                        Assert.fail();
                    }
                }));
    }
}
