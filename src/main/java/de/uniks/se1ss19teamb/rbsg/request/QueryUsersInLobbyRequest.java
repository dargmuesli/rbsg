package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class QueryUsersInLobbyRequest extends AbstractRestRequest {

    private String userToken;

    public QueryUsersInLobbyRequest(String userToken) {
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
        return "/user";
    }

    @Override
    protected String getUserToken() {
        return userToken;
    }

    //Custom Request Helper

    public ArrayList<String> getUsersInLobby() {
        ArrayList<String> usersInLobby = new ArrayList<>();
        for (JsonElement lobby : getResponse().get("data").getAsJsonArray()) {
            usersInLobby.add(lobby.getAsString());
        }
        return usersInLobby;
    }

}
