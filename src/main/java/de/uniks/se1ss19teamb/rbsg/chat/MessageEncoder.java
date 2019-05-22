package de.uniks.se1ss19teamb.rbsg.chat;

import org.json.simple.JSONObject;

import javax.websocket.EncodeException;


public class MessageEncoder {

    public JSONObject encode(final Message message) throws EncodeException{
        JSONObject json = new JSONObject();
        json.put("message", message.getMessage());
        json.put("sender", message.getSender());
        json.put("id", message.getReceived());
        return json;
    }
}