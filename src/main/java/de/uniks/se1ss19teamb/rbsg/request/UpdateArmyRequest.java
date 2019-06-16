package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.Army;

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

    public UpdateArmyRequest(Army army, String userToken) {
        this.userToken = userToken;
        armyId = army.getId();
        armyName = army.getName();
        unitIDs = army.getUnits();
    }

    @Override
    protected JsonObject buildJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", armyName);
        JsonArray unitIdArray = new JsonArray();
        for (String unit : unitIDs) {
            unitIdArray.add(unit);
        }
        json.add("units", unitIdArray);
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

    /*
    public Army getNewArmy() {
        JsonObject response = getResponse().get("data").getAsJsonObject();
        Army responseArmy = new Army();
        responseArmy.setName(response.get("name").getAsString());
        responseArmy.setId(response.get("id").getAsString());
        JsonArray unitJArray = response.get("units").getAsJsonArray();
        ArrayList<String> unitArray = new ArrayList<>();
        for (JsonElement e : unitJArray) {
            unitArray.add(e.getAsString());
        }
        responseArmy.setUnits(unitArray);
        return responseArmy;

    }
     */
}
