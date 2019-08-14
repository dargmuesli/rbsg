package de.uniks.se1ss19teamb.rbsg.ai;

import de.uniks.se1ss19teamb.rbsg.request.CreateTemporaryUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocketDistributor;
import de.uniks.se1ss19teamb.rbsg.ui.BotManagerController;
import de.uniks.se1ss19teamb.rbsg.ui.BotSelectionController;
import de.uniks.se1ss19teamb.rbsg.ui.GameSelectionController;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.logging.LogManager;

public class BotControl {

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();

    private static ArrayList<BotUser> botUsers = new ArrayList<>();

    private InGameController inGameController;

    private static String gameId;

    public static void setGameId(String id) {
        gameId = id;
    }

    public static void createBotUser(int number, BotSelectionController botSelectionController) {
        BotUser botUser = new BotUser();
        botUser.setBotNumber(number);
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

        if (GameSocketDistributor.getGameSocket(number + 1 ) == null) {
            GameSocketDistributor.setGameSocket(number + 1, gameId);
        }
        //TODO: inGameController and GameSocket have to be set.
        botUser.createBotAi();

        //Just info for dev. Will be deleted later
        NotificationHandler.getInstance().sendInfo(loginUserRequest.getData(), logger);
        NotificationHandler.getInstance().sendInfo("Bot Username: " + botUser.getBotUserName() +
            "\nBot Password: " + botUser.getBotUserPassword(), logger);
    }

    public void setInGameController(InGameController inGameController) {
        this.inGameController = inGameController;
    }
}
