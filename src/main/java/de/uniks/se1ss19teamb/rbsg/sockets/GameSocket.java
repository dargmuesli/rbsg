package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGameGame;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sound.SoundManager;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import de.uniks.se1ss19teamb.rbsg.ui.GameLobbyController;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import de.uniks.se1ss19teamb.rbsg.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameSocket extends AbstractMessageWebSocket {

    private static final Logger logger = LogManager.getLogger();

    private List<GameSocketMessageHandler.GameSocketGameRemoveObject> handlersRemoveObject =
        new ArrayList<>();
    private List<GameSocketMessageHandler.GameSocketGameChangeObject> handlersChangeObject =
        new ArrayList<>();

    public static GameSocket instance;
    private static String gameId;
    private static String armyId;
    private static boolean spectator;
    private static boolean firstGameInitObjectReceived;
    private static List<ChatMessageHandler> handlersChat = new ArrayList<>();
    private boolean ignoreOwn = false;

    public GameSocket(String gameId) {
        this(gameId, null, false);
    }

    public GameSocket(String gameId, String armyId, boolean spectator) {
        GameSocket.gameId = gameId;
        GameSocket.armyId = armyId;
        GameSocket.spectator = spectator;

        registerWebSocketHandler((response) -> {
            if (response.get("msg") != null) {
                NotificationHandler.getInstance()
                    .sendWarning(response.get("msg").getAsString(), LogManager.getLogger());
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
                    if (StringUtil.checkHasNot(data, "message", logger)) {
                        return;
                    }

                    String message = data.get("message").getAsString();

                    switch (message) {
                        case "You have no army with the given id.":
                            NotificationHandler.getInstance().sendError(message, logger);
                            break;
                        case "Initialize game, sending start situation...":
                            firstGameInitObjectReceived = false;
                            NotificationHandler.getInstance().sendInfo("Game initializes.", logger);
                            break;
                        case "You already joined a game.":
                            NotificationHandler.getInstance().sendWarning(message, logger);
                            break;
                        default:
                            NotificationHandler.getInstance()
                                .sendWarning("Unknown message \"" + message + "\"", logger);
                    }

                    break;
                case "gameInitObject":
                    if (!firstGameInitObjectReceived) {
                        firstGameInitObjectReceived = true;

                        InGameGame inGameGame = SerializeUtils.deserialize(data.toString(), InGameGame.class);

                        InGameController.inGameObjects.clear();
                        InGameController.inGameObjects.put(inGameGame.getId(), inGameGame);
                    } else {
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
                                    SerializeUtils.deserialize(data.toString(), EnvironmentTile.class);
                                InGameController.environmentTiles.put(new Pair<>(
                                    environmentTile.getX(), environmentTile.getY()), environmentTile);
                                break;
                            case "Player":
                                InGamePlayer inGamePlayer =
                                    SerializeUtils.deserialize(data.toString(), InGamePlayer.class);
                                InGameController.inGameObjects.put(inGamePlayer.getId(), inGamePlayer);
                                break;
                            case "Unit":
                                InGameController.unitTiles.add(
                                    SerializeUtils.deserialize(data.toString(), UnitTile.class));
                                break;
                            default:
                                NotificationHandler.getInstance().sendWarning(
                                    "Unknown tile type: " + type, logger);
                        }
                    }
                    break;
                case "gameInitFinished":
                    NotificationHandler.getInstance().sendInfo("Game initialized.", logger);
                    GameLobbyController.instance.updatePlayers();

                    Platform.runLater(() -> GameLobbyController.instance.vbxMinimap.getChildren()
                        .add(TextureManager.computeMinimap(
                            InGameController.environmentTiles, -1, InGameController.unitTileMapByTileId)));
                    break;
                case "gameNewObject":
                    if (StringUtil.checkHasNot(data, "id", logger)) {
                        return;
                    }

                    switch (data.get("id").getAsString().replaceFirst("@.+", "")) {
                        case "Player":
                            InGamePlayer inGamePlayer = SerializeUtils.deserialize(data.toString(), InGamePlayer.class);

                            InGameController.inGameObjects.put(inGamePlayer.getId(), inGamePlayer);

                            if (GameLobbyController.instance != null) {
                                GameLobbyController.instance.updatePlayers();
                            }

                            // TODO handle in chat window
                            NotificationHandler.getInstance().sendInfo("New Player joined! \""
                                + inGamePlayer.getName() + " (" + inGamePlayer.getColor() + ")"
                                + "\"", logger);
                            break;
                        case "Unit":
                            InGameController.unitTiles.add(
                                SerializeUtils.deserialize(data.toString(), UnitTile.class));
                            break;
                        default:
                            NotificationHandler.getInstance().sendError(
                                "Unknown new game object id: " + data.get("id").getAsString(), logger);
                    }
                    break;
                case "gameChangeObject":
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

                                NotificationHandler.getInstance().sendInfo(readyMessage.toString(), logger);
                            }
                            break;
                        case "Game":
                            if (!InGameController.gameInitFinished
                                && data.get("fieldName").getAsString().equals("phase")) {
                                InGameController.gameInitFinished = true;
                                if (GameLobbyController.instance != null) {
                                    GameLobbyController.instance.startGameTransition();
                                }
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
                                    NotificationHandler.getInstance().sendError(
                                        "Unknown fieldName object: " + fieldName, logger);
                            }
                            break;
                        default:
                            NotificationHandler.getInstance().sendError(
                                "Unknown changed game object id: " + data.get("id").getAsString(), logger);
                    }

                    for (GameSocketMessageHandler.GameSocketGameChangeObject handler : handlersChangeObject) {
                        handler.handle(type);
                    }
                    break;
                case "gameRemoveObject":
                    if (StringUtil.checkHasNot(data, "id", logger)) {
                        return;
                    }

                    type = data.get("id").getAsString().replaceFirst("@.+", "");

                    switch (type) {
                        case "Player":
                            InGameController.inGameObjects.remove(data.get("id"));

                            if (GameLobbyController.instance != null) {
                                GameLobbyController.instance.updatePlayers();
                            }

                            // TODO handle in chat window
                            NotificationHandler.getInstance()
                                .sendInfo(data.get("id").getAsString().replaceFirst("@.+", "")
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
                            NotificationHandler.getInstance().sendError(
                                "Unknown game object id: " + data.get("id").getAsString(), logger);
                    }

                    for (GameSocketMessageHandler.GameSocketGameRemoveObject handler : handlersRemoveObject) {
                        handler.handle(type);
                    }
                    break;
                case "gameChat":
                    String from = data.get("from").getAsString();

                    if (this.ignoreOwn && from.equals(LoginController.getUserName())) {
                        return;
                    }

                    String msg = data.get("message").getAsString();
                    boolean isPrivate = data.get("channel").getAsString().equals("private");

                    for (ChatMessageHandler handler : handlersChat) {
                        handler.handle(msg, from, isPrivate);
                    }
                    break;
                case "inGameError":
                    if (response.get("data").getAsString().equals("You need to select an army to be ready.")) {
                        GameLobbyController.instance.denyReadiness();
                    }

                    NotificationHandler.getInstance().sendError("InGameError: "
                        + response.get("data").getAsString(), logger);
                    break;
                default:
                    NotificationHandler.getInstance().sendWarning("Unknown action \"" + action + "\"", logger);
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
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "chat");
        json.addProperty("channel", "all");
        json.addProperty("message", message);
        sendToWebsocket(json);
    }

    @Override
    public void sendPrivateMessage(String message, String target) {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "chat");
        json.addProperty("channel", "private");
        json.addProperty("to", target);
        json.addProperty("message", message);
        sendToWebsocket(json);
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
