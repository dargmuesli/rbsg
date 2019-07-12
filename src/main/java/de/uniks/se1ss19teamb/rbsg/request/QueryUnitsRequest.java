package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.model.units.*;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;

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

    public ArrayList<AbstractUnit> getUnits() {
        ArrayList<AbstractUnit> units = new ArrayList<>();

        for (JsonElement g : getResponse().get("data").getAsJsonArray()) {
            JsonObject unit = g.getAsJsonObject();
            AbstractUnit current;

            switch (unit.get("type").getAsString()) {
                case "Bazooka Trooper":
                    current = new BazookaTrooper();
                    break;
                case "Chopper":
                    current = new Chopper();
                    break;
                case "Heavy Tank":
                    current = new HeavyTank();
                    break;
                case "Infantry":
                    current = new Infantry();
                    break;
                case "Jeep":
                    current = new Jeep();
                    break;
                case "Light Tank":
                    current = new LightTank();
                    break;
                default:
                    NotificationHandler.getInstance()
                        .sendError("Unit type unknown: " + unit.get("type").getAsString(), LogManager.getLogger());
                    return null;
            }

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
}
