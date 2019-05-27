package de.uniks.se1ss19teamb.rbsg.chat;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;

public class Chat {

  public static ArrayList<String> chatLog = new ArrayList<String>();
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

  public void writeLog(Path path) throws IOException {
    PrintWriter out = new PrintWriter(path.toString());
    out.println("Chat log seit dem " + date + "\n");
    for (String s : chatLog) {
      out.println(s);
    }
    out.close();
  }

}
