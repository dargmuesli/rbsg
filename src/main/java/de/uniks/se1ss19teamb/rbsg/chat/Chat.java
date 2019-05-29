package de.uniks.se1ss19teamb.rbsg.chat;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;

public class Chat {

  private String CHANNEL = "/chat?user=";
  ChatLogEntry chatLogEntry = new ChatLogEntry();
  public String userName;
  public String sendToUser;
  public String message;

  public Chat (String userName) {
        this.userName = userName;
        chatLogEntry.sender = userName;
    }

  public void setMessage (String message) {
    this.message = message;
    chatLogEntry.message = message;
    chatLogEntry.addToChatLog(this.userName, message);
  }

  public void sendMessage() {
    // send Message (all)
  }

  public void sendMessage(String sendToUser){
    // send Message (private)
  }

  public void writeLog(Path path) throws IOException {
    chatLogEntry.writeLog(path);
  }

  public static class ChatLogEntry{

    public static ArrayList<String> chatLog = new ArrayList<String>();
    public Date date = new Date();
    public String sender;
    public String message;


    public ChatLogEntry() {}

    public void addToChatLog(String sender, String message){
      chatLog.add(sender + " sagte: \"" + message + "\" " + "am " + new Date());
    }

    public void writeLog (Path path) throws IOException {
      PrintWriter out = new PrintWriter(path.toString());
      out.println("Chat log seit: " + date + "\n");
      for (String s : chatLog) {
        out.println(s);
      }
      out.close();
    }

  }

}
