package de.uniks.se1ss19teamb.rbsg.chat;

import javax.websocket.OnMessage;
import java.text.SimpleDateFormat;

public class ClientEndpoint {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    @OnMessage
    public void onMessage(Message message){
        System.out.println(String.format("[%s:%s] %s",
        simpleDateFormat.format(message.getReceived()), message.getSender(), message.getMessage()));
    }

}
