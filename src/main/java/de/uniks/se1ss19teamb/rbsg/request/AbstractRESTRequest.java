package de.uniks.se1ss19teamb.rbsg.request;

import org.json.simple.JSONObject;

public abstract class AbstractRESTRequest implements RESTRequest{
	
	private static final String url = "https://rbsg.uniks.de/api";
	
	private JSONObject response = null;
	
	protected abstract JSONObject buildJSON();
	
	protected abstract String getHTTPMethod(); //"get", "post", "delete", "put"
	
	protected abstract String getEndpoint(); //"/user", "/user/login", etc.
	
	@Override
	public void sendRequest() {
		JSONObject request = buildJSON();
		
		//TODO TB-24 & TB-25
		//
		//Request with request
		//
		//result = result from HTTP
	}
	
	@Override
	public void sendRequest(RESTRequestResponseHandler callback) {
		sendRequest();
		
		callback.handle(response);
	}
	
	@Override
	public JSONObject getResponse() {
		return response;
	}
	
}
