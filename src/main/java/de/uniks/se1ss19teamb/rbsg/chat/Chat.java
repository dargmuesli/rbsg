package de.uniks.se1ss19teamb.rbsg.chat;

import de.uniks.se1ss19teamb.rbsg.model.ChatHistoryEntry;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatMessageHandler;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Allows to send public and private messages and provides chat history functionality.
 */
public class Chat {
    private static final Logger logger = LogManager.getLogger();
    private ArrayList<ChatHistoryEntry> history = new ArrayList<>();
    private ChatSocket chatSocket;
    private GameSocket gameSocket;
    /**
     * Defines that and how received messages are added to the chat history.
     */
    public ChatMessageHandler chatMessageHandler = (message, from, isPrivate)
        -> addToHistory(message, from, isPrivate ? chatSocket.getUserName() : "All");
    public ChatMessageHandler gameChatMessageHandler = (message, from, isPrivate)
        -> addToHistory(message, from, isPrivate ? gameSocket.getUserName() : "All");
    private Path path;

    /**
     * Constructor that connects to the chat socket and registers the {@link #chatMessageHandler}.
     *
     * @param chatSocket The chat socket for communication.
     * @param path       The location at which the chat history is saved.
     */
    public Chat(ChatSocket chatSocket, Path path) {
        this.chatSocket = chatSocket;
        this.chatSocket.registerChatMessageHandler(chatMessageHandler);
        this.path = path;
    }

    public Chat(GameSocket gameSocket, Path path) {
        this.gameSocket = gameSocket;
        this.gameSocket.registerGameMessageHandler(gameChatMessageHandler);
        this.path = path;
    }

    /**
     * Disconnects the chat socket, writes the chat history to file and logs the user out.
     */
    public void disconnect() {
        this.chatSocket.disconnect();
        writeHistory();
        new LogoutUserRequest(this.chatSocket.getUserKey()).sendRequest();
    }

    /**
     * Sends a public message via the chat socket.
     *
     * @param message The textual content.
     */
    public void sendMessage(String message) {
        this.chatSocket.sendMessage(message);
    }

    public void gameSendMessage(String message) {
        this.gameSocket.sendMessage(message);
    }

    /**
     * Sends a private message via the chat socket.
     *
     * @param message  The textual content.
     * @param receiver The receiver's name.
     */
    public void sendMessage(String message, String receiver) {
        addToHistory(message, this.chatSocket.getUserName(), receiver);
        this.chatSocket.sendPrivateMessage(message, receiver);
    }

    public void gameSendMessage(String message, String receiver) {
        addToHistory(message, this.gameSocket.getUserName(), receiver);
        this.gameSocket.sendPrivateMessage(message, receiver);
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

    /**
     * Writes the chat history to file.
     */
    private void writeHistory() {
        if (Files.notExists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                NotificationHandler.getInstance()
                    .sendError("Chat-Verzeichnis konnte nicht erstellt werden!", logger, e);
            }
        }

        try (FileWriter fw = new FileWriter(path.toString(), true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (ChatHistoryEntry cle : history) {
                out.println(SerializeUtils.serialize(cle));
            }
        } catch (IOException e) {
            NotificationHandler.getInstance().sendError("Fehler beim Schreiben im Chat-Verzeichnis!", logger, e);
        }
    }

    /**
     * Deletes the chat history file.
     */
    public void deleteHistory() {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            NotificationHandler.getInstance().sendError("Chat log could not be deleted!", logger, e);
        }
    }
}
