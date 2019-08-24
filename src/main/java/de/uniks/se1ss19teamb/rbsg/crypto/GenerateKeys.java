package de.uniks.se1ss19teamb.rbsg.crypto;

import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;
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

    static PrivateKey getPrivateKey() {
        init();
        return privateKey;
    }

    private static void init() {
        String sanitizedUsername = SerializeUtil.sanitizeFilename(LoginController.getUserName());
        Path privateKeyPath = SerializeUtil.getAppDataPath().resolve("private-key_" + sanitizedUsername + ".der");
        Path publicKeyPath = SerializeUtil.getAppDataPath().resolve("public-key_" + sanitizedUsername + ".der");

        if (publicKey == null && privateKey == null) {
            if (Files.exists(privateKeyPath) && Files.exists(publicKeyPath)) {
                try {
                    Optional<PrivateKey> optionalPrivateKey = readPrivateKey(Files.readAllBytes(privateKeyPath));
                    Optional<PublicKey> optionalPublicKey = readPublicKey(Files.readAllBytes(publicKeyPath));

                    if (optionalPrivateKey.isPresent() && optionalPublicKey.isPresent()) {
                        privateKey = optionalPrivateKey.get();
                        publicKey = optionalPublicKey.get();
                    }
                } catch (IOException e) {
                    NotificationHandler.sendError("Could not save crypto keys to file!", LogManager.getLogger(), e);
                }
            } else {
                createKeys();

                try {
                    Files.write(privateKeyPath, privateKey.getEncoded());
                    Files.write(publicKeyPath, publicKey.getEncoded());
                } catch (IOException e) {
                    NotificationHandler.sendError("Could not save crypto keys to file!", LogManager.getLogger(), e);
                }
            }
        } else if (publicKey == null || privateKey == null) {
            throw new IllegalStateException("Only one key is set!");
        }
    }

    private static Optional<PrivateKey> readPrivateKey(byte[] privateKey) {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return Optional.of(kf.generatePrivate(spec));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static Optional<PublicKey> readPublicKey(byte[] publicKey) {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return Optional.of(kf.generatePublic(spec));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<PublicKey> readPublicKey(String username) {
        Path publicKeyPath = SerializeUtil.getAppDataPath().resolve(
            "public-key_" + SerializeUtil.sanitizeFilename(username) + ".der");

        if (Files.exists(publicKeyPath)) {
            try {
                return readPublicKey(Files.readAllBytes(publicKeyPath));
            } catch (IOException e) {
                NotificationHandler.sendError(
                    "Could not read " + username + "'s public key!", LogManager.getLogger(), e);
                return Optional.empty();
            }
        } else {
            if (LoginController.getUserName().equals(username)) {
                init();
                return Optional.of(publicKey);
            } else {
                NotificationHandler.sendError("The user's public key does not exist locally!", LogManager.getLogger());
                return Optional.empty();
            }
        }
    }

    public static void setPrivateKey(byte[] privateKeyBytes) {
        readPrivateKey(privateKeyBytes).ifPresent(GenerateKeys::setPrivateKey);
    }

    private static void setPrivateKey(PrivateKey privateKey) {
        GenerateKeys.privateKey = privateKey;
    }

    public static void setPublicKey(byte[] publicKeyBytes) {
        readPublicKey(publicKeyBytes).ifPresent(GenerateKeys::setPublicKey);
    }

    private static void setPublicKey(PublicKey publicKey) {
        GenerateKeys.publicKey = publicKey;
    }
}
