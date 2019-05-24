package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public class LogoutUserRequest extends AbstractRESTRequest {

    private String userToken;
    
    public LogoutUserRequest(String userToken) {
        this.userToken = userToken;
    }
    
    @Override
    protected JsonObject buildJson() {
        return null;
    }

    @Override
    protected String getHTTPMethod() {
        return "get";
    }

    @Override
    protected String getEndpoint() {
        return "/user/logout";
    }

    @Override
    protected String getUserToken() {
        return userToken;
    }
}
