package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractWebSocket implements WebSocket {

    private static final String url = "wss://rbsg.uniks.de/ws";
    
    protected List<WebSocketMessageHandler> handlers = new ArrayList<>();
    
    protected WebSocketClient websocket;
    
    protected abstract String getEndpoint();
    
    protected abstract String getUserKey();

    private static final Logger logger = LogManager.getLogger(AbstractWebSocket.class);

    private ErrorHandler errorHandler = new ErrorHandler();

    @Override
    public void connect() {
        if (getUserKey() != null) {
            WebSocket.changeUserKey(getUserKey());
        }
        
        if (websocket == null) {
            try {
                websocket = new WebSocketClient(new URI(url + getEndpoint()), (response) ->  {
                    for (WebSocketMessageHandler handler : handlers) {
                        handler.handle(response);
                    }
                });
            } catch (URISyntaxException e) {
                errorHandler.sendError("Fehler in der Websocket-URI-Syntax");
                logger.error(e);
            }
        } else {
            errorHandler.sendError("Es besteht bereits eine Websocket-Verbindung!");
            throw new IllegalStateException("Cannot connect to an already connected Websocket");
        }
    }

    @Override
    public void disconnect() {
        try {
            websocket.stop();
        } catch (Exception e) {
            errorHandler.sendError("Websocket-Verbindung konnte nicht gestoppt werden!");
            logger.error(e);
        }
        websocket = null;
    }

    @Override
    public void registerWebSocketHandler(WebSocketMessageHandler handler) {
        handlers.add(handler);
    }
    
    protected void sendToWebsocket(JsonObject msg) {
        websocket.sendMessage(msg);
    }
    
}
