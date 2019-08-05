package de.uniks.se1ss19teamb.rbsg.chat.encryption;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class GenerateKeys {

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    public void createKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }

    public static PrivateKey getPrivateKey() {
        return privateKey;
    }
}



