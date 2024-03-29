package de.uniks.se1ss19teamb.rbsg.bot;

import de.uniks.se1ss19teamb.rbsg.ai.AI;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.request.CreateTemporaryUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocketDistributor;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.ui.modules.BotSelectionController;
import de.uniks.se1ss19teamb.rbsg.ui.modules.TurnUiController;

import java.util.ArrayList;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;


public class BotControl {

    public static ArrayList<BotUser> botUsers = new ArrayList<>();

    private static String gameId;

    public static void setGameId(String id) {
        gameId = id;
    }

    /**
     * Creates a temporary user that is run by an {@link AI}.
     *
     * @param number                 The bot's number that is used for {@link GameSocket} creation.
     * @param difficulty             The bot's difficulty.
     * @param botSelectionController The controller on which the bot will be selected.
     */
    public static void createBotUser(int number, int difficulty, BotSelectionController botSelectionController) {

        BotUser botUser = new BotUser();
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
        LogManager.getLogger().info(loginUserRequest.getData());
        LogManager.getLogger().info("Bot Username: " + botUser.getBotUserName()
            + "\nBot Password: " + botUser.getBotUserPassword());
        // end info

        botUsers.add(number, botUser);
        botSelectionController.setBot(botUser);
        botUser.joinGame();

        if (GameSocketDistributor.getGameSocket(number + 1) == null) {
            GameSocketDistributor.setGameSocket(number + 1, gameId);
        }

        botUser.setGameSocket(GameSocketDistributor.getGameSocket(number + 1));
        Objects.requireNonNull(GameSocketDistributor.getGameSocket(number + 1)).setBotGameSocket();
        botUser.connectGamesocket();
        botUser.instantiateBotAi(difficulty);
        botUser.setReady();
    }

    /**
     * Only for checkstyle.
     * Initializes the Bot.
     *
     * @param inGameControllerInput input
     */
    public static void initializeBotAi(InGameController inGameControllerInput) {
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

    /**
     * Getter for a bot's user.
     *
     * @param number The bot's number.
     * @return The bot's user.
     */
    public static BotUser getBotUser(int number) {
        if (number >= botUsers.size()) {
            return null;
        } else {
            return botUsers.get(number);
        }
    }
}
