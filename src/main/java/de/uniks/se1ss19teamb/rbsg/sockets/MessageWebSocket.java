package de.uniks.se1ss19teamb.rbsg.sockets;

public interface MessageWebSocket {
    void registerMessageHandler(ChatMessageHandler handler);

    void sendMessage(String message);

    void sendPrivateMessage(String message, String target);
}
