package de.uniks.se1ss19teamb.rbsg.chat;

import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Chat  {

    private ArrayList<ChatLogEntry> chatLog = new ArrayList<>();
    private String sender;
    private LoginUserRequest login;
    private LogoutUserRequest logout;
    private ChatSocket chatSocket;
    private Path path = Paths.get("src/main/resources/de/uniks/se1ss19teamb/rbsg/chatLog.txt");

    public Chat(String sender, String password) throws IOException {
        this.sender = sender;
        // Login
        this.login = new LoginUserRequest(sender, password);
        login.sendRequest();

        this.chatSocket = new ChatSocket(sender, login.getUserKey());

        this.chatSocket.connect();

        this.receiveMessages();
    }

    public void disconnect() throws IOException {
        writeLog(this.path);
        this.chatSocket.disconnect();
        this.logout = new LogoutUserRequest(this.login.getUserKey());
        this.logout.sendRequest();
    }

    public void sendMessage(String message) {
        // TODO send action (All)
        this.chatSocket.sendMessage(message);
    }

    public void sendMessage(String message, String receiver) {
        addToChatLog(message, this.sender, receiver);
        this.chatSocket.sendPrivateMessage(message, receiver);
    }

    public void receiveMessages() {
        this.chatSocket.registerChatMessageHandler((message, from, isPrivate) -> {
            addToChatLog(message, from, this.sender);
        });
    }

    public void addToChatLog(String message, String sender) {
        chatLog.add(new ChatLogEntry(message, sender));
    }

    public void addToChatLog(String message, String sender, String receiver) {
        chatLog.add(new ChatLogEntry(message, sender, receiver));
    }

    public void writeLog() {
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
