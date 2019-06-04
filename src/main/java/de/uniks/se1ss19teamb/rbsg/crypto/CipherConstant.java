package de.uniks.se1ss19teamb.rbsg.crypto;

import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PrivateKey;
import java.security.PublicKey;

public class CipherConstant {
    static PublicKey publicKey;
    static PrivateKey privateKey;
    private static final Logger logger = LogManager.getLogger(CipherConstant.class);
    private static ErrorHandler errorHandler = new ErrorHandler();
    static {
        try {
            publicKey = CipherUtils
                    .readPublicKey("src/main/resources/de/uniks/se1ss19teamb/rbsg/public.der");
            privateKey = CipherUtils
                    .readPrivateKey("src/main/resources/de/uniks/se1ss19teamb/rbsg/Dummy.der");
        } catch (Exception e) {
            errorHandler.sendError("Konnte nicht Key lesen, ueberpruefe Klasse CipherUtils");
            logger.error(e);
        }
    }
}
