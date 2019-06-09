package de.uniks.se1ss19teamb.rbsg.sockets;


interface WebSocket {

    static void changeUserKey(String userKey) {
        CustomWebSocketConfigurator.setUserKey(userKey);
    }

    static void logout() {
        changeUserKey("");
    }

    void connect();

    void disconnect();

    void registerWebSocketHandler(WebSocketMessageHandler handler);

}
