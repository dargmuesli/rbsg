package de.uniks.se1ss19teamb.rbsg.chatencryption;

import de.uniks.se1ss19teamb.rbsg.crypto.CipherController;

import java.io.*;
import java.net.Socket;

public class ChatCommunication {
    String type;
    String anotherPulbic;
    String myPublicKey;
    CipherController cp;
    String filename;

    ObjectInputStream in;
    ObjectOutputStream out;
    BufferedReader stdIn;
    Socket socket;

    public ChatCommunication(
        Socket echoSochet, ObjectInputStream in, ObjectOutputStream out, String type, String filename) {
        cp = new CipherController();
        this.type = type;
        this.socket = echoSochet;
        this.out = out;
        this.in = in;
        this.filename = filename;
        stdIn = new BufferedReader(new InputStreamReader(System.in));
    }

    public void sendMessage(ChatMessage inputString) {
        cp.encryptMessage(inputString.text, filename);
        anotherPulbic = GenerateKeys.getPublicKey().toString();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            String recoveredSecret = stringBuilder.toString();
            out.writeObject(recoveredSecret);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChatMessage receiveMessage() throws IOException, ClassNotFoundException {
        ChatMessage temp = (ChatMessage) in.readObject();

        if (type.equalsIgnoreCase("Client")) {
            System.out.println("Server: ");
        } else {
            System.out.print("Client");
        }
        System.out.print("Encrypted Message " + temp.text);
        temp.text = cp.decryptMessage(temp.text);
        System.out.println("Decrypted Message " + temp.text);
        return temp;
    }

    public void endChat() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
