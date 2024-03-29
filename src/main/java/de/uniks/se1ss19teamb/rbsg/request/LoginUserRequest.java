package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public class LoginUserRequest extends AbstractDataRestRequest<String> {

    private String username;
    private String password;

    /**
     * Returns an empty "data" array, a "status" ("success" | "failure"), and a human readable
     * "message".
     *
     * @param username The User to be logged in
     * @param password The Password associated with the user
     */
    public LoginUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected JsonObject buildJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", username);
        json.addProperty("password", password);
        return json;
    }

    @Override
    protected String getHttpMethod() {
        return "post";
    }

    @Override
    protected String getEndpoint() {
        return "/user/login";
    }

    @Override
    protected String getUserToken() {
        return null;
    }

    /**
     * Standard provider for the request's response.
     *
     * @return The user's key.
     */
    @Override
    public String getData() {
        return (((JsonObject) getResponse().get("data")).get("userKey").getAsString());
    }

    @Override
    public String getErrorMessage() {
        return "Could not log user in!";
    }

}
