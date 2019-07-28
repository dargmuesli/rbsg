package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public class DeleteArmyRequest extends AbstractRestRequest {

    private String userToken;
    private String armyId;

    public DeleteArmyRequest(String armyId, String userToken) {
        this.userToken = userToken;
        this.armyId = armyId;
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
        return "/army/" + armyId;
    }

    @Override
    protected String getUserToken() {
        return userToken;
    }

    @Override
    public String getErrorMessage() {
        return "Could not delete army!";
    }
}
