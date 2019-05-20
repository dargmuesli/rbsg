package de.uniks.se1ss19teamb.rbsg.request;

import org.json.simple.JSONObject;

public class LogoutUserRequest extends AbstractRESTRequest {

    private String userToken;
    
    public LogoutUserRequest(String userToken) {
        this.userToken = userToken;
    }
    
    @Override
    protected JSONObject buildJSON() {
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
