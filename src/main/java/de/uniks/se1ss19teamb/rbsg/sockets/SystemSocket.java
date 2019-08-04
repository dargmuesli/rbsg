package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class SystemSocket extends AbstractWebSocket {

    public static SystemSocket instance;

    private List<SystemSocketMessageHandler.SystemSocketUserJoinHandler> handlersUserJoin
        = new ArrayList<>();
    private List<SystemSocketMessageHandler.SystemSocketUserLeftHandler> handlersUserLeft
        = new ArrayList<>();
    private List<SystemSocketMessageHandler.SystemSocketGameCreateHandler> handlersGameCreate
        = new ArrayList<>();
    private List<SystemSocketMessageHandler.SystemSocketGameDeleteHandler> handlersGameDelete
        = new ArrayList<>();
    private List<SystemSocketMessageHandler.SystemSocketPlayerJoinedGame> handlersPlayerJoinedGame
        = new ArrayList<>();

    public SystemSocket() {
        registerWebSocketHandler((response) -> {
            String action = response.get("action").getAsString();
            JsonObject data = response.get("data").getAsJsonObject();

            switch (action) {
                case "userJoined":
                    for (SystemSocketMessageHandler.SystemSocketUserJoinHandler handler : handlersUserJoin) {
                        handler.handle(data.get("name").getAsString());
                    }
                    break;
                case "userLeft":
                    for (SystemSocketMessageHandler.SystemSocketUserLeftHandler handler : handlersUserLeft) {
                        handler.handle(data.get("name").getAsString());
                    }
                    break;
                case "gameCreated":
                    for (SystemSocketMessageHandler.SystemSocketGameCreateHandler handler : handlersGameCreate) {
                        handler.handle(data.get("name").getAsString(),
                            data.get("id").getAsString(), data.get("neededPlayer").getAsInt());

                    }
                    break;
                case "gameDeleted":
                    for (SystemSocketMessageHandler.SystemSocketGameDeleteHandler handler : handlersGameDelete) {
                        handler.handle(data.get("id").getAsString());
                    }
                    break;
                case "playerJoinedGame": case "playerLeftGame":
                    for (SystemSocketMessageHandler.SystemSocketPlayerJoinedGame handler : handlersPlayerJoinedGame) {
                        handler.handle(data.get("id").getAsString(), data.get("joinedPlayer").getAsInt());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Illegal action " + action
                        + " recieved on System Socket!");
            }
        });
    }

    @Override
    protected String getEndpoint() {
        return "/system";
    }

    //Custom Helpers

    public void registerUserJoinHandler(SystemSocketMessageHandler
                                            .SystemSocketUserJoinHandler handler) {
        handlersUserJoin.add(handler);
    }

    public void registerUserLeftHandler(SystemSocketMessageHandler
                                            .SystemSocketUserLeftHandler handler) {
        handlersUserLeft.add(handler);
    }

    public void registerGameCreateHandler(SystemSocketMessageHandler
                                              .SystemSocketGameCreateHandler handler) {
        handlersGameCreate.add(handler);
    }

    public void registerGameDeleteHandler(SystemSocketMessageHandler
                                              .SystemSocketGameDeleteHandler handler) {
        handlersGameDelete.add(handler);
    }

    public void registerPlayerJoinedGameHandler(SystemSocketMessageHandler.SystemSocketPlayerJoinedGame handler) {

        handlersPlayerJoinedGame.add(handler);
    }

}
