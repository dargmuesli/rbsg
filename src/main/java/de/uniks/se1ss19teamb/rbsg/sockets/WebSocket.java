package de.uniks.se1ss19teamb.rbsg.sockets;


interface WebSocket {
    void connect();

    void disconnect();

    void registerWebSocketHandler(WebSocketMessageHandler handler);
}
