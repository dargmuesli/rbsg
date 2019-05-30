package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.Game;

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
    
    public ArrayList<Game> getGames() {
        ArrayList<Game> games = new ArrayList<Game>();
        
        for (JsonElement g : getResponse().get("data").getAsJsonArray()) {
            Game current = new Game();
            JsonObject game = g.getAsJsonObject();
            current.setId(game.get("id").getAsString());
            current.setName(game.get("name").getAsString());
            current.setJoinedPlayers(game.get("joinedPlayer").getAsLong());
            current.setNeededPlayers(game.get("neededPlayer").getAsLong());
            games.add(current);
        }
        
        return games;
    }

}
