package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;
import java.io.IOException;
import org.apache.http.ParseException;

public interface RestRequest {
    void sendRequest() throws IOException, ParseException;
    
    void sendRequest(RestRequestResponseHandler callback) throws IOException, ParseException;
    
    JsonObject getResponse();
    
    default boolean getSuccessful() {
        return getResponse() != null && getResponse().get("status").getAsString().equals("success");
    }
    
    default String getMessage() {
        return getResponse() == null
                ? "Request not yet sent to Server" : getResponse().get("message").getAsString();
    }
}
