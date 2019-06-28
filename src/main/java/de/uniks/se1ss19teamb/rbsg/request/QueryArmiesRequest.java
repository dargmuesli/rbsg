package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.Army;

import java.util.ArrayList;

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
        ArrayList<Army> armies = new ArrayList<Army>();

        for (JsonElement g : getResponse().get("data").getAsJsonArray()) {
            Army current = new Army();
            JsonObject army = g.getAsJsonObject();
            current.setId(army.get("id").getAsString());
            current.setName(army.get("name").getAsString());

            JsonArray units = army.get("units").getAsJsonArray();
            ArrayList<String> unitList = new ArrayList<>();
            for (JsonElement unit : units) {
                unitList.add(unit.getAsString());
            }
            current.setUnits(unitList);
            armies.add(current);
        }

        return armies;
    }
}
