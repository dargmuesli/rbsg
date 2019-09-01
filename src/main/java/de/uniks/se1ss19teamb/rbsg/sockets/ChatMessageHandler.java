package de.uniks.se1ss19teamb.rbsg.sockets;

public interface ChatMessageHandler {
    void handle(String message, String from, boolean isPrivate, boolean wasEncrypted);
}
