package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;

import java.util.ArrayList;
import java.util.List;

public class QueryArmiesRequest extends AbstractRestRequest {

    private String userToken;

    public QueryArmiesRequest(String userToken) {
        this.userToken = userToken;
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
        return "/army";
    }

    @Override
    protected String getUserToken() {
        return userToken;
    }

    //Custom Request Helper

    public ArrayList<Army> getArmies() {
        ArrayList<Army> armies = new ArrayList<>();

        for (JsonElement g : getResponse().get("data").getAsJsonArray()) {
            Army current = new Army();
            JsonObject army = g.getAsJsonObject();
            current.setId(army.get("id").getAsString());
            current.setName(army.get("name").getAsString());

            JsonArray unitIds = army.get("units").getAsJsonArray();
            List<Unit> unitList = new ArrayList<>();

            for (JsonElement unitId : unitIds) {
                Unit unit = new Unit();
                unit.setId(unitId.getAsString());
                unitList.add(unit);
            }

            current.setUnits(unitList);
            armies.add(current);
        }

        return armies;
    }
}
