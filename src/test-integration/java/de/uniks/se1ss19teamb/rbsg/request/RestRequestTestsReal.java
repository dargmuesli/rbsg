package de.uniks.se1ss19teamb.rbsg.request;

import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Game;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import java.util.ArrayList;
import org.apache.http.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;


public class RestRequestTestsReal {

    private String userKey;

    @Test
    public void registerUserTest() {

        RegisterUserRequest req = new RegisterUserRequest("testTeamB", "qwertz");
        try {
            //Test Returning Json
            //Demonstration on how to directly process Json with Lambda
            req.sendRequest((response) -> {
                Assert.assertEquals("failure", response.get("status").getAsString());
                Assert.assertEquals("Name already taken", response.get("message").getAsString());
            });

            //Test Request Helpers
            //This is the way one should query information from the Request Handlers
            //ALWAYS!!! check getSuccessful first, since if it returns false, all other methods
            //except getMessage have undefined behaviour or might throw Exceptions
            Assert.assertFalse(req.getSuccessful());
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
            Assert.assertTrue(req.getSuccessful());
            Assert.assertEquals(36, req.getUserKey().length());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void queryUsersInLobbyTest() throws ParseException {
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();

        QueryUsersInLobbyRequest req = new QueryUsersInLobbyRequest(login.getUserKey());
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
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();

        LogoutUserRequest req = new LogoutUserRequest(login.getUserKey());
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
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();

        CreateGameRequest req = new CreateGameRequest("testTeamBGame", 2, login.getUserKey());
        try {
            //Query Request
            req.sendRequest();

            Assert.assertTrue(req.getSuccessful());
            Assert.assertEquals(24, req.getGameId().length());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void queryGamesTest() throws ParseException {
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();

        CreateGameRequest createGame = new CreateGameRequest("testTeamBGame", 2, login.getUserKey());
        createGame.sendRequest();

        QueryGamesRequest req = new QueryGamesRequest(login.getUserKey());
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
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();

        CreateGameRequest createGame = new CreateGameRequest("testTeamBGame", 2, login.getUserKey());
        createGame.sendRequest();

        DeleteGameRequest req = new DeleteGameRequest(createGame.getGameId(), login.getUserKey());
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
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();

        CreateGameRequest createGame = new CreateGameRequest("testTeamBGame", 2, login.getUserKey());
        createGame.sendRequest();

        JoinGameRequest req = new JoinGameRequest(createGame.getGameId(), login.getUserKey());
        try {
            req.sendRequest();

            Assert.assertTrue(req.getSuccessful());
            Assert.assertEquals("Game joined, you will be disconnected from the chat and the"
                + " system socket. Please connect to "
                + "/ws/game?gameId=GAME_ID", req.getMessage());

            //Check if we actually joined the game
            QueryGamesRequest query = new QueryGamesRequest(login.getUserKey());
            query.sendRequest();

            Assert.assertEquals(1, query.getGames().stream().filter((game) -> game.getId()
                .equals(createGame.getGameId())).findFirst().get().getJoinedPlayers());
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    private LoginUserRequest loginUser() {
        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();
        userKey = login.getUserKey();
        return login;
    }

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

    @Test
    public void createArmyRequestTest() {
        String name = "TestBArmy";
        LoginUserRequest login = loginUser();

        ArrayList<String> unitIDs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            unitIDs.add("5cc051bd62083600017db3b6");
        }
        CreateArmyRequest req = new CreateArmyRequest(name, unitIDs, login.getUserKey());

        req.sendRequest();
        Assert.assertTrue(req.getSuccessful());
        deleteArmy(req.getArmyID());
    }

    @Test
    public void deleteArmyRequestTest() {
        String armyId;
        loginUser();
        CreateArmyRequest createArmyRequest = createArmy();
        armyId = createArmyRequest.getArmyID();
        DeleteArmyRequest req = new DeleteArmyRequest(armyId, userKey);
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
}
