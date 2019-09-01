package de.uniks.se1ss19teamb.rbsg.bot;

import de.uniks.se1ss19teamb.rbsg.ai.AI;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.request.CreateTemporaryUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocketDistributor;
import de.uniks.se1ss19teamb.rbsg.ui.BotSelectionController;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.ui.TurnUiController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BotControl {

    private static final Logger logger = LogManager.getLogger();

    public static ArrayList<BotUser> botUsers = new ArrayList<>();

    private static InGameController inGameController;

    private static String gameId;

    public static void setGameId(String id) {
        gameId = id;
    }

    /**
     * Creates a temporary user that is run by an {@link AI}.
     *
     * @param number                 The bot's number that is used for {@link GameSocket} creation.
     * @param botSelectionController The controller on which the bot will be selected.
     */
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

        //Just info for dev. Will be deleted later
        NotificationHandler.sendInfo(loginUserRequest.getData(), logger);
        NotificationHandler.sendInfo("Bot Username: " + botUser.getBotUserName()
            + "\nBot Password: " + botUser.getBotUserPassword(), logger);
        // end info

        botUsers.add(number, botUser);
        botSelectionController.setBot(botUser);
        botUser.joinGame();

        if (GameSocketDistributor.getGameSocket(number + 1) == null) {
            GameSocketDistributor.setGameSocket(number + 1, gameId);
        }

        botUser.setGameSocket(GameSocketDistributor.getGameSocket(number + 1));
        GameSocketDistributor.getGameSocket(number + 1).setBotGameSocket();
        botUser.connectGamesocket();
        botUser.instantiateBotAi(difficulty);
        botUser.setReady();
    }

    //TODO: fix javadoc

    /**
     * Only for checkstyle.
     * Initializes the Bot.
     *
     * @param inGameControllerInput input
     */
    public static void initializeBotAi(InGameController inGameControllerInput) {
        inGameController = inGameControllerInput;
        for (InGamePlayer player : TurnUiController.getInstance().inGamePlayerList) {
            for (BotUser botUser : botUsers) {
                if (botUser.getBotUserName().equals(player.getName())) {
                    botUser.setBotPlayerId(player.getId());
                }
            }
        }
        for (BotUser botUser : botUsers) {
            botUser.initializeBotAi(inGameControllerInput);
        }
    }

    //TODO: fix javadoc

    /**
     * Only for checkstyle.
     * Checks if bot has its turn.
     *
     * @param playerId playerid
     */
    public static void checkForBotsTurn(String playerId) {
        for (InGamePlayer player : TurnUiController.getInstance().inGamePlayerList) {
            if (player.getId().equals(playerId)) {
                for (BotUser botUser : botUsers) {
                    if (botUser.getBotUserName().equals(player.getName())) {
                        botUser.doAiTurn();
                    }
                }
            }
        }

    }

    public static void deactivateBotUser(int number) {
        botUsers.get(number).deactivate();
        botUsers.remove(number);
    }

    //TODO: fix javadoc
    /**
     * Only for checkstyle.
     *
     */
    public static BotUser getBotUser(int number) {
        if (number >= botUsers.size()) {
            return null;
        } else {
            return botUsers.get(number);
        }
    }
}
