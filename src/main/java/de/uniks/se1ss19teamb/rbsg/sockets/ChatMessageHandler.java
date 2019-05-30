package de.uniks.se1ss19teamb.rbsg.sockets;

public interface ChatMessageHandler {
    public void handle(String message, String from, boolean isPrivate);
}
