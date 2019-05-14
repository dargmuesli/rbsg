package de.uniks.se1ss19teamb.rbsg.request;

import org.json.simple.JSONObject;

public interface RESTRequestResponseHandler {
	public void handle(JSONObject response);
}
