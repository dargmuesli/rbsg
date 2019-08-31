package de.uniks.se1ss19teamb.rbsg.bot;

import de.uniks.se1ss19teamb.rbsg.request.CreateGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocketDistributor;
import de.uniks.se1ss19teamb.rbsg.ui.ArmyManagerController;
import de.uniks.se1ss19teamb.rbsg.ui.BotSelectionController;
import org.junit.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.mockito.Mockito.mock;

public class BotTestsReal extends ApplicationTest {

    String userKey;
    GameSocket testGameSocket;
    String gameId;


    public void SetupGame() {
        LoginUserRequest lur = new LoginUserRequest("TeamBTestUser", "qwertz");
        lur.sendRequest();
        userKey = lur.getData();

        CreateGameRequest cgr = new CreateGameRequest("TestGameB", 2, userKey);
        cgr.sendRequest();
        gameId = cgr.getData();
        GameSocketDistributor.setGameSocket(0, gameId);
        testGameSocket = GameSocketDistributor.getGameSocket(0);
    }

    @Test
    public void testBotControl() {
        BotSelectionController botSelectionController = mock(BotSelectionController.class);
        BotControl.setGameId(gameId);
        BotControl.createBotUser(0, -1, botSelectionController);


    }


}
