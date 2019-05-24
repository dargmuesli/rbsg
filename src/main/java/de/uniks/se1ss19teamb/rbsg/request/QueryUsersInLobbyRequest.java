package de.uniks.se1ss19teamb.rbsg.request;

import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class QueryUsersInLobbyRequest extends AbstractRESTRequest {

    private String userToken;
    
    public QueryUsersInLobbyRequest(String userToken) {
        this.userToken = userToken;
    }
    
    @Override
    protected JsonObject buildJson() {
        return null;
    }

    @Override
    protected String getHTTPMethod() {
        return "get";
    }

    @Override
    protected String getEndpoint() {
        return "/user";
    }

    @Override
    protected String getUserToken() {
        return userToken;
    }
    
    //Custom Request Helper
    
    @SuppressWarnings("unchecked")
    public ArrayList<String> getUsersInLobby(){
        ArrayList<String> usersInLobby = new ArrayList<>();
        for(JsonElement lobby: getResponse().get("data").getAsJsonArray()) {
            usersInLobby.add(lobby.getAsString());
        }
        return usersInLobby;
    }

}
