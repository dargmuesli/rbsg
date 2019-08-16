package de.uniks.se1ss19teamb.rbsg.bot;

import de.uniks.se1ss19teamb.rbsg.ai.AI;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;

import java.util.List;

public class BotUser {

    private String botUserKey;
    private String botUserName;
    private String botUserPassword;

    private int botNumber;

    private AI botAi = null;

    void setInGameController(InGameController inGameController) {
        this.inGameController = inGameController;
    }

    private InGameController inGameController;

    void setGameSocket(GameSocket gameSocket) {
        this.gameSocket = gameSocket;
    }

    private GameSocket gameSocket;

    void instantiateBotAi(int difficulty) {
        if (botAi == null) {
            botAi = AI.instantiate(-1);
            botAi.initialize(botUserKey, gameSocket, inGameController);
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

    private void setupArmy() {
        List<String> neededArmy = botAi.requestArmy();

    }
}
