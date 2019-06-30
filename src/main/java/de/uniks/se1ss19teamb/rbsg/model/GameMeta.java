package de.uniks.se1ss19teamb.rbsg.model;

/**
 * A model for games as used in the lobby.
 */
public class GameMeta {

    //Fields defined by Server
    private long joinedPlayers;

    private String name;

    private String id;

    private long neededPlayers;

    /**
     * Standard getter.
     *
     * @return the amount of players who joined the game.
     */
    public long getJoinedPlayers() {
        return joinedPlayers;
    }

    /**
     * Standard setter.
     *
     * @param joinedPlayers the amount of players who joined the game.
     */
    public void setJoinedPlayers(Long joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
    }

    /**
     * Standard getter.
     *
     * @return the game's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Standard setter.
     *
     * @param name the game's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Standard getter.
     *
     * @return the game's id.
     */
    public String getId() {
        return id;
    }

    /**
     * Standard setter.
     *
     * @param id the game's id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Standard getter.
     *
     * @return the amount of players who need to be joined for the game to start.
     */
    public long getNeededPlayers() {
        return neededPlayers;
    }

    /**
     * Standard setter.
     *
     * @param neededPlayers the amount of players who need to be joined for the game to start.
     */
    public void setNeededPlayers(long neededPlayers) {
        this.neededPlayers = neededPlayers;
    }

    /**
     * Overrides default {@link Object#toString}.
     *
     * @return the same value as {@link #getName}.
     */
    public String toString() {
        return name;
    }
}
