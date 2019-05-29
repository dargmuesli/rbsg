package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;

public interface WebSocketMessageHandler {
    public void handle(JsonObject response);
}
