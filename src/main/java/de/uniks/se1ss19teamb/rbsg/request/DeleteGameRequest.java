package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public class DeleteGameRequest extends AbstractRestRequest {

    private String userToken;
    private String gameId;

    public DeleteGameRequest(String gameId, String userToken) {
        this.userToken = userToken;
        this.gameId = gameId;
    }

    @Override
    protected JsonObject buildJson() {
        return null;
    }

    @Override
    protected String getHttpMethod() {
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

    @Override
    public String getErrorMessage() {
        return "Could not delete game!";
    }

}
