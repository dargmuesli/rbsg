package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public class CreateGameRequest extends AbstractRestRequest {
   
    private String userToken;
    private String gameName;
    private int neededPlayers;
   
    public CreateGameRequest(String gameName, int neededPlayers, String userToken) {
        this.userToken = userToken;
        this.neededPlayers = neededPlayers;
        this.gameName = gameName;
    }
   
    @Override
    protected JsonObject buildJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", gameName);
        json.addProperty("neededPlayer", neededPlayers);
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
    
    //Custom Request Helper
    
    public String getGameId() {
        return (((JsonObject)getResponse().get("data")).get("gameId").getAsString());
    }

}
