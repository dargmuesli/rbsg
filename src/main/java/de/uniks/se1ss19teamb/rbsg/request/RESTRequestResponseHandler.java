package de.uniks.se1ss19teamb.rbsg.request;

import com.google.gson.JsonObject;

public interface RestRequestResponseHandler {
    void handle(JsonObject response);
}
