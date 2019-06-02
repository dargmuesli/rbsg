package de.uniks.se1ss19teamb.rbsg.chatencryption;

import java.io.IOException;

public class ChatSender extends Thread {
    String prompt;
    ChatCommunication chat;
    ChatMessage msg;

    public ChatSender(ChatCommunication chat, String prompt) {
        this.prompt = prompt;
        this.chat = chat;
    }

    public void run() {
        msg = new ChatMessage(chat.myPublicKey);
        try {
            chat.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            String input = "";
            try {
                input = chat.stdIn.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (input.equalsIgnoreCase("End Chat")) {
                chat.endChat();
                return;
            }

            msg = new ChatMessage(input);
            try {
                chat.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("<Sent at : ");
        }
    }
}
