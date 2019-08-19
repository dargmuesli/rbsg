package de.uniks.se1ss19teamb.rbsg.chat.encryption;

import java.io.IOException;

public class ChatReceiver extends Thread {

    private ChatCommunication chat;
    private String prompt;
    private String serverAdd;

    public ChatReceiver(ChatCommunication chat, String prompt, String serverAdd) {
        this.chat = chat;
        this.serverAdd = serverAdd;
        this.prompt = prompt;
    }

    public void run() {
        String received;
        int count = 0;
        String publicKey;
        try {
            while ((received = chat.receiveMessage()) != null) {
                if (count == 0) {
                    publicKey = received;
                    chat.anotherPublic = publicKey;
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
