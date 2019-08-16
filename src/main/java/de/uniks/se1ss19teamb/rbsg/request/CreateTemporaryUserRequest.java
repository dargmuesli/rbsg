package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class CreateTemporaryUserRequest extends AbstractDataRestRequest<ArrayList<String>> {


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
        return "/user/temp";
    }

    @Override
    protected String getUserToken() {
        return null;
    }

    @Override
    public ArrayList<String> getData() {
        ArrayList<String> tempUserData = new ArrayList<>();
        tempUserData.add(0, ((JsonObject) getResponse().get("data")).get("name").getAsString());
        tempUserData.add(1, ((JsonObject) getResponse().get("data")).get("password").getAsString());

        return tempUserData;
    }

    @Override
    public String getErrorMessage() {
        return "Could not query users in lobby!";
    }
}
