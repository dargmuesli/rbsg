package de.uniks.se1ss19teamb.rbsg.bot;

import de.uniks.se1ss19teamb.rbsg.request.CreateTemporaryUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocketDistributor;
import de.uniks.se1ss19teamb.rbsg.ui.BotSelectionController;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;

import java.util.ArrayList;


public class BotControl {

    private static ArrayList<BotUser> botUsers = new ArrayList<>();

    private static InGameController inGameController;

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

        if (GameSocketDistributor.getGameSocket(number + 1) == null) {
            GameSocketDistributor.setGameSocket(number + 1, gameId);
        }

        botUser.setGameSocket(GameSocketDistributor.getGameSocket(number + 1));
        //TODO: inGameController and GameSocket have to be set.
        botUser.instantiateBotAi();
        botUser.setInGameController(inGameController);
    }

    public static void setInGameController(InGameController inGameControllerInput) {
        inGameController = inGameControllerInput;
    }
}
