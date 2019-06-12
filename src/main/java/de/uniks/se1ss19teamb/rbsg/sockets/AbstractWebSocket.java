package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractWebSocket implements WebSocket {

    private static final String url = "wss://rbsg.uniks.de/ws";
    private static final Logger logger = LogManager.getLogger();
    List<WebSocketMessageHandler> handlers = new ArrayList<>();
    WebSocketClient websocket;
    private NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();

    protected abstract String getEndpoint();

    protected abstract String getUserKey();

    @Override
    public void connect() {
        if (getUserKey() != null) {
            WebSocket.changeUserKey(getUserKey());
        }

        if (websocket == null) {
            try {
                websocket = new WebSocketClient(new URI(url + getEndpoint()), (response) -> {
                    for (WebSocketMessageHandler handler : handlers) {
                        handler.handle(response);
                    }
                });
            } catch (URISyntaxException e) {
                notificationHandler.sendError("Fehler in der Websocket-URI-Syntax", logger, e);
            }
        } else {
            notificationHandler.sendError("Es besteht bereits eine Websocket-Verbindung!", logger,
                new IllegalStateException("Cannot connect to an already connected Websocket"));
        }
    }

    @Override
    public void disconnect() {
        try {
            websocket.stop();
        } catch (Exception e) {
            notificationHandler.sendError("Websocket-Verbindung konnte nicht gestoppt werden!", logger, e);
        }
        websocket = null;
    }

    @Override
    public void registerWebSocketHandler(WebSocketMessageHandler handler) {
        handlers.add(handler);
    }

    void sendToWebsocket(JsonObject msg) {
        websocket.sendMessage(msg);
    }

}
