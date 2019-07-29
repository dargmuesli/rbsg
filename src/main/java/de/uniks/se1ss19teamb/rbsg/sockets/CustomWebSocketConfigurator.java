package de.uniks.se1ss19teamb.rbsg.sockets;

import de.uniks.se1ss19teamb.rbsg.ui.LoginController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.websocket.ClientEndpointConfig;

public class CustomWebSocketConfigurator extends ClientEndpointConfig.Configurator {

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        super.beforeRequest(headers);
        ArrayList<String> key = new ArrayList<>();
        key.add(LoginController.getUserToken());
        headers.put("userKey", key);
    }
}
