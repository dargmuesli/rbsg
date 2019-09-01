package de.uniks.se1ss19teamb.rbsg.sockets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import javax.websocket.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ClientEndpoint(configurator = CustomWebSocketConfigurator.class)
public class WebSocketClient {

    public Session mySession;

    private static final Logger logger = LogManager.getLogger();
    private static final String NOOP = "noop";

    private Timer noopTimer;
    private WebSocketMessageHandler initialHandler;

    WebSocketClient(URI endpoint, WebSocketMessageHandler initialHandler) {
        this.noopTimer = new Timer();
        this.initialHandler = initialHandler;

        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpoint);
        } catch (Exception e) {
            NotificationHandler.sendError("Creation of the websocket client failed!", logger, e);
        }
    }

    /**
     * Sends a message to the websocket.
     *
     * @param message The message to send.
     */
    public void sendMessage(JsonObject message) {
        if (this.mySession != null && this.mySession.isOpen()) {
            try {
                this.mySession.getBasicRemote().sendText(message.toString());
            } catch (Exception e) {
                NotificationHandler.sendError("Message could not be sent to the websocket client!", logger, e);
            }
        }
    }

    void stop() throws Exception {
        if (this.mySession != null && this.mySession.isOpen()) {
            this.mySession.close();
            this.noopTimer.cancel();
        }
    }

    /**
     * Sends a NOOP regularly to keep the websocket connection alive.
     *
     * @param session The now open session.
     */
    @OnOpen
    public void onOpen(Session session) {
        this.mySession = session;
        logger.info("WS connected to " + this.mySession.getRequestURI());
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (mySession.isOpen()) {
                    try {
                        mySession.getBasicRemote().sendText(NOOP);
                    } catch (Exception e) {
                        NotificationHandler.sendError("Can not send NOOP", logger);
                    }
                }
            }

        };
        // send NoOp every 30 seconds
        this.noopTimer.schedule(task, 0, 1000 * 30);
    }

    /**
     * Parses the message from JSON and calls the message handler.
     *
     * @param message The message that was received.
     */
    @OnMessage
    public void onMessage(String message) {
        JsonParser parser = new JsonParser();
        JsonObject response = (JsonObject) parser.parse(message);
        initialHandler.handle(response);
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    /**
     * Resets the websocket configuration when it closes and prints the reason if possible.
     *
     * @param session     The session that is closed.
     *                    Currently not used.
     * @param closeReason The reason why the connection was closed.
     */
    @SuppressWarnings("unused")
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        String message = "WS " + mySession.getRequestURI() + " closed.";

        if (!closeReason.getReasonPhrase().equals("")) {
            message += " Reason: " + closeReason.getReasonPhrase();
            logger.warn(message);
        } else {
            logger.info(message);
        }
        this.mySession = null;
        this.noopTimer.cancel();
    }
}
