package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
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
        JsonArray unitArray = new JsonArray();
        for (String id : unitIDs) {
            unitArray.add(id);
        }
        json.add("units", unitArray);


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
