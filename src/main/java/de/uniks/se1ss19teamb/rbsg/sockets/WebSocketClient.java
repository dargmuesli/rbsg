package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.websocket.*;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

@ClientEndpoint(configurator = CustomWebSocketConfigurator.class)
public class WebSocketClient {
    
    public static final String NOOP = "noop";
    private Session mySession;
    private Timer noopTimer;
    private WebSocketResponseHandler initialHandler;
    
    public WebSocketClient(URI endpoint, WebSocketResponseHandler initialHandler) {
        this.noopTimer = new Timer();
        this.initialHandler = initialHandler;
        
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendMessage(JsonObject message) {
        if (this.mySession != null && this.mySession.isOpen()) {
            try {
                this.mySession.getBasicRemote().sendText(message.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void stop() throws Exception {
        if (this.mySession != null && this.mySession.isOpen()) {
            this.mySession.close();
            this.noopTimer.cancel();
        }
    }
    
    @OnOpen
    public void OnOpen(Session session) {
        this.mySession = mySession;
        System.out.println("WS connected to " + this.mySession.getRequestURI());
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(mySession.isOpen()) {
                    try {
                        mySession.getBasicRemote().sendText(NOOP);
                    } catch (Exception e){
                        System.err.println("Can not send NOOP");
                    }
                }
            }
            
        };
        this.noopTimer.schedule(task , 0, 1000 * 60 * 4);
    }
    
    @OnMessage
    public void OnMessage(String message) {
    	JsonParser parser = new JsonParser();
        JsonObject response = (JsonObject) parser.parse(message);
        initialHandler.handle(response);
    }
    
    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }
    
    @OnClose
    public void onClose(Session session) {
        this.mySession = null;
        System.out.println("WS " + mySession.getRequestURI() + "closed.");
        
        this.noopTimer.cancel();
    }
}
