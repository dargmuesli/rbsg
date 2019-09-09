package de.uniks.se1ss19teamb.rbsg.bot;

import de.uniks.se1ss19teamb.rbsg.ai.AI;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.request.CreateArmyRequest;
import de.uniks.se1ss19teamb.rbsg.request.JoinGameRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.UserKeys;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.ui.modules.ArmyManagerController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

public class BotUser {

    private String botUserKey;
    private String botUserName;
    private String botUserPassword;
    private String botPlayerId;
    private String gameId;

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    private AI botAi = null;

    void setGameSocket(GameSocket gameSocket) {
        this.gameSocket = gameSocket;
    }

    private GameSocket gameSocket;

    // TODO: use the real difficulty
    void instantiateBotAi(int difficulty) {
        if (botAi == null) {
            botAi = AI.instantiateStrategic(difficulty - 1);
            List<String> neededArmy = botAi.requestArmy();
            ArrayList<Unit> units = new ArrayList<>();

            if (neededArmy != null) {
                for (String unitId : neededArmy) {
                    units.add(new Unit(unitId));
                }
            } else {
                Map<String, Unit> availableUnitTypes = ArmyManagerController.availableUnits;

                for (int i = 0; i < 10; i++) {
                    int j = (int) (Math.random() * 6);
                    String unitId;
                    ArrayList<Unit> unitArrayList = new ArrayList<>(availableUnitTypes.values());
                    unitId = unitArrayList.get(j).getId();
                    units.add(new Unit(unitId));
                }
            }

            CreateArmyRequest car = new CreateArmyRequest(botUserName + "'s Army", units, botUserKey);
            car.sendRequest();
            String armyId = car.getData();
            gameSocket.changeArmy(armyId);
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

    void joinGame() {
        JoinGameRequest request = new JoinGameRequest(gameId, botUserKey);
        request.sendRequest();
        NotificationHandler.sendInfo(request.getResponse().toString(), LogManager.getLogger());
    }

    public void startGame() {
        gameSocket.startGame();
    }

    void connectGamesocket() {
        UserKeys.setBotUserKey(botUserKey);
        gameSocket.connect();
        UserKeys.revertUserKey();
    }

    void setReady() {
        gameSocket.readyToPlay();
    }

    void doAiTurn() {
        botAi.doTurn();
    }

    void setBotPlayerId(String id) {
        botPlayerId = id;
    }

    void initializeBotAi(InGameController inGameController) {
        botAi.initialize(botPlayerId, gameSocket, inGameController);
    }

    /**
     * Makes the bot leave the game.
     */
    void deactivate() {
        UserKeys.setBotUserKey(botUserKey);
        gameSocket.leaveGame();
        gameSocket.disconnect();
        UserKeys.revertUserKey();
    }
}
