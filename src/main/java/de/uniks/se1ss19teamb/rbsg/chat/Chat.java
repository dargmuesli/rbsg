package de.uniks.se1ss19teamb.rbsg.chat;

import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Chat  {

    ArrayList<ChatLogEntry> chatLog = new ArrayList<ChatLogEntry>();
    public String sender;
    LoginUserRequest login;
    LogoutUserRequest logout;
    ChatSocket chatSocket;
    Path path = Paths.get("src/main/resources/de/uniks/se1ss19teamb/rbsg/chatLog.txt");

    public Chat (String sender, String password) throws IOException {
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
        // TODO send action (private)
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

    public void writeLog (Path path) throws IOException {
        PrintWriter out = new PrintWriter(path.toString());
        for (ChatLogEntry cle : chatLog) {
            out.println(SerializeUtils.serialize(cle));
        }
        out.close();
    }
}
