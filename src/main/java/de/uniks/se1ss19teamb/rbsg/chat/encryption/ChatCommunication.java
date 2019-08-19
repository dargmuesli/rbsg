package de.uniks.se1ss19teamb.rbsg.chat.encryption;

import de.uniks.se1ss19teamb.rbsg.crypto.CipherController;

import java.io.*;
import java.net.Socket;

public class ChatCommunication {

    String anotherPublic;
    String myPublicKey;
    BufferedReader stdIn;

    private CipherController cp;
    private String privateKeyFilename;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private String type;

    public ChatCommunication(
        Socket echoSochet, ObjectInputStream in, ObjectOutputStream out, String type, String privateKeyFilename) {

        this.type = type;
        this.socket = echoSochet;
        this.out = out;
        this.in = in;
        this.privateKeyFilename = privateKeyFilename;

        cp = new CipherController();
        stdIn = new BufferedReader(new InputStreamReader(System.in));
    }

    public void sendMessage(String inputString) {
        cp.encryptMessage(inputString, privateKeyFilename);
        anotherPublic = GenerateKeys.getPublicKey().toString();

        try {
            BufferedReader br = new BufferedReader(new FileReader(privateKeyFilename));
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

    String receiveMessage() throws IOException, ClassNotFoundException {
        String encryptedMessage = (String) in.readObject();
        String decryptedMessage = cp.decryptMessage(encryptedMessage);

        if (type.equalsIgnoreCase("Client")) {
            System.out.println("Server: ");
        } else {
            System.out.print("Client");
        }

        System.out.print("Encrypted Message " + encryptedMessage);
        System.out.println("Decrypted Message " + decryptedMessage);

        return encryptedMessage;
    }

    void endChat() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
