package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.ui.ArmyManagerController;

import java.util.ArrayList;
import java.util.List;

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
        JsonArray unitIds = data.get("units").getAsJsonArray();
        List<Unit> unitList = new ArrayList<>();

        for (JsonElement unitId : unitIds) {
            unitList.add(ArmyManagerController.availableUnits.get(unitId.getAsString()));
        }

        return new Army(armyID, name, unitList);
    }
}
