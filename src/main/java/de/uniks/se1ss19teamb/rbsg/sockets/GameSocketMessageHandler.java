package de.uniks.se1ss19teamb.rbsg.sockets;

public interface GameSocketMessageHandler {

    interface GameSocketGameRemoveObject {
        void handle(String type);
    }

}
