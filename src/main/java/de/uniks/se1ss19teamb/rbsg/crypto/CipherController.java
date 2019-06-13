package de.uniks.se1ss19teamb.rbsg.crypto;

import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class CipherController {
    private static final Logger logger = LogManager.getLogger();

    private NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();
    //This string needs to be related with data

    public void encryptMessage(String msg, String filename) {
        try {
            //Turns the desired message into a byte Array
            byte[] message = msg.getBytes(StandardCharsets.UTF_8);

            //2.Encrypts the desired message
            byte[] secret = CipherUtils.encrypt(CipherConstant.publicKey, message);

            //4.Stores the secret message into text file
            FileWriter fw = new FileWriter(filename);
            fw.write(Base64.encodeBase64String(secret));
            fw.close();

        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException
            | BadPaddingException | IllegalBlockSizeException | IOException e) {
            notificationHandler.sendError("Fehler beim Verschlüsseln einer Nachricht!", logger, e);
        }
    }

    //decrypt the message
    public String decryptMessage(String filename) {

        String decryptedMessage = "";

        //1.Reads the encrypted message
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            //2.Constructs the encrypted message
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            String recoveredSecret = stringBuilder.toString();

            byte[] recSecret = Base64.decodeBase64(recoveredSecret);

            //4.Decrypts the message
            byte[] recoveredMessage = CipherUtils.decrypt(CipherConstant.privateKey, recSecret);
            decryptedMessage = new String(recoveredMessage, StandardCharsets.UTF_8);
            br.close();

        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException
            | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            notificationHandler.sendError("Fehler beim Entschlüsseln einer Nachricht!", logger, e);
        }
        return decryptedMessage;
    }

}
