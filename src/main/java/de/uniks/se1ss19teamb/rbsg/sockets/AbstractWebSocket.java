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

    public WebSocketClient websocket;

    List<WebSocketMessageHandler> handlers = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger();
    private static final String url = "wss://rbsg.uniks.de/ws";

    protected abstract String getEndpoint();

    @Override
    public void connect() {
        if (websocket == null || websocket.mySession == null) {
            try {
                websocket = new WebSocketClient(new URI(url + getEndpoint()), (response) -> {
                    for (WebSocketMessageHandler handler : handlers) {
                        handler.handle(response);
                    }
                });
            } catch (URISyntaxException e) {
                NotificationHandler.sendError("The websocket uri syntax is incorrect!", logger, e);
            }
        } else {
            NotificationHandler.sendInfo("A websocket connection already exists!", logger);
        }
    }

    @Override
    public void disconnect() {
        try {
            websocket.stop();
        } catch (Exception e) {
            NotificationHandler.sendError("The websocket connection couldn't be stopped!", logger, e);
        }

        websocket = null;
    }

    @Override
    public void registerWebSocketHandler(WebSocketMessageHandler handler) {
        handlers.add(handler);
    }

    public void sendToWebsocket(JsonObject msg) {
        websocket.sendMessage(msg);
    }

}
