package de.uniks.se1ss19teamb.rbsg.request;

import org.json.simple.JSONObject;

public abstract class AbstractRESTRequest implements RESTRequest{
	
	private JSONObject response = null;
	
	protected abstract JSONObject buildJSON();
	
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
