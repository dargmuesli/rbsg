package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.InGameMetadata;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.PlayerTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameSocket extends AbstractWebSocket {

    private static final Logger logger = LogManager.getLogger();

    public static GameSocket instance;
    private static String userKey;
    private static String gameId;
    private static String armyId;
    private static boolean spectator;
    private static boolean firstGameInitObjectReceived;
    private static List<ChatMessageHandler> handlersChat = new ArrayList<>();
    private static String userName;
    private boolean ignoreOwn = false;

    public GameSocket(String userName, String userKey, String gameId, String armyId, boolean spectator) {
        GameSocket.userName = userName;
        GameSocket.userKey = userKey;
        GameSocket.gameId = gameId;
        GameSocket.armyId = armyId;
        GameSocket.spectator = spectator;

        registerWebSocketHandler((response) -> {
            if (response.has("action")) {
                String action = response.get("action").getAsString();

                switch (action) {
                    case "info":
                        if (response.has("data")) {
                            JsonObject data = response.getAsJsonObject("data");

                            if (data.has("message")) {
                                String message = data.get("message").getAsString();

                                switch (message) {
                                    case "You have no army with the given id.":
                                        NotificationHandler.getInstance().sendError(message, logger);
                                        break;
                                    case "Initialize game, sending start situation...":
                                        firstGameInitObjectReceived = false;
                                        break;
                                    case "You already joined a game.":
                                        NotificationHandler.getInstance().sendWarning(message, logger);
                                        break;
                                    default:
                                        System.out.println();
                                        NotificationHandler.getInstance()
                                            .sendWarning("Unknown message \"" + message + "\"", logger);
                                }
                            }
                        }

                        break;
                    case "gameInitObject":
                        if (response.has("data")) {
                            JsonObject data = response.getAsJsonObject("data");

                            if (!firstGameInitObjectReceived) {
                                firstGameInitObjectReceived = true;

                                InGameController.inGameMetadata =
                                    SerializeUtils.deserialize(data.toString(), InGameMetadata.class);
                            } else {
                                if (data.has("id")) {
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
                                            InGameController.playerTiles.add(
                                                SerializeUtils.deserialize(data.toString(), PlayerTile.class));
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
                            }
                        }
                        break;
                    case "gameInitFinished":
                        InGameController.gameInitFinished = true;
                        break;
                    case "gameRemoveObject":
                        // TODO
                        break;
                    case "gameChat":
                        if (response.has("data")) {
                            JsonObject data = response.getAsJsonObject("data");
                            if (data.get("msg") != null) {
                                //TODO Handle error in MSG
                                return;
                            }

                            String from = data.get("from").getAsString();
                            if (this.ignoreOwn && from.equals(userName)) {
                                return;
                            }

                            String msg = data.get("message").getAsString();
                            boolean isPrivate = data.get("channel").getAsString().equals("private");
                            for (ChatMessageHandler handler : handlersChat) {
                                handler.handle(msg, from, isPrivate);
                            }
                        }
                        break;
                    case "gameNewObject":
                        if (response.has("data")) {
                            // TODO maybe handler to chat window
                            JsonObject data = response.getAsJsonObject("data");
                            NotificationHandler.getInstance().sendInfo("New Player joined! \""
                                + data.get("name").getAsString() + "(" + data.get("color").getAsString() + ")"
                                + "\"", logger);
                        }
                        break;
                    default:
                        NotificationHandler.getInstance().sendWarning("Unknown action \"" + action + "\"", logger);
                }
            }

            // TODO: receive chat messages
        });
    }

    @Override
    protected String getEndpoint() {
        StringBuilder stringBuilder = new StringBuilder("/game?gameId=")
            .append(gameId);

        // assumption: an armyId is only optional in spectator mode
        if (!spectator) {
            stringBuilder
            .append("&armyId=")
            .append(armyId)
                .append("&spectator=true");
        }

        return stringBuilder.toString();
    }

    @Override
    protected String getUserKey() {
        return userKey;
    }

    public String getUserName() {
        return userName;
    }

    public void registerGameMessageHandler(ChatMessageHandler handler) {
        handlersChat.add(handler);
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
        json.addProperty("unitId", unitId);
        json.addProperty("path", SerializeUtils.serialize(path));
        sendToWebsocket(json);
    }

    public void attackUnit(String unitId, String toAttackId) {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "command");
        json.addProperty("action", "attackUnit");
        json.addProperty("unitId", unitId);
        json.addProperty("toAttackId",  toAttackId);
        sendToWebsocket(json);
    }

    public void nextPhase() {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "command");
        json.addProperty("action", "nextPhase");
        sendToWebsocket(json);
    }

    public void sendMessage(String message) {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "chat");
        json.addProperty("channel", "all");
        json.addProperty("message", message);
        sendToWebsocket(json);
    }

    public void sendPrivateMessage(String message, String target) {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "chat");
        json.addProperty("channel", "private");
        json.addProperty("to", target);
        json.addProperty("message", message);
        sendToWebsocket(json);
    }

}
