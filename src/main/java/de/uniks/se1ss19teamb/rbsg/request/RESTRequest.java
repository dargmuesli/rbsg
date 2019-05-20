package de.uniks.se1ss19teamb.rbsg.request;

import org.json.simple.JSONObject;

public interface RESTRequest {
	public void sendRequest();
	
	public void sendRequest(RESTRequestResponseHandler callback);
	
	public JSONObject getResponse();
}
