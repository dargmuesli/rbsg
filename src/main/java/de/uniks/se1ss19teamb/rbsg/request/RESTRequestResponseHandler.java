package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public interface RESTRequestResponseHandler {
    void handle(JsonObject response);
}
