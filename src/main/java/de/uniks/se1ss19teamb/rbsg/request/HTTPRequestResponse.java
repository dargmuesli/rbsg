package de.uniks.se1ss19teamb.rbsg.request;


public class HTTPRequestResponse {
	public HTTPRequestResponse(String body, int status, String errorMsg) {
		this.body = body;
		this.status = status;
		this.errorMsg = errorMsg;
	}
	
	public int status;
	
	public String errorMsg;
	
	public String body;
}