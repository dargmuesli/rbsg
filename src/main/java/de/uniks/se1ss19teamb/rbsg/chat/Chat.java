package de.uniks.se1ss19teamb.rbsg.chat;

import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;

public class Chat {

    private String CHANNEL = "/chat?user=";
    ArrayList<ChatLogEntry> chatLog = new ArrayList<ChatLogEntry>();
    public String sender;
    public String receiver;
    public String message;

    public Chat (String sender) {
        this.sender = sender;
    }

    public void sendMessage(String message) {
        // TODO send action (All)
        addToChatLog(this.sender, message);
    }

    public void sendMessage(String receiver, String message) {
        // TODO send action (private)
        addToChatLog(this.sender, receiver, message);
    }
    
    public void addToChatLog(String sender, String message) {
        chatLog.add(new ChatLogEntry(sender, message));
    }

    public void addToChatLog(String sender, String receiver, String message) {
        chatLog.add(new ChatLogEntry(sender, receiver, message));
    }

    public void writeLog (Path path) throws IOException {
        PrintWriter out = new PrintWriter(path.toString());
        for (ChatLogEntry cle : chatLog) {
            out.println(SerializeUtils.serialize(cle));
        }
        out.close();
    }
}
