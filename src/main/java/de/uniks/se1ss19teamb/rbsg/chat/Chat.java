package de.uniks.se1ss19teamb.rbsg.chat;

import javax.websocket.OnMessage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class Chat {

    public static ArrayList<String> chatLog = new ArrayList<String>();
    public static Date date = new Date();
    // ArrayList<User> users = new ArrayList<User>();

    /*
    add to chatLog:
     -on Message sent
     -on Message received
     -on System message received

    Messages acn be sent to either all user or specific users (chatMode)
    System messages are received by all Users
     */

    public class ChatLog {

    }

    public void writeLog(String fileUrl) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileUrl);
        DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
        outStream.writeUTF("Chat log seit dem " + date + "\n\n\n");
        for (int i = 0; i<chatLog.size(); i++){
            outStream.writeUTF(chatLog.get(i) + "\n");
        }
        outStream.close();
    }


    public static class Message {

        private String message;
        private String sender;
        private Date received;

        public Message(String message, String sender, Date received){
            this.message = message;
            this.sender = sender;
            this.received = received;
            chatLog.add("\"" + this.message + "\", gesendet von : " + this.sender + " - " + this.received);
        }

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

}
