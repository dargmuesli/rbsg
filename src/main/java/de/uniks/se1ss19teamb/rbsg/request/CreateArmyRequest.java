package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.Unit;

import java.util.List;

public class CreateArmyRequest extends AbstractRestRequest {

    private String userToken;
    private String armyName;
    private List<Unit> units;

    public CreateArmyRequest(String armyName, List<Unit> units, String userToken) {
        this.userToken = userToken;
        this.units = units;
        this.armyName = armyName;
    }

    @Override
    protected JsonObject buildJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", armyName);
        JsonArray unitArray = new JsonArray();
        for (Unit unit : units) {
            unitArray.add(unit.getId());
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

    public String getArmyID() {
        JsonObject responseData = getResponse().get("data").getAsJsonObject();
        return responseData.get("id").getAsString();
    }
}
