package de.uniks.se1ss19teamb.rbsg.sockets;


public interface SystemSocketMessageHandler {
    interface SystemSocketUserJoinHandler{
        public void handle(String name);
    }
    interface SystemSocketUserLeftHandler{
        public void handle(String name);
    }
    interface SystemSocketGameCreateHandler{
        public void handle(String name, String gameId, int neededPlayers);
    }
    interface SystemSocketGameDeleteHandler{
        public void handle(String gameId);
    }
}
