package de.uniks.se1ss19teamb.rbsg.request;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public interface RESTRequest {
	public void sendRequest() throws IOException, ParseException;
	
	public void sendRequest(RESTRequestResponseHandler callback) throws IOException, ParseException;
	
	public JSONObject getResponse();
	
	public default boolean getSuccessful() {
		return getResponse() == null ? false : getResponse().get("status").equals("success");
	}
	
	public default String getMessage() {
		return getResponse() == null ? "Request not yet sent to Server" : (String) getResponse().get("message");
	}
}
