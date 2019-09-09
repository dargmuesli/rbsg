package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.ui.modules.ArmyManagerController;

import java.util.ArrayList;
import java.util.List;

public class QueryArmiesRequest extends AbstractDataRestRequest<ArrayList<Army>> {

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

    /**
     * Standard provider for the request's response.
     *
     * @return The armies.
     */
    @Override
    public ArrayList<Army> getData() {
        ArrayList<Army> armies = new ArrayList<>();

        for (JsonElement g : getResponse().get("data").getAsJsonArray()) {
            JsonObject army = g.getAsJsonObject();
            String id = army.get("id").getAsString();
            String name = army.get("name").getAsString();

            JsonArray unitIds = army.get("units").getAsJsonArray();
            List<Unit> unitList = new ArrayList<>();

            for (JsonElement unitId : unitIds) {
                unitList.add(ArmyManagerController.availableUnits.get(unitId.getAsString()));
            }

            armies.add(new Army(id, name, unitList));
        }

        return armies;
    }

    @Override
    public String getErrorMessage() {
        return "Could not query armies!";
    }
}
