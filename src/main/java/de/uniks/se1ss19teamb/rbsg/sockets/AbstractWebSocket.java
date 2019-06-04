package de.uniks.se1ss19teamb.rbsg.sockets;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

public abstract class AbstractWebSocket implements WebSocket {

    private static final String url = "wss://rbsg.uniks.de/ws";
    
    protected List<WebSocketMessageHandler> handlers = new ArrayList<>();
    
    protected WebSocketClient websocket;
    
    protected abstract String getEndpoint();
    
    protected abstract String getUserKey();
    
    @Override
    public void connect() {
        if(getUserKey() != null) {
            WebSocket.changeUserKey(getUserKey());
        }
        
        if(websocket == null) {
            try {
                websocket = new WebSocketClient(new URI(url + getEndpoint()), (response) ->  {
                    for(WebSocketMessageHandler handler : handlers)
                        handler.handle(response);
                });
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        else {
            throw new IllegalStateException("Cannot connect to an already connected Websocket");
        }
    }

    @Override
    public void disconnect() {
        try {
            websocket.stop();
        } catch (Exception e) {
            e.printStackTrace();
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
