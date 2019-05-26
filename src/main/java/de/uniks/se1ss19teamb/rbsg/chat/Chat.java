package de.uniks.se1ss19teamb.rbsg.chat;

import javax.websocket.OnMessage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class Chat {

    public ArrayList<String> chatLog = new ArrayList<String>();
    public Date date = new Date();
    private String CHANNEL = "/chat?user=";
    public String userName;
    public String sendToUser;
    public String message;

    public Chat (String userName) {
        this.userName = userName;
    }

    public void setMessage(String message) {
        this.message = message;
        chatLog.add("\"" + userName + ": " + this.message + "\" gesendet am: " + new Date());
    }

    public void sendMessage() {
        // send Message (all)
    }

    public void sendMessage(String sendToUser){
        // send Message (private)
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

}
