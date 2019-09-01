package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.Unit;

import java.util.List;

public class CreateArmyRequest extends AbstractDataRestRequest<String> {

    private String userToken;
    private String armyName;
    private List<Unit> units;

    /**
     * Constructor for an army creation request with individual field values.
     *
     * @param armyName  The army's name.
     * @param units     The army's units.
     * @param userToken The token to authenticate with.
     */
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

    /**
     * Standard provider for the request's response.
     *
     * @return The new army's id.
     */
    @Override
    public String getData() {
        JsonObject responseData = getResponse().get("data").getAsJsonObject();
        return responseData.get("id").getAsString();
    }

    @Override
    public String getErrorMessage() {
        return "Could not create army!";
    }
}
