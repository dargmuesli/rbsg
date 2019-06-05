package de.uniks.se1ss19teamb.rbsg.chat.encryption;

import java.io.IOException;

public class ChatReciever extends Thread {
    String prompt;
    ChatCommunication chat;
    String serverAdd;

    public ChatReciever(ChatCommunication chat, String prompt, String serverAdd) {
        this.chat = chat;
        this.serverAdd = serverAdd;
        this.prompt = prompt;
    }

    public void run() {
        ChatMessage recieved;
        int count = 0;
        String publicKey;
        try {
            while ((recieved = chat.receiveMessage()) != null) {
                if (count == 0) {
                    publicKey = recieved.text;
                    chat.anotherPulbic = publicKey;
                } else if (count == 1) {
                    if (!(prompt.equalsIgnoreCase("Client : "))) {
                        System.out.println("Connected to Server: " + serverAdd
                            + "\nStreams Established. Ready to Chat .. ");
                    } else {
                        System.out.println("Keys Exchanged");

                    }
                } else {
                    System.out.println("This message was sent at : ");
                    System.out.println("This message was recieved at : ");
                }
                count++;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
