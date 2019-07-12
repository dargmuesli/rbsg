package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.Unit;

import java.util.ArrayList;

public class QueryUnitsRequest extends AbstractRestRequest {

    private String userToken;

    public QueryUnitsRequest(String userToken) {
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
        return "/army/units";
    }

    @Override
    protected String getUserToken() {
        return userToken;
    }

    //Custom Request Helper

    public ArrayList<Unit> getUnits() {
        ArrayList<Unit> units = new ArrayList<>();

        for (JsonElement g : getResponse().get("data").getAsJsonArray()) {
            JsonObject unit = g.getAsJsonObject();
            Unit current = new Unit();

            current.setMp(unit.get("mp").getAsInt());
            current.setHp(unit.get("hp").getAsInt());
            current.setType(unit.get("type").getAsString());
            ArrayList<String> unitsList = new ArrayList<>();
            JsonArray unitArray = unit.get("canAttack").getAsJsonArray();

            for (JsonElement o : unitArray) {
                unitsList.add(o.getAsString());
            }

            current.setCanAttack(unitsList);
            units.add(current);
        }

        return units;
    }
}
