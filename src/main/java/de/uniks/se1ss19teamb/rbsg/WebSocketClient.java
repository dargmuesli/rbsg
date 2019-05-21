package de.uniks.se1ss19teamb.rbsg;

import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.security.auth.callback.Callback;
import javax.websocket.*;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

@ClientEndpoint(configurator = CustomWebSocketConfigurator.class)
public class WebSocketClient {
    
    public static final String NOOP = "noop";
    private Session mySession;
    private Timer noopTimer;
    
    public WebSocketClient(URI endpoint) {
        this.noopTimer = new Timer();
        
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpoint);
        } catch (Exception e) {
            e.printStackTrace();
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
