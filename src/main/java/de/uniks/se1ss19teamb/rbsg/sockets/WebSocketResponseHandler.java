package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;

public interface WebSocketResponseHandler {
    public void handle(JsonObject response);
}
