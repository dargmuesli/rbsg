package de.uniks.se1ss19teamb.rbsg.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class CipherUtils {

    public static byte[] readFileBytes(String filename) throws IOException{
        Path path =  Paths.get(filename);
        return Files.readAllBytes(path);
    }

    //read Strings and generates public key for the given Strings
    public static PublicKey readPublicKey(String filename) throws InvalidKeySpecException, NoSuchAlgorithmException,IOException{
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(readFileBytes(filename));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicSpec);
    }

    //reads Strings and generates Private key for the given Strings
    public static PrivateKey readPrivateKey(String filename) throws IOException,NoSuchAlgorithmException,InvalidKeySpecException{
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(readFileBytes(filename));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    //encrypts using public key and returns as Byte
    public static byte[] encrypt(PublicKey key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,key);
        return cipher.doFinal(plaintext);
    }

    //decrypts the key using privatekey and returns as Byte
    public static byte[] decrypt(PrivateKey key,byte[] ciphertext) throws NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,BadPaddingException{
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE,key);
        return cipher.doFinal(ciphertext);
    }
}
