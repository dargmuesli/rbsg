package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;

interface WebSocketMessageHandler {
    void handle(JsonObject response);
}
