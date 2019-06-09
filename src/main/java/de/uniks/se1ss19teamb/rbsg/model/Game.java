package de.uniks.se1ss19teamb.rbsg.model;


public class Game {

    //Fields defined by Server
    private long joinedPlayers;

    private String name;

    private String id;

    private long neededPlayers;

    public long getJoinedPlayers() {
        return joinedPlayers;
    }

    public void setJoinedPlayers(Long joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getNeededPlayers() {
        return neededPlayers;
    }

    public void setNeededPlayers(long neededPlayers) {
        this.neededPlayers = neededPlayers;
    }

    public String toString() {
        return name;
    }
}
