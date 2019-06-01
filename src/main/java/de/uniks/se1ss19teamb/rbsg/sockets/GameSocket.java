package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class GameSocket extends AbstractWebSocket {

    private String userKey;
    private String gameId;
    
    private List<GameMessageHandler> handlers = new ArrayList<>();
    
    public GameSocket(String gameId, String userKey) {
        this.userKey = userKey;
        this.gameId = gameId;
        registerWebSocketHandler((response) -> {
            for (GameMessageHandler handler : handlers) {
                handler.handle(response);
            }
        });
    }
    
    @Override
    protected String getEndpoint() {
        return "/game?gameId=" + gameId;
    }

    @Override
    protected String getUserKey() {
        return userKey;
    }
    
    //Custom Helpers
    
    public void registerGameMessageHandler(GameMessageHandler handler) {
        handlers.add(handler);
    }
    
    //TODO Send and receive Handlers. Implement once more of this WS is known from Release 2
    
}
