package de.uniks.se1ss19teamb.rbsg.chat;

import java.io.IOException;
import java.nio.file.Path;

public class Chat {

    private String CHANNEL = "/chat?user=";
    ChatLogEntry chatLogEntry = new ChatLogEntry();
    public String userName;
    public String sendToUser;
    public String message;

    public Chat (String userName) {
        this.userName = userName;
        chatLogEntry.sender = userName;
    }

    public void setMessage (String message) {
        this.message = message;
        chatLogEntry.message = message;
        chatLogEntry.addToChatLog(this.userName, message);
    }

    public void sendMessage() {
        // send Message (all)
    }

    public void sendMessage(String sendToUser) {
        // send Message (private)
    }

    public void writeLog(Path path) throws IOException {
        chatLogEntry.writeLog(path);
    }
}
