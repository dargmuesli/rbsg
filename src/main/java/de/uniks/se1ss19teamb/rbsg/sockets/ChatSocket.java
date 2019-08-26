package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.chat.Chat;
import de.uniks.se1ss19teamb.rbsg.crypto.CipherUtils;
import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;

public class ChatSocket extends AbstractMessageWebSocket {

    public static ChatSocket instance;

    private boolean ignoreOwn;
    private List<ChatMessageHandler> handlersChat = new ArrayList<>();

    public ChatSocket(String userName, String userKey) {
        this(userName, userKey, false);
    }

    private ChatSocket(String userName, String userKey, boolean ignoreOwn) {
        this.ignoreOwn = ignoreOwn;

        registerWebSocketHandler((response)
            -> Chat.defaultMessageHandler(response, this.ignoreOwn, userName, handlersChat));
    }

    @Override
    protected String getEndpoint() {
        return "/chat?user=" + LoginController.getUserName();
    }

    @Override
    public void registerMessageHandler(ChatMessageHandler handler) {
        handlersChat.add(handler);
    }

    @Override
    public void sendMessage(String message) {
        JsonObject json = new JsonObject();
        json.addProperty("channel", "all");
        json.addProperty("message", message);
        sendToWebsocket(json);
    }

    @Override
    public void sendPrivateMessage(String message, String target) {
        JsonObject json = new JsonObject();
        json.addProperty("channel", "private");
        json.addProperty("to", target);
        json.addProperty("message", message);
        sendToWebsocket(json);
    }
}