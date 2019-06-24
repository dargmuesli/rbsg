package de.uniks.se1ss19teamb.rbsg.model;

import java.util.Date;

public class ChatLogEntry {

    public String message;
    public String sender;
    public String receiver;
    private Date date = new Date();

    public ChatLogEntry() {

    }

    public ChatLogEntry(String message, String sender) {
        this.message = message;
        this.sender = sender;
        this.receiver = "All";
        this.date = new Date();
    }

    public ChatLogEntry(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.date = new Date();
    }
}
