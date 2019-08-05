package de.uniks.se1ss19teamb.rbsg.crypto;

import de.uniks.se1ss19teamb.rbsg.chat.encryption.GenerateKeys;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


class CipherConstant {
    private static final Logger logger = LogManager.getLogger();
    static PublicKey publicKey;
    static PrivateKey privateKey;
    static  GenerateKeys keys = new GenerateKeys();

    static {
        try {
            keys.createKeys();
            publicKey = keys.getPublicKey();
            privateKey = keys.getPrivateKey();
        } catch (Exception e) {
            NotificationHandler.sendError("A key could not be read!", logger, e);
        }
    }
}
