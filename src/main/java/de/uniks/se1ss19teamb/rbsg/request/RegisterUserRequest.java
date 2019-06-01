package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public class RegisterUserRequest extends AbstractRESTRequest{
    
    private String username, password;
    
    /**
     * Returns an empty "data" array, a "status" ("success" | "failure"), and a human readable "message"
     * 
     * @param username The Username to be used
     * @param password The Password to be associated with the user
     */
    public RegisterUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RegisterUserRequest(String username, String password, HTTPManager httpManager) {
        this.username = username;
        this.password = password;
        this.httpManager = httpManager;
    }
    
    @Override
    protected JsonObject buildJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", username);
        json.addProperty("password", password);
        return json;
    }

    @Override
    protected String getHTTPMethod() {
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
