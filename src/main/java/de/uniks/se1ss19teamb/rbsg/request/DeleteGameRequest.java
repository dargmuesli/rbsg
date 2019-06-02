package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public class DeleteGameRequest extends AbstractRESTRequest{
    
    private String userToken, gameId;
    
    public DeleteGameRequest(String gameId, String userToken) {
        this.userToken = userToken;
        this.gameId = gameId;
    }

    public DeleteGameRequest(String gameId, String userToken, HTTPManager httpManager) {
        this.userToken = userToken;
        this.gameId = gameId;
        this.httpManager = httpManager;
    }
    
    @Override
    protected JsonObject buildJson() {
        return null;
    }

    @Override
    protected String getHTTPMethod() {
        return "delete";
    }

    @Override
    protected String getEndpoint() {
        return "/game/" + gameId;
    }

    @Override
    protected String getUserToken() {
        return userToken;
    }

}
