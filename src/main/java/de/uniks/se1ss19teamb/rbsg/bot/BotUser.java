package de.uniks.se1ss19teamb.rbsg.bot;

import de.uniks.se1ss19teamb.rbsg.ai.AI;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;

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

    void instantiateBotAi() {
        if (botAi == null) {
            botAi = AI.instantiate(botUserKey, gameSocket, inGameController, -1);
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
}
