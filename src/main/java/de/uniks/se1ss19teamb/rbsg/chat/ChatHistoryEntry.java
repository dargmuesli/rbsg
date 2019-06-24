package de.uniks.se1ss19teamb.rbsg.chat;

import java.util.Date;

class ChatHistoryEntry {

    public String message;
    public String sender;
    public String receiver;
    private Date date = new Date();

    public ChatHistoryEntry() {

    }

    public ChatHistoryEntry(String message, String sender) {
        this.message = message;
        this.sender = sender;
        this.receiver = "All";
        this.date = new Date();
    }

    public ChatHistoryEntry(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.date = new Date();
    }
}
