package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.Army;

import java.util.ArrayList;

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

    public Army getRequestedArmy() {
        JsonObject data = getResponse().get("data").getAsJsonObject();
        String armyID = data.get("id").getAsString();
        String name = data.get("name").getAsString();
        JsonArray units = data.get("units").getAsJsonArray();
        ArrayList<String> unitList = new ArrayList<>();

        for (JsonElement e : units) {
            unitList.add(e.getAsString());
        }

        return new Army(armyID, name, unitList);
    }
}
