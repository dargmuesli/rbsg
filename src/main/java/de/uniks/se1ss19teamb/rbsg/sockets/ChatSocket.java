package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ChatSocket extends AbstractWebSocket {
    
    private String userKey;
    private String userName;
    
    private List<ChatMessageHandler> handlersChat = new ArrayList<>();
    
    private boolean ignoreOwn;
    
    public ChatSocket(String userName, String userKey) {
        this(userName, userKey, false);
    }
    
    public ChatSocket(String userName, String userKey, boolean ignoreOwn) {
        this.userKey = userKey;
        this.userName = userName;
        this.ignoreOwn = ignoreOwn;
        registerWebSocketHandler((response) -> {
            if (response.get("msg") != null) {
                //TODO Handle error in MSG
                return;
            }
            
            String from = response.get("from").getAsString();
            if (this.ignoreOwn && from.equals(userName)) {
                return;
            }
            
            String msg = response.get("message").getAsString();
            boolean isPrivate = response.get("channel").getAsString().equals("private");
            for (ChatMessageHandler handler : handlersChat) {
                handler.handle(msg, from, isPrivate);
            }
        });
    }
    
    @Override
    protected String getEndpoint() {
        return "/chat?user=" + userName;
    }
    
    @Override
    public String getUserKey() {
        return userKey;
    }
    
    public String getUserName() {
        return userName;
    }
    
    //Custom Helpers
    
    public void registerChatMessageHandler(ChatMessageHandler handler) {
        handlersChat.add(handler);
    }
    
    public void sendMessage(String message) {
        JsonObject json = new JsonObject();
        json.addProperty("channel", "all");
        json.addProperty("message", message);
        sendToWebsocket(json);
    }
    
    public void sendPrivateMessage(String message, String target) {
        JsonObject json = new JsonObject();
        json.addProperty("channel", "private");
        json.addProperty("to", target);
        json.addProperty("message", message);
        sendToWebsocket(json);
    }
}