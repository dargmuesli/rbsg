package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;
import java.io.IOException;
import org.apache.http.ParseException;

public interface RESTRequest {
    void sendRequest() throws IOException, ParseException;
    
    void sendRequest(RESTRequestResponseHandler callback) throws IOException, ParseException;
    
    JsonObject getResponse();
    
    default boolean getSuccessful() {
        return getResponse() == null
                ? false : getResponse().get("status").getAsString().equals("success");
    }
    
    default String getMessage() {
        return getResponse() == null
                ? "Request not yet sent to Server" : getResponse().get("message").getAsString();
    }
}
