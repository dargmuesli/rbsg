package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;
import org.apache.http.ParseException;

import java.io.IOException;

public interface RESTRequest {
    public void sendRequest() throws IOException, ParseException;
    
    public void sendRequest(RESTRequestResponseHandler callback) throws IOException, ParseException;
    
    public JsonObject getResponse();
    
    public default boolean getSuccessful() {
        return getResponse() == null ? false : getResponse().get("status").equals("success");
    }
    
    public default String getMessage() {
        return getResponse() == null ? "Request not yet sent to Server" : getResponse().get("message").getAsString();
    }
}
