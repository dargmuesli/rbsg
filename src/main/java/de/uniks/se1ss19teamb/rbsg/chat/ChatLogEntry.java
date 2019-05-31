package de.uniks.se1ss19teamb.rbsg.chat;

import java.util.Date;

public class ChatLogEntry {

    public Date date = new Date();
    public String sender;
    public String receiver;
    public String message;

    public ChatLogEntry() {

    }

    public ChatLogEntry(String sender, String message) {
        this.sender = sender;
        this.receiver = "All";
        this.message = message;
        this.date = new Date();
    }

    public ChatLogEntry(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date = new Date();
    }
}
