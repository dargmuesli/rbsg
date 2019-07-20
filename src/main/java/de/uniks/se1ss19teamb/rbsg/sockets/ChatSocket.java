package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;

public class ChatSocket extends AbstractWebSocket {

    public static ChatSocket instance;

    private String userKey;
    private String userName;
    private boolean ignoreOwn;
    private List<ChatMessageHandler> handlersChat = new ArrayList<>();

    public ChatSocket(String userName, String userKey) {
        this(userName, userKey, false);
    }

    private ChatSocket(String userName, String userKey, boolean ignoreOwn) {
        this.userKey = userKey;
        this.userName = userName;
        this.ignoreOwn = ignoreOwn;
        registerWebSocketHandler((response) -> {
            if (response.get("msg") != null) {
                NotificationHandler.getInstance()
                    .sendWarning(response.get("msg").getAsString(), LogManager.getLogger());
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