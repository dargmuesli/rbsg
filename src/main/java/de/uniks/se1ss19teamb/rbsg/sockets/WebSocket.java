package de.uniks.se1ss19teamb.rbsg.sockets;


public interface WebSocket {
    
    public void connect();
    
    public void disconnect();
    
    public void registerWebSocketHandler(WebSocketMessageHandler handler);
    
    public static void changeUserKey(String userKey) {
        CustomWebSocketConfigurator.setUserKey(userKey);
    }
    
    public static void logout() {
        changeUserKey("");
    }
    
}
