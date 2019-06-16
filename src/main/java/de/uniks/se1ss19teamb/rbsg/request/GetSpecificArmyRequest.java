package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public class GetSpecificArmyRequest extends AbstractRestRequest {

    private String userToken;
    private String armyId;

    public GetSpecificArmyRequest(String armyId, String userToken) {
        this.userToken = userToken;
        this.armyId = armyId;
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
        return "/army/" + armyId;
    }

    @Override
    protected String getUserToken() {
        return userToken;
    }
}
