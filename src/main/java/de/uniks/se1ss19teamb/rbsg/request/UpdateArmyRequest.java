package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class UpdateArmyRequest extends AbstractRestRequest {

    private String userToken;
    private String armyId;
    private String armyName;
    private ArrayList<String> unitIDs;

    public UpdateArmyRequest(String armyId, String armyName, ArrayList<String> unitIDs, String userToken) {
        this.userToken = userToken;
        this.armyId = armyId;
        this.armyName = armyName;
        this.unitIDs = unitIDs;
    }

    @Override
    protected JsonObject buildJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", armyName);
        for (String unit : unitIDs) {
            json.addProperty("units", unit);
        }
        return json;
    }

    @Override
    protected String getHttpMethod() {
        return "put";
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
