package de.uniks.se1ss19teamb.rbsg.ai;

import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;

public class BotUser {

    private String botUserKey;
    private String botUserName;
    private String botUserPassword;
    private int botNumber;

    private Kaiten botAi = null;

    private InGameController inGameController;
    private GameSocket gameSocket;

    public void createBotAi() {
        if (botAi == null) {
            botAi = new Kaiten(botUserKey, gameSocket, inGameController);
        }
    }

    public void setBotUserKey(String botUserKey) {
        this.botUserKey = botUserKey;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotUserPassword(String botUserPassword) {
        this.botUserPassword = botUserPassword;
    }

    public String getBotUserKey() {
        return botUserKey;
    }

    public String getBotUserName() {
        return botUserName;
    }

    public String getBotUserPassword() {
        return botUserPassword;
    }

    public void setBotNumber(int botNumber) {this.botNumber = botNumber;}
}
