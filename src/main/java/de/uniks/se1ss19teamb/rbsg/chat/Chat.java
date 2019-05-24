package de.uniks.se1ss19teamb.rbsg.chat;

import javax.websocket.OnMessage;
import java.util.ArrayList;

public class Chat {

    ArrayList<Message> chatLog = new ArrayList<Message>();
    // ArrayList<User> users = new ArrayList<User>();

    /*
    add to chatLog:
     -on Message sent
     -on Message received
     -on System message received

    Messages acn be sent to either all user or specific users (chatMode)
    System messages are received by all Users
     */

}
