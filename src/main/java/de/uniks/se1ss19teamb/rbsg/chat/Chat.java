package de.uniks.se1ss19teamb.rbsg.chat;

import de.uniks.se1ss19teamb.rbsg.crypto.CipherUtils;
import de.uniks.se1ss19teamb.rbsg.crypto.GenerateKeys;
import de.uniks.se1ss19teamb.rbsg.model.ChatHistoryEntry;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.AbstractMessageWebSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatMessageHandler;
import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtil;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Allows to send public and private messages and provides chat history functionality.
 */
public class Chat {
    /**
     * The path at which the chat log is saved.
     */
    public static Path chatLogPath = Paths.get(
        System.getProperty("java.io.tmpdir") + File.separator + "rbsg_chat-log.txt");

    /**
     * Defines that and how received messages are added to the chat history.
     */
    public ChatMessageHandler chatMessageHandler = (message, from, isPrivate)
        -> {
        if (message.startsWith("[tbe]")) {
            message = CipherUtils.decryptMessage(message.substring(5));
        }

        addToHistory(message, from, isPrivate ? LoginController.getUserName() : "All");
    };

    private static final Logger logger = LogManager.getLogger();
    private ArrayList<ChatHistoryEntry> history = new ArrayList<>();
    private AbstractMessageWebSocket messageWebSocket;
    private Path path;

    /**
     * Constructor that connects to the chat socket and registers the {@link #chatMessageHandler}.
     *
     * @param messageWebSocket The chat socket for communication.
     * @param path             The location at which the chat history is saved.
     */
    public Chat(AbstractMessageWebSocket messageWebSocket, Path path) {
        this.messageWebSocket = messageWebSocket;
        this.messageWebSocket.registerMessageHandler(chatMessageHandler);
        this.path = path;
    }

    public static void defaultMessageHandler(
        JsonObject response, boolean ignoreOwn, String userName, List<ChatMessageHandler> handlersChat) {

        if (response.get("msg") != null) {
            NotificationHandler.sendWarning(response.get("msg").getAsString(), LogManager.getLogger());
            return;
        }

        String from = response.get("from").getAsString();

        if (ignoreOwn && from.equals(userName)) {
            return;
        }

        String msg = response.get("message").getAsString();
        boolean isPrivate = response.get("channel").getAsString().equals("private");
        boolean wasEncrypted = false;

        if (msg.startsWith("[tbe]")) {
            wasEncrypted = true;
            msg = CipherUtils.decryptMessage(msg.substring(5));
        }

        for (ChatMessageHandler handler : handlersChat) {
            handler.handle(msg, from, isPrivate, wasEncrypted);
        }
    }

    /**
     * Disconnects the chat socket, writes the chat history to file and logs the user out.
     */
    public void disconnect() {
        this.messageWebSocket.disconnect();
        writeHistory();
        new LogoutUserRequest(LoginController.getUserToken()).sendRequest();
    }

    /**
     * Sends a public message via the chat socket.
     *
     * @param message The textual content.
     */
    public void sendMessage(String message) {
        executeCommandsOnMessage(message, null).ifPresent(s
            -> this.messageWebSocket.sendMessage(message));
    }

    /**
     * Sends a private message via the chat socket.
     *
     * @param message  The textual content.
     * @param receiver The receiver's name.
     */
    public void sendMessage(String message, String receiver) {
        addToHistory(message, LoginController.getUserName(), receiver);
        executeCommandsOnMessage(message, receiver).ifPresent(s
            -> this.messageWebSocket.sendPrivateMessage(message, receiver));
    }

    /**
     * Adds an entry to the chat history.
     *
     * @param message  The textual content.
     * @param receiver The receiver's name.
     * @param sender   The sender's name.
     */
    private void addToHistory(String message, String sender, String receiver) {
        history.add(new ChatHistoryEntry(message, sender, receiver));
    }

    public static Optional<String> executeCommandsOnMessage(String message, String receiver) {

        // Export the public key only.
        if (message.equals("/enc_export")) {
            Optional<File> optionalFile = SerializeUtil.chooseFile(true);
            Optional<PublicKey> optionalPublicKey = GenerateKeys.readPublicKey(LoginController.getUserName());

            if (optionalFile.isPresent() && optionalPublicKey.isPresent()) {
                try {
                    Files.write(optionalFile.get().toPath().resolve(
                        "public-key_" + SerializeUtil.sanitizeFilename(LoginController.getUserName()) + ".der"),
                        optionalPublicKey.get().getEncoded());
                    NotificationHandler.sendSuccess("Public key exported successfully.", LogManager.getLogger());
                } catch (IOException e) {
                    NotificationHandler.sendError("Could not export public key!", LogManager.getLogger(), e);
                }
            }

            return Optional.empty();
        }

        if (receiver != null) {

            // Encrypt the message.
            // tbe = team b encrypted
            if (message.startsWith("/enc ")) {
                message = "[tbe]" + CipherUtils.encryptMessage(message.substring(5), receiver);
            }
        }

        return Optional.of(message);
    }

    /**
     * Writes the chat history to file.
     */
    private void writeHistory() {
        if (Files.notExists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                NotificationHandler.sendError("Chat directory could not be created!", logger, e);
            }
        }

        try (FileWriter fw = new FileWriter(path.toString(), true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (ChatHistoryEntry cle : history) {
                out.println(SerializeUtil.serialize(cle));
            }
        } catch (IOException e) {
            NotificationHandler.sendError("Writing to the chat directory failed!", logger, e);
        }
    }

    /**
     * Deletes the chat history file.
     */
    public void deleteHistory() {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            NotificationHandler.sendError("Chat log could not be deleted!", logger, e);
        }
    }
}
