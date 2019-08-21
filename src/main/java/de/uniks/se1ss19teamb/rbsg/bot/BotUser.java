package de.uniks.se1ss19teamb.rbsg.bot;

import de.uniks.se1ss19teamb.rbsg.ai.AI;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.request.JoinGameRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.UserKeys;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;


public class BotUser {

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();

    private String botUserKey;
    private String botUserName;
    private String botUserPassword;
    private String gameId;

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    private int botNumber;

    private AI botAi = null;

    //ingameController always null?
    void setInGameController(InGameController inGameController) {
        this.inGameController = inGameController;
    }

    private InGameController inGameController;

    void setGameSocket(GameSocket gameSocket) {
        this.gameSocket = gameSocket;
    }

    private GameSocket gameSocket;

    // TODO: use the real difficulty
    void instantiateBotAi(int difficulty) {
        if (botAi == null) {
            botAi = AI.instantiateStrategic(Integer.MAX_VALUE);
            List<String> neededArmy = botAi.requestArmy();

            ArrayList<Unit> units = new ArrayList<>();
            if (neededArmy != null) {
                for (String unit : neededArmy) {
                    
                }
            }

            // TODO: Initialize has to be done when the game starts.
            // botAi.initialize(botUserKey, gameSocket, inGameController);

        }
    }

    void setBotUserKey(String botUserKey) {
        this.botUserKey = botUserKey;
    }

    void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    void setBotUserPassword(String botUserPassword) {
        this.botUserPassword = botUserPassword;
    }


    public String getBotUserName() {
        return botUserName;
    }

    String getBotUserPassword() {
        return botUserPassword;
    }

    void setBotNumber(int botNumber) {
        this.botNumber = botNumber;
    }

    void joinGame() {
        JoinGameRequest request = new JoinGameRequest(gameId, botUserKey);
        request.sendRequest();
        NotificationHandler.sendInfo(request.getResponse().toString(), logger);
    }

    void connectGamesocket() {
        UserKeys.setBotUserKey(botUserKey);
        gameSocket.connect();
        UserKeys.revertUserKey();
    }
}
