package de.uniks.se1ss19teamb.rbsg.request;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.uniks.se1ss19teamb.rbsg.HTTPManager;

public abstract class AbstractRESTRequest implements RESTRequest{
	
	private static final String url = "https://rbsg.uniks.de/api";
	
	private JSONObject response = null;
	
	private static HTTPManager httpManager = new HTTPManager();
	
	protected abstract JSONObject buildJSON();
	
	protected abstract String getHTTPMethod(); //"get", "post", "delete", "put"
	
	protected abstract String getEndpoint(); //"/user", "/user/login", etc.
	
	protected abstract String getUserToken();
	
	@Override
	public void sendRequest() throws IOException, ParseException {
		String result = "";
		String token = getUserToken();
		try {
			switch(getHTTPMethod()) {
			case "get":
				result = httpManager.get(new URI(url + getEndpoint()), token == null ? null : new Header[] { new BasicHeader("userKey", token) });
				break;
			case "post":
				result = httpManager.post(new URI(url + getEndpoint()), token == null ? null : new Header[] { new BasicHeader("userKey", token) }, new StringEntity(buildJSON().toString()));
				break;
			case "delete":
				result = httpManager.post(new URI(url + getEndpoint()), token == null ? null : new Header[] { new BasicHeader("userKey", token) }, null);
				break;
				
			default:
				throw new MethodNotSupportedException("Method not Supported: " + getHTTPMethod());
			}
		} catch(Exception e) {
			throw new IOException(e);
		} finally {
			JSONParser parser = new JSONParser();
			response = (JSONObject) parser.parse(result);
		}
	}
	
	@Override
	public void sendRequest(RESTRequestResponseHandler callback) throws IOException, ParseException {
		sendRequest();
		
		callback.handle(response);
	}
	
	@Override
	public JSONObject getResponse() {
		return response;
	}
	
}
