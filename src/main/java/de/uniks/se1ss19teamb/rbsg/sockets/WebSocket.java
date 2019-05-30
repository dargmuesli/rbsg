package de.uniks.se1ss19teamb.rbsg.sockets;


public interface WebSocket {
    
    void connect();
    
    void disconnect();
    
    void registerWebSocketHandler(WebSocketMessageHandler handler);
    
    static void changeUserKey(String userKey) {
        CustomWebSocketConfigurator.setUserKey(userKey);
    }
    
    static void logout() {
        changeUserKey("");
    }
    
}
