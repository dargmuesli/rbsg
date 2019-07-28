package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.Unit;

import java.util.ArrayList;

public class QueryUnitsRequest extends AbstractDataRestRequest<ArrayList<Unit>> {

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

    /**
     * Standard provider for the request's response.
     *
     * @return The units.
     */
    @Override
    public ArrayList<Unit> getData() {
        ArrayList<Unit> units = new ArrayList<>();
        for (JsonElement g : getResponse().get("data").getAsJsonArray()) {
            Unit current = new Unit();
            JsonObject unit = g.getAsJsonObject();
            current.setId(unit.get("id").getAsString());
            current.setType(unit.get("type").getAsString());
            current.setMp(unit.get("mp").getAsInt());
            current.setHp(unit.get("hp").getAsInt());
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

    @Override
    public String getErrorMessage() {
        return "Could not query units!";
    }
}
