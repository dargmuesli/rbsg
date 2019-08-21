package de.uniks.se1ss19teamb.rbsg.bot;

import de.uniks.se1ss19teamb.rbsg.request.CreateTemporaryUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocketDistributor;
import de.uniks.se1ss19teamb.rbsg.ui.BotSelectionController;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.util.ArrayList;

import org.apache.logging.log4j.Logger;


public class BotControl {

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();

    private static ArrayList<BotUser> botUsers = new ArrayList<>();

    private static InGameController inGameController;

    private static String gameId;

    public static void setGameId(String id) {
        gameId = id;
    }

    public static void createBotUser(int number, int difficulty, BotSelectionController botSelectionController) {
        BotUser botUser = new BotUser();
        botUser.setBotNumber(number);
        botUser.setGameId(gameId);
        CreateTemporaryUserRequest ctur = new CreateTemporaryUserRequest();
        ctur.sendRequest();
        botUser.setBotUserName(ctur.getData().get(0));
        botUser.setBotUserPassword(ctur.getData().get(1));

        LoginUserRequest loginUserRequest = new LoginUserRequest(botUser.getBotUserName(),
            botUser.getBotUserPassword());
        loginUserRequest.sendRequest();
        botUser.setBotUserKey(loginUserRequest.getData());
        botUsers.add(number, botUser);
        botSelectionController.setBot(botUser);
        botUser.joinGame();

        if (GameSocketDistributor.getGameSocket(number + 1) == null) {
            GameSocketDistributor.setGameSocket(number + 1, gameId);
        }
        botUser.setGameSocket(GameSocketDistributor.getGameSocket(number + 1));
        botUser.instantiateBotAi(difficulty);
        botUser.setInGameController(inGameController);
        botUser.connectGamesocket();

        //Just info for dev. Will be deleted later
        NotificationHandler.sendInfo(loginUserRequest.getData(), logger);
        NotificationHandler.sendInfo("Bot Username: " + botUser.getBotUserName()
            + "\nBot Password: " + botUser.getBotUserPassword(), logger);
    }

    public static void setInGameController(InGameController inGameControllerInput) {
        inGameController = inGameControllerInput;
    }
}
