package de.uniks.se1ss19teamb.rbsg.sockets;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWebSocket implements WebSocket {

	private static final String url = "wss://rbsg.uniks.de/ws";
	
	private	List<WebSocketResponseHandler> handlers = new ArrayList<WebSocketResponseHandler>();
	
	protected WebSocketClient websocket;
	
	protected abstract String getEndpoint();
	
	protected abstract String getUserKey();
	
	@Override
	public void connect() {
		if(getUserKey() != null) {
			WebSocket.changeUserKey(getUserKey());
		}
		
		if(websocket == null) {
			try {
				websocket = new WebSocketClient(new URI(url + getEndpoint()), (response) ->  {
					for(WebSocketResponseHandler handler : handlers)
						handler.handle(response);
				});
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		else {
			//TODO Should not Occur without Bugs
		}
	}

	@Override
	public void disconnect() {
		try {
			websocket.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		websocket = null;
	}

	@Override
	public void registerWebSocketHandler(WebSocketResponseHandler handler) {
		handlers.add(handler);
	}
	
}
