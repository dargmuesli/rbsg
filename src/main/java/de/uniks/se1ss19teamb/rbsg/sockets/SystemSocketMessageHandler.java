package de.uniks.se1ss19teamb.rbsg.sockets;


public interface SystemSocketMessageHandler {
    interface SystemSocketUserJoinHandler{
        void handle(String name);
    }
    interface SystemSocketUserLeftHandler{
        void handle(String name);
    }
    interface SystemSocketGameCreateHandler{
        void handle(String name, String gameId, int neededPlayers);
    }
    interface SystemSocketGameDeleteHandler{
        void handle(String gameId);
    }
}
