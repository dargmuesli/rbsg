package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public class CreateGameRequest extends AbstractDataRestRequest<String> {

    private String userToken;
    private String gameName;
    private int neededPlayers;
    private long seed;

    public CreateGameRequest(String gameName, int neededPlayers, String userToken) {
        this(gameName, neededPlayers, -1, userToken);
    }

    /**
     * Constructor for a game creation request with individual field values.
     *
     * @param gameName      The game's name.
     * @param neededPlayers The game's count of needed players.
     * @param seed          The game's seed.
     * @param userToken     The token to authenticate with.
     */
    public CreateGameRequest(String gameName, int neededPlayers, long seed, String userToken) {
        this.userToken = userToken;
        this.neededPlayers = neededPlayers;
        this.gameName = gameName;
        this.seed = seed;
    }

    @Override
    protected JsonObject buildJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", gameName);
        json.addProperty("neededPlayer", neededPlayers);

        if (seed != -1) {
            json.addProperty("seed", seed);
        }

        return json;
    }

    @Override
    protected String getHttpMethod() {
        return "post";
    }

    @Override
    protected String getEndpoint() {
        return "/game";
    }

    @Override
    protected String getUserToken() {
        return userToken;
    }

    /**
     * Standard provider for the request's response.
     *
     * @return The new game's id.
     */
    @Override
    public String getData() {
        return (((JsonObject) getResponse().get("data")).get("gameId").getAsString());
    }

    @Override
    public String getErrorMessage() {
        return "Could not create game!";
    }

}
