package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public class JoinGameRequest extends AbstractRestRequest {

    private String userToken;
    private String gameId;

    public JoinGameRequest(String gameId, String userToken) {
        this.userToken = userToken;
        this.gameId = gameId;
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
        return "/game/" + gameId;
    }

    @Override
    protected String getUserToken() {
        return userToken;
    }

}
