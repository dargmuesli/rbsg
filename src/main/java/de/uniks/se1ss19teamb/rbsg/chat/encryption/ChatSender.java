package de.uniks.se1ss19teamb.rbsg.chat.encryption;

import java.io.IOException;

public class ChatSender extends Thread {
    private String prompt;
    private ChatCommunication chat;
    private String msg;

    public ChatSender(ChatCommunication chat, String prompt) {
        this.prompt = prompt;
        this.chat = chat;
    }

    public void run() {
        msg = chat.myPublicKey;

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

            msg = input;

            try {
                chat.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("<Sent at : ");
        }
    }
}
