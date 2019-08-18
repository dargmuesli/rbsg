package de.uniks.se1ss19teamb.rbsg.crypto;

import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


class CipherConstant {
    private static final Logger logger = LogManager.getLogger();
    static PublicKey publicKey;
    static PrivateKey privateKey;

    static {
        try {
            publicKey = CipherUtils
                .readPublicKey("src/main/resources/de/uniks/se1ss19teamb/rbsg/public.der");
            privateKey = CipherUtils
                .readPrivateKey("src/main/resources/de/uniks/se1ss19teamb/rbsg/Dummy.der");
        } catch (Exception e) {
            NotificationHandler.sendError("A key could not be read!", logger, e);
        }
    }
}
