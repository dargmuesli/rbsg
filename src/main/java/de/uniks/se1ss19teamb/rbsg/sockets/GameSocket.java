package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.chat.Chat;
import de.uniks.se1ss19teamb.rbsg.crypto.CipherUtils;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGameGame;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sound.SoundManager;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import de.uniks.se1ss19teamb.rbsg.ui.GameLobbyController;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.ui.TurnUiController;
import de.uniks.se1ss19teamb.rbsg.ui.WinScreenController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtil;
import de.uniks.se1ss19teamb.rbsg.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameSocket extends AbstractMessageWebSocket {

    private final Logger logger = LogManager.getLogger();

    private List<GameSocketMessageHandler.GameSocketGameRemoveObject> handlersRemoveObject =
        new ArrayList<>();
    private List<GameSocketMessageHandler.GameSocketGameChangeObject> handlersChangeObject =
        new ArrayList<>();

    public GameSocket instance;
    private String gameId;
    private String armyId;
    private boolean spectator;
    private boolean firstGameInitObjectReceived;
    private List<ChatMessageHandler> handlersChat = new ArrayList<>();
    private boolean ignoreOwn = false;
    public String currentPlayer;
    public String phaseString;

    public GameSocket(String gameId) {
        this(gameId, null, false);
    }

    public GameSocket(String gameId, String armyId, boolean spectator) {
        this.gameId = gameId;
        this.armyId = armyId;
        this.spectator = spectator;

        registerWebSocketHandler((response) -> {
            if (response.get("msg") != null) {
                NotificationHandler.sendWarning(response.get("msg").getAsString(), LogManager.getLogger());
                return;
            } else if (StringUtil.checkHasNot(response, "action", logger)) {
                return;
            }
            
            String action = response.get("action").getAsString();
            JsonObject data = null;

            if (action.equals("gameStarts")) {
                logger.info("The game is starting!");
                return;
            } else if (!action.equals("gameInitFinished")) {
                if (StringUtil.checkHasNot(response, "data", logger)) {
                    return;
                }

                if (!response.get("data").isJsonPrimitive()) {
                    data = response.getAsJsonObject("data");
                }
            }

            switch (action) {
                case "info":
                    assert data != null;
                    if (StringUtil.checkHasNot(data, "message", logger)) {
                        return;
                    }

                    String message = data.get("message").getAsString();

                    switch (message) {
                        case "You have no army with the given id.":
                            NotificationHandler.sendError(message, logger);
                            break;
                        case "Initialize game, sending start situation...":
                            firstGameInitObjectReceived = false;
                            NotificationHandler.sendInfo("Game initializes.", logger);
                            break;
                        case "You already joined a game.":
                            NotificationHandler.sendWarning(message, logger);
                            break;
                        default:
                            NotificationHandler.sendWarning("Unknown message \"" + message + "\"", logger);
                    }

                    break;
                case "gameInitObject":
                    if (!firstGameInitObjectReceived) {
                        firstGameInitObjectReceived = true;

                        assert data != null;
                        InGameGame inGameGame = SerializeUtil.deserialize(data.toString(), InGameGame.class);

                        InGameController.inGameObjects.clear();
                        InGameController.inGameObjects.put(inGameGame.getId(), inGameGame);
                    } else {
                        assert data != null;
                        if (StringUtil.checkHasNot(data, "id", logger)) {
                            return;
                        }

                        String type = data.get("id").getAsString().replaceFirst("@.+", "");

                        switch (type) {
                            case "Forest":
                            case "Sand":
                            case "Grass":
                            case "Water":
                            case "Mountain":
                                EnvironmentTile environmentTile =
                                    SerializeUtil.deserialize(data.toString(), EnvironmentTile.class);
                                InGameController.environmentTiles.put(new Pair<>(
                                    environmentTile.getX(), environmentTile.getY()), environmentTile);
                                break;
                            case "Player":
                                InGamePlayer inGamePlayer =
                                    SerializeUtil.deserialize(data.toString(), InGamePlayer.class);
                                InGameController.inGameObjects.put(inGamePlayer.getId(), inGamePlayer);
                                break;
                            case "Unit":
                                //TODO does this case exist?
                                InGameController.unitTiles.add(
                                    SerializeUtil.deserialize(data.toString(), UnitTile.class));
                                break;
                            default:
                                NotificationHandler.sendWarning(
                                    "Unknown tile type: " + type, logger);
                        }
                    }
                    break;
                case "gameInitFinished":

                    NotificationHandler.sendInfo("Game initialized.", logger);
                    if (GameLobbyController.instance != null) {
                        GameLobbyController.instance.updatePlayers();
                    }

                    Platform.runLater(() -> GameLobbyController.instance.vbxMinimap.getChildren()
                        .add(TextureManager.computeMinimap(
                            InGameController.environmentTiles, -1, InGameController.unitTileMapByTileId)));

                    break;
                case "gameNewObject":
                    assert data != null;
                    if (StringUtil.checkHasNot(data, "id", logger)) {
                        return;
                    }

                    switch (data.get("id").getAsString().replaceFirst("@.+", "")) {
                        case "Player":
                            InGamePlayer inGamePlayer = SerializeUtil.deserialize(data.toString(), InGamePlayer.class);

                            InGameController.inGameObjects.put(inGamePlayer.getId(), inGamePlayer);

                            if (GameLobbyController.instance != null) {
                                GameLobbyController.instance.updatePlayers();
                            }

                            // TODO handle in chat window
                            NotificationHandler.sendInfo("New Player joined! \""
                                + inGamePlayer.getName() + " (" + inGamePlayer.getColor() + ")"
                                + "\"", logger);
                            break;
                        case "Unit":
                            InGameController.unitTiles.add(
                                SerializeUtil.deserialize(data.toString(), UnitTile.class));
                            break;
                        default:
                            NotificationHandler.sendError(
                                "Unknown new game object id: " + data.get("id").getAsString(), logger);
                    }
                    break;
                case "gameChangeObject":
                    assert data != null;
                    if (StringUtil.checkHasNot(data, "id", logger)) {
                        return;
                    }

                    String newValue;
                    String fieldName;
                    String type = data.get("id").getAsString().replaceFirst("@.+", "");

                    switch (type) {
                        case "Player":
                            InGamePlayer inGamePlayer =
                                (InGamePlayer) InGameController.inGameObjects.get(data.get("id").getAsString());

                            if (StringUtil.checkHasNot(data, "fieldName", logger)) {
                                return;
                            }

                            fieldName = data.get("fieldName").getAsString();

                            if (StringUtil.checkHasNot(data, "newValue", logger)) {
                                return;
                            }

                            newValue = data.get("newValue").getAsString();

                            if (fieldName.equals("isReady")) {
                                boolean ready = Boolean.parseBoolean(newValue);

                                if (inGamePlayer != null) {
                                    inGamePlayer.setReady(ready);

                                    if (inGamePlayer.getName().equals(LoginController.getUserName()) && ready) {
                                        GameLobbyController.instance.confirmReadiness();
                                    }
                                }

                                if (GameLobbyController.instance != null) {
                                    GameLobbyController.instance.updatePlayers();
                                }
                            }

                            if (inGamePlayer != null) {
                                StringBuilder readyMessage = new StringBuilder("Player \"")
                                    .append(inGamePlayer.getName())
                                    .append(" (")
                                    .append(inGamePlayer.getColor())
                                    .append(") is ");

                                if (inGamePlayer.isReady()) {
                                    // TODO maybe handler to chat window
                                    readyMessage.append("now ready.");
                                } else {
                                    readyMessage.append("not ready.");
                                }

                                NotificationHandler.sendInfo(readyMessage.toString(), logger);
                            }
                            break;
                        case "Game":
                            fieldName = data.get("fieldName").getAsString();

                            switch (fieldName) {
                                case "currentPlayer":
                                    InGameController.movedUnitTiles.clear();

                                    currentPlayer = data.get("newValue").getAsString();

                                    if (TurnUiController.getInstance() == null) {
                                        TurnUiController.startShowTurn = data.get("newValue").getAsString();
                                    } else {
                                        TurnUiController.getInstance().showTurn(data.get("newValue").getAsString());
                                    }
                                    break;
                                case "phase":
                                    if (!GameLobbyController.instance.gameInitFinished) {
                                        GameLobbyController.instance.gameInitFinished = true;
                                        GameLobbyController.instance.startGameTransition();
                                    }

                                    switch (data.get("newValue").getAsString()) {
                                        case "movePhase":
                                            phaseString = "Movement Phase";
                                            if (InGameController.getInstance() != null) {
                                                InGameController.instance.autoMode();
                                            }
                                            break;
                                        case "attackPhase":
                                            phaseString = "Attack Phase";
                                            break;
                                        case "lastMovePhase":
                                            phaseString = "Last Movement Phase";
                                            break;
                                        default:
                                            phaseString = "Unknown phase!";
                                    }

                                    if (TurnUiController.getInstance() == null) {
                                        TurnUiController.startTurnLabel = phaseString;
                                    } else {
                                        TurnUiController.getInstance().setTurnLabel(phaseString);
                                    }

                                    break;
                                case "winner":
                                    WinScreenController.getInstance().setWinningScreen(data.get("newValue")
                                            .getAsString());
                                    break;
                                default:
                                    logger.error("Unknown field name \"" + fieldName + "\"");
                            }
                            break;
                        case "Unit":
                            if (StringUtil.checkHasNot(data, "fieldName", logger)) {
                                return;
                            }

                            fieldName = data.get("fieldName").getAsString();

                            if (StringUtil.checkHasNot(data, "newValue", logger)) {
                                return;
                            }

                            newValue = data.get("newValue").getAsString();
                            String id = data.get("id").getAsString();

                            switch (fieldName) {
                                case "position":
                                    if (InGameController.getInstance() != null) {
                                        InGameController.getInstance().changeUnitPos(id, newValue);
                                    }
                                    break;
                                case "hp":
                                    //SoundManager.playSound("Omae", 0);
                                    if (InGameController.getInstance() != null) {
                                        InGameController.getInstance().changeUnitHp(id, newValue);
                                    }
                                    break;
                                default:
                                    NotificationHandler.sendError(
                                        "Unknown fieldName object: " + fieldName, logger);
                            }
                            break;
                        default:
                            NotificationHandler.sendError(
                                "Unknown changed game object id: " + data.get("id").getAsString(), logger);
                    }

                    for (GameSocketMessageHandler.GameSocketGameChangeObject handler : handlersChangeObject) {
                        handler.handle(type);
                    }
                    break;
                case "gameRemoveObject":
                    assert data != null;
                    if (StringUtil.checkHasNot(data, "id", logger)) {
                        return;
                    }

                    type = data.get("id").getAsString().replaceFirst("@.+", "");

                    switch (type) {
                        case "Player":
                            InGameController.inGameObjects.remove(data.get("id").getAsString());

                            if (GameLobbyController.instance != null) {
                                GameLobbyController.instance.updatePlayers();
                            }

                            // TODO handle in chat window
                            NotificationHandler.sendInfo(data.get("id").getAsString().replaceFirst("@.+", "")
                                    + " has left the game!", logger);
                            SoundManager.playSound("Omae_nani", 0);
                            break;
                        case "Unit":
                            for (int i = 0; i < InGameController.unitTiles.size(); i++) {
                                if (InGameController.unitTiles.get(i).getId().equals(data.get("id").getAsString())) {
                                    UnitTile attacker = InGameController.getInstance()
                                        .findAttackingUnit(InGameController.unitTiles.get(i));

                                    if (attacker != null) {
                                        SoundManager.playSound(attacker.getType().replaceAll(" ", ""), 0);
                                    }

                                    InGameController.getInstance().changeUnitPos(data.get("id").getAsString(), null);
                                    InGameController.unitTiles.remove(i);
                                }
                            }

                            SoundManager.playSound("nani", 0);
                            break;
                        default:
                            NotificationHandler.sendError(
                                "Unknown game object id: " + data.get("id").getAsString(), logger);
                    }

                    for (GameSocketMessageHandler.GameSocketGameRemoveObject handler : handlersRemoveObject) {
                        handler.handle(type);
                    }
                    break;
                case "gameChat":
                    Chat.defaultMessageHandler(response.getAsJsonObject("data"), this.ignoreOwn, LoginController.getUserName(), handlersChat);
                    break;
                case "inGameError":
                    if (response.get("data").getAsString().equals("You need to select an army to be ready.")) {
                        GameLobbyController.instance.denyReadiness();
                    }

                    NotificationHandler.sendError("InGameError: "
                        + response.get("data").getAsString(), logger);
                    break;
                default:
                    NotificationHandler.sendWarning("Unknown action \"" + action + "\"", logger);
            }

            // TODO: receive chat messages
        });
    }

    @Override
    protected String getEndpoint() {
        StringBuilder stringBuilder = new StringBuilder("/game?gameId=")
            .append(gameId);

        // assumption: an armyId is only optional in spectator mode
        if (spectator) {
            stringBuilder.append("&spectator=true");
        } else {
            stringBuilder
                .append("&armyId=")
                .append(armyId)
                .append("&spectator=false");
        }

        return stringBuilder.toString();
    }

    public void changeArmy(String armyId) {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "command");
        json.addProperty("action", "changeArmy");
        json.addProperty("data", armyId);
        sendToWebsocket(json);
    }

    public void leaveGame() {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "command");
        json.addProperty("action", "leaveGame");
        sendToWebsocket(json);
    }

    public void readyToPlay() {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "command");
        json.addProperty("action", "readyToPlay");
        sendToWebsocket(json);
    }

    public void startGame() {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "command");
        json.addProperty("action", "startGame");
        sendToWebsocket(json);
    }

    public void moveUnit(String unitId, String[] path) {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "command");
        json.addProperty("action", "moveUnit");
        JsonObject data = new JsonObject();
        data.addProperty("unitId", unitId);
        JsonArray jpath = new JsonArray();

        for (String p : path) {
            jpath.add(p);
        }

        data.add("path", jpath);
        json.add("data", data);
        sendToWebsocket(json);
    }

    public void attackUnit(String unitId, String toAttackId) {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "command");
        json.addProperty("action", "attackUnit");
        JsonObject data = new JsonObject();
        data.addProperty("unitId", unitId);
        data.addProperty("toAttackId", toAttackId);
        json.add("data", data);
        sendToWebsocket(json);
    }

    public void nextPhase() {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "command");
        json.addProperty("action", "nextPhase");
        sendToWebsocket(json);
    }

    @Override
    public void registerMessageHandler(ChatMessageHandler handler) {
        handlersChat.add(handler);
    }

    @Override
    public void sendMessage(String message) {
        Chat.executeCommandsOnMessage(message, null).ifPresent(s -> {
            JsonObject json = new JsonObject();
            json.addProperty("messageType", "chat");
            json.addProperty("channel", "all");
            json.addProperty("message", s);
            sendToWebsocket(json);
        });
    }

    @Override
    public void sendPrivateMessage(String message, String target) {
        Chat.executeCommandsOnMessage(message, target).ifPresent(s -> {
            JsonObject json = new JsonObject();
            json.addProperty("messageType", "chat");
            json.addProperty("channel", "private");
            json.addProperty("to", target);
            json.addProperty("message", s);
            sendToWebsocket(json);
        });
    }

    //Custom Helpers

    public void registerGameRemoveObject(GameSocketMessageHandler
                                             .GameSocketGameRemoveObject handler) {
        handlersRemoveObject.add(handler);
    }

    public void registerGameChangeObject(GameSocketMessageHandler
                                             .GameSocketGameChangeObject handler) {
        handlersChangeObject.add(handler);
    }
}
