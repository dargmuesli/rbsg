package de.uniks.se1ss19teamb.rbsg.sockets;

import java.util.ArrayList;
import java.util.List;

public class GameSocket extends AbstractWebSocket {

    private String userKey;
    private String gameId;

    private List<GameMessageHandler> handlersGame = new ArrayList<>();

    public GameSocket(String gameId, String userKey) {
        this.userKey = userKey;
        this.gameId = gameId;
        registerWebSocketHandler((response) -> {
            for (GameMessageHandler handler : handlersGame) {
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
        handlersGame.add(handler);
    }

    //TODO Send and receive Handlers. Implement once more of this WS is known from Release 2

}
