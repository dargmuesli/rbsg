package de.uniks.se1ss19teamb.rbsg.chat;

import org.json.simple.JSONObject;

import java.util.Date;

public class MessageDecoder {
    public Message decode(final JSONObject textMessage){
        Message message = new Message();
        message.setMessage((String) textMessage.get("message"));
        message.setSender((String) textMessage.get("sender"));
        message.setReceived(new Date());
        return message;
    }
}
