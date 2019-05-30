package de.uniks.se1ss19teamb.rbsg.sockets;

import javax.websocket.ClientEndpointConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomWebSocketConfigurator extends ClientEndpointConfig.Configurator {
    
    public static String userKey = "";
    
    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        super.beforeRequest(headers);
        ArrayList<String> key = new ArrayList<>();
        key.add(userKey);
        headers.put("userKey", key);
    }
    
    protected static void setUserKey(String key) {
        userKey = key;
    }
}
