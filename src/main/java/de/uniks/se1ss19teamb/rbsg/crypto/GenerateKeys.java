package de.uniks.se1ss19teamb.rbsg.crypto;

import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.logging.log4j.LogManager;

public class GenerateKeys {

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    private static void createKeys() {
        KeyPairGenerator kpg;

        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            NotificationHandler.sendError("Key pair generation failed!", LogManager.getLogger(), e);
            return;
        }

        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
    }

    private static void init() {
        if (publicKey == null && privateKey == null) {
            createKeys();

            try {
                String sanitizedUsername = SerializeUtil.sanitizeFilename(LoginController.getUserName());

                Files.write(SerializeUtil.getAppDataPath()
                    .resolve("private-key_" + sanitizedUsername + ".der"), privateKey.getEncoded());
                Files.write(SerializeUtil.getAppDataPath()
                    .resolve("public-key_" + sanitizedUsername + ".der"), publicKey.getEncoded());
            } catch (IOException e) {
                NotificationHandler.sendError("Could not save crypto keys to file!", LogManager.getLogger(), e);
            }
        } else if (publicKey == null || privateKey == null) {
            throw new IllegalStateException("Only one key is set!");
        }
    }

    static PublicKey getPublicKey() {
        init();
        return publicKey;
    }

    public static void setPublicKey(byte[] publicKey) {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            setPublicKey(kf.generatePublic(spec));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private static void setPublicKey(PublicKey publicKey) {
        GenerateKeys.publicKey = publicKey;
    }

    static PrivateKey getPrivateKey() {
        init();
        return privateKey;
    }

    public static void setPrivateKey(byte[] privateKey) {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            setPrivateKey(kf.generatePrivate(spec));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private static void setPrivateKey(PrivateKey privateKey) {
        GenerateKeys.privateKey = privateKey;
    }
}
