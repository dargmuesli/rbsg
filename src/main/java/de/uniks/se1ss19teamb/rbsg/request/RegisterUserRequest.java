package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public class RegisterUserRequest extends AbstractRestRequest {
    
    private String username;
    private String password;
    
    /**
     * Returns an empty "data" array, a "status" ("success" | "failure"), and a human readable
     * "message".
     * 
     * @param username The Username to be used
     * @param password The Password to be associated with the user
     */
    public RegisterUserRequest(String username, String password) {
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
        return "/user";
    }

    @Override
    protected String getUserToken() {
        return null;
    }
}
