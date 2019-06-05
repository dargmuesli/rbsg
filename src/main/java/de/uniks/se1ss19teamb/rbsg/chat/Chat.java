package de.uniks.se1ss19teamb.rbsg.chat;

import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
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

public class Chat  {

    private ArrayList<ChatLogEntry> chatLog = new ArrayList<>();
    private ChatSocket chatSocket;
    private Path path;

    public Chat(String sender, String userKey) {
        this(new ChatSocket(sender, userKey), Paths.get("src/main/resources/de/uniks/se1ss19teamb/rbsg/chatLog.txt"));
    }

    public Chat(String sender, String userKey, Path path) {
        this(new ChatSocket(sender, userKey), path);
    }

    private Chat(ChatSocket chatSocket, Path path) {
        this.chatSocket = chatSocket;
        this.chatSocket.connect();
        this.chatSocket.registerChatMessageHandler((message, from, isPrivate)
            -> addToChatLog(message, from, this.chatSocket.getUserName()));
        this.path = path;
    }

    public void disconnect() throws IOException {
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

    public void addToChatLog(String message, String sender, String receiver) {
        chatLog.add(new ChatLogEntry(message, sender, receiver));
    }

    public void writeLog() {
        if (Files.notExists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter fw = new FileWriter(path.toString(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            for (ChatLogEntry cle : chatLog) {
                out.println(SerializeUtils.serialize(cle));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteLog() {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            new ErrorHandler().sendError("Chat log could not be deleted!");
        }
    }
}
