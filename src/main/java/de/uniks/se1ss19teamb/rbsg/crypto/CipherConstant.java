package de.uniks.se1ss19teamb.rbsg.crypto;

import de.uniks.se1ss19teamb.rbsg.chat.encryption.GenerateKeys;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


class CipherConstant {
    private static final Logger logger = LogManager.getLogger();

    private static GenerateKeys keys = new GenerateKeys();

    static PrivateKey privateKey;
    static PublicKey publicKey;

    static {
        try {
            keys.createKeys();
            publicKey = GenerateKeys.getPublicKey();
            privateKey = GenerateKeys.getPrivateKey();
        } catch (Exception e) {
            NotificationHandler.sendError("A key could not be read!", logger, e);
        }
    }
}
