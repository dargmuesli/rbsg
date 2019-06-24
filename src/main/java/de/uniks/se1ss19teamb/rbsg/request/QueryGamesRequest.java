package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.GameMeta;

import java.util.ArrayList;

public class QueryGamesRequest extends AbstractRestRequest {

    private String userToken;

    public QueryGamesRequest(String userToken) {
        this.userToken = userToken;
    }

    @Override
    protected JsonObject buildJson() {
        return null;
    }

    @Override
    protected String getHttpMethod() {
        return "get";
    }

    @Override
    protected String getEndpoint() {
        return "/game";
    }

    @Override
    protected String getUserToken() {
        return userToken;
    }

    //Custom Request Helper

    public ArrayList<GameMeta> getGames() {
        ArrayList<GameMeta> gameMetas = new ArrayList<GameMeta>();

        for (JsonElement g : getResponse().get("data").getAsJsonArray()) {
            GameMeta current = new GameMeta();
            JsonObject game = g.getAsJsonObject();
            current.setId(game.get("id").getAsString());
            current.setName(game.get("name").getAsString());
            current.setJoinedPlayers(game.get("joinedPlayer").getAsLong());
            current.setNeededPlayers(game.get("neededPlayer").getAsLong());
            gameMetas.add(current);
        }

        return gameMetas;
    }

}
