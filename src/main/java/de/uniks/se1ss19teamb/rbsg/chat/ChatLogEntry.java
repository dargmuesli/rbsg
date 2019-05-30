package de.uniks.se1ss19teamb.rbsg.chat;

import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;

public class ChatLogEntry {

    public static ArrayList<ChatLogEntry> chatLog = new ArrayList<ChatLogEntry>();
    public Date date = new Date();
    public String sender;
    public String message;

    public ChatLogEntry() {}

    public ChatLogEntry(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.date = new Date();
    }

    public void addToChatLog(String sender, String message) {
        chatLog.add(new ChatLogEntry(sender, message));
    }

    public void writeLog (Path path) throws IOException {
        PrintWriter out = new PrintWriter(path.toString());
        for (ChatLogEntry cle : chatLog) {
            out.println(SerializeUtils.serialize(cle));
        }
        out.close();
    }
}
