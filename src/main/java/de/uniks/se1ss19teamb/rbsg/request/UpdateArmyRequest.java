package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;

import java.util.List;

public class UpdateArmyRequest extends AbstractRestRequest {

    private String userToken;
    private String armyId;
    private String armyName;
    private List<Unit> units;

    /**
     * Constructor for an army update request.
     *
     * @param army      The army to update.
     *                  Id, name and units are taken from it.
     * @param userToken The token to authenticate with.
     */
    public UpdateArmyRequest(Army army, String userToken) {
        this.userToken = userToken;
        armyId = army.getId();
        armyName = army.getName();
        units = army.getUnits();
    }

    @Override
    protected JsonObject buildJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", armyName);
        JsonArray unitIdArray = new JsonArray();

        for (Unit unit : units) {
            unitIdArray.add(unit.getId());
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

    @Override
    public String getErrorMessage() {
        return "Could not update army!";
    }

}
