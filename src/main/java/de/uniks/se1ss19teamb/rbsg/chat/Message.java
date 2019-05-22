package de.uniks.se1ss19teamb.rbsg.chat;

import java.util.Date;

public class Message {

    private String message;
    private String sender;
    private Date received;

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}