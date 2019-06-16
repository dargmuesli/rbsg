package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class CreateArmyRequest extends AbstractRestRequest {

    private String userToken;
    private String armyName;
    private ArrayList<String> unitIDs;

    public CreateArmyRequest(String armyName, ArrayList<String> unitIDs, String userToken) {
        this.userToken = userToken;
        this.unitIDs = unitIDs;
        this.armyName = armyName;
    }

    @Override
    protected JsonObject buildJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", armyName);
        StringBuilder unitIDBuilder = new StringBuilder();
        for (String id : unitIDs) {
            unitIDBuilder.append("\"" + id + "\" ");
        }
        unitIDBuilder.deleteCharAt(0);
        unitIDBuilder.delete(unitIDBuilder.length() - 2, unitIDBuilder.length());

        json.addProperty("name", unitIDBuilder.toString());
        return json;
    }

    @Override
    protected String getHttpMethod() {
        return "post";
    }

    @Override
    protected String getEndpoint() {
        return "/army";
    }

    @Override
    protected String getUserToken() {
        return userToken;
    }
}
