package de.uniks.se1ss19teamb.rbsg.request;

import org.json.simple.JSONObject;

public class QueryUsersInLobbyRequest extends AbstractRESTRequest {

	private String userToken;
	
	public QueryUsersInLobbyRequest(String userToken) {
		this.userToken = userToken;
	}
	
	@Override
	protected JSONObject buildJSON() {
		return null;
	}

	@Override
	protected String getHTTPMethod() {
		return "get";
	}

	@Override
	protected String getEndpoint() {
		return "/user";
	}

	@Override
	protected String getUserToken() {
		return userToken;
	}

}
