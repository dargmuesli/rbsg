package de.uniks.se1ss19teamb.rbsg.request;

import de.uniks.se1ss19teamb.rbsg.model.Game;

import org.apache.http.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;


public class RestRequestTestsReal {

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

    @Test
    public void createArmyRequestTest() {
        String name = "TestBArmy";

        LoginUserRequest login = new LoginUserRequest("testTeamB", "qwertz");
        login.sendRequest();

        ArrayList<String> unitIDs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            unitIDs.add("5cc051bd62083600017db3b6");
        }
        CreateArmyRequest req = new CreateArmyRequest(name, unitIDs, login.getUserKey());

        req.sendRequest();
        Assert.assertTrue(req.getSuccessful());

    }

    @After
    public void cleanupGames() throws ParseException {
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
