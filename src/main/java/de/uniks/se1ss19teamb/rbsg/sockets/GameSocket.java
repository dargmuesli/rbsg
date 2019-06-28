package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.InGameMetadata;
import de.uniks.se1ss19teamb.rbsg.model.InGameTile;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameSocket extends AbstractWebSocket {

    private String userKey;
    private String gameId;
    private String armyId;

    private static final Logger logger = LogManager.getLogger();
    private static final NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();

    private boolean firstGameInitObjectReceived;

    private List<ChatMessageHandler> handlersChat = new ArrayList<>();

    public GameSocket(String userKey, String gameId, String armyId) {
        this.userKey = userKey;
        this.gameId = gameId;
        this.armyId = armyId;

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
                                        notificationHandler.sendError(message, logger);
                                        break;
                                    case "Initialize game, sending start situation...":
                                        firstGameInitObjectReceived = false;
                                        break;
                                    default:
                                        notificationHandler.sendWarning("Unknown message \"" + message + "\"", logger);
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
                                InGameTile tile = SerializeUtils.deserialize(data.toString(), InGameTile.class);
                            	InGameController.inGameTiles.put(new Pair<>(tile.getX(), tile.getY()), tile);
                            }
                        }
                        break;
                    case "gameInitFinished":
                        InGameController.gameInitFinished = true;
                        break;
                    default:
                        notificationHandler.sendWarning("Unknown action \"" + action + "\"", logger);
                }
            }

            // TODO: receive chat messages
        });
    }

    @Override
    protected String getEndpoint() {
        return "/game?gameId=" + gameId + "&armyId=" + armyId;
    }

    @Override
    protected String getUserKey() {
        return userKey;
    }

    public void registerGameMessageHandler(ChatMessageHandler handler) {
        handlersChat.add(handler);
    }

    public void leaveGame() {
        JsonObject json = new JsonObject();
        json.addProperty("messageType", "command");
        json.addProperty("action", "leaveGame");
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
