package de.uniks.se1ss19teamb.rbsg.chat;

import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatMessageHandler;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Chat {
    private static final Logger logger = LogManager.getLogger(Chat.class);

    private ErrorHandler errorHandler = ErrorHandler.getErrorHandler();
    private ArrayList<ChatLogEntry> chatLog = new ArrayList<>();
    private ChatSocket chatSocket;
    public ChatMessageHandler chatMessageHandler = (message, from, isPrivate)
        -> addToChatLog(message, from, isPrivate ? chatSocket.getUserName() : "All");
    private Path path;

    private Chat(String sender, String userKey) {
        this(new ChatSocket(sender, userKey), Paths.get("src/main/resources/de/uniks/se1ss19teamb/rbsg/chatLog.txt"));
    }

    private Chat(String sender, String userKey, Path path) {
        this(new ChatSocket(sender, userKey), path);
    }

    public Chat(ChatSocket chatSocket, Path path) {
        this.chatSocket = chatSocket;
        this.chatSocket.connect();
        this.chatSocket.registerChatMessageHandler(chatMessageHandler);
        this.path = path;
    }

    public void disconnect() {
        this.chatSocket.disconnect();
        writeLog();
        new LogoutUserRequest(this.chatSocket.getUserKey()).sendRequest();
    }

    public void sendMessage(String message) {
        // TODO send action (All)
        this.chatSocket.sendMessage(message);
    }

    public void sendMessage(String message, String receiver) {
        addToChatLog(message, this.chatSocket.getUserName(), receiver);
        this.chatSocket.sendPrivateMessage(message, receiver);
    }

    public void addToChatLog(String message, String sender) {
        chatLog.add(new ChatLogEntry(message, sender));
    }

    private void addToChatLog(String message, String sender, String receiver) {
        chatLog.add(new ChatLogEntry(message, sender, receiver));
    }

    private void writeLog() {
        if (Files.notExists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                errorHandler.sendError("Chat-Verzeichnis konnte nicht erstellt werden!");
                logger.error(e);
            }
        }

        try (FileWriter fw = new FileWriter(path.toString(), true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (ChatLogEntry cle : chatLog) {
                out.println(SerializeUtils.serialize(cle));
            }
        } catch (IOException e) {
            errorHandler.sendError("Fehler beim Schreiben im Chat-Verzeichnis!");
            logger.error(e);
        }
    }

    public void deleteLog() {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            ErrorHandler.getErrorHandler().sendError("Chat log could not be deleted!");
        }
    }
}
