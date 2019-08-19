package de.uniks.se1ss19teamb.rbsg.crypto;

import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import java.nio.charset.StandardCharsets;
import java.security.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;

public class CipherUtils {

    private static byte[] encrypt(byte[] plaintext) throws NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, GenerateKeys.getPrivateKey());

        return cipher.doFinal(plaintext);
    }

    public static String encryptMessage(String message) {
        try {
            return Base64.encodeBase64String(
                CipherUtils.encrypt(
                    message.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException
            | BadPaddingException | IllegalBlockSizeException e) {
            NotificationHandler.sendError("Encryption of a message failed!", LogManager.getLogger(), e);
            return null;
        }
    }

    private static byte[] decrypt(byte[] ciphertext) throws
        NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
        BadPaddingException {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, GenerateKeys.getPublicKey());

        return cipher.doFinal(ciphertext);
    }

    public static String decryptMessage(String message) {
        try {
            return new String(
                CipherUtils.decrypt(
                    Base64.decodeBase64(message)),
                StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException
            | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            NotificationHandler.sendError("Decryption of a message failed!", LogManager.getLogger(), e);
            return null;
        }
    }
}
