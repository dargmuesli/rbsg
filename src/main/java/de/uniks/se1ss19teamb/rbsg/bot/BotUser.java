package de.uniks.se1ss19teamb.rbsg.bot;

import de.uniks.se1ss19teamb.rbsg.ai.AI;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.request.CreateArmyRequest;
import de.uniks.se1ss19teamb.rbsg.request.JoinGameRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.UserKeys;
import de.uniks.se1ss19teamb.rbsg.ui.ArmyManagerController;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;


public class BotUser {

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();

    private String botUserKey;
    private String botUserName;
    private String botUserPassword;
    private String gameId;
    private String armyId;

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
                for (String unitId : neededArmy) {
                    units.add(new Unit(unitId));
                }

            } else {
                Map<String, Unit> availableUnitTypes = ArmyManagerController.availableUnits;
                for (int i = 0; i < 10; i++) {
                    int j = (int) (Math.random() * 6);
                    String unitId;
                    switch (j) {
                        case 0:
                            unitId = availableUnitTypes.get("bazooka trooper").getId();
                            break;
                        case 1:
                            unitId = availableUnitTypes.get("jeep").getId();
                            break;
                        case 2:
                            unitId = availableUnitTypes.get("infantry").getId();
                            break;
                        case 3:
                            unitId = availableUnitTypes.get("chopper").getId();
                            break;
                        case 4:
                            unitId = availableUnitTypes.get("light tank").getId();
                            break;
                        default:
                            unitId = availableUnitTypes.get("heavy tank").getId();
                            break;
                    }
                    units.add(new Unit(unitId));
                }
            }
            CreateArmyRequest car = new CreateArmyRequest(botUserName + "'s Army", units, botUserKey);
            car.sendRequest();
            System.out.println(car.getSuccessful());
            armyId = car.getData();
            gameSocket.changeArmy(armyId);

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

    void setReady() {
        gameSocket.readyToPlay();
    }
}
