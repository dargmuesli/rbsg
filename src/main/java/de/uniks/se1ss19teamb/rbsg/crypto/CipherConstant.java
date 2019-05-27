package de.uniks.se1ss19teamb.rbsg.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;

public class CipherConstant {
    static PublicKey publicKey;
    static PrivateKey privateKey;

    static {
        try {
            publicKey = CipherUtils.readPublicKey("src/main/resources/de/uniks/se1ss19teamb/rbsg/public.der");
            privateKey = CipherUtils.readPrivateKey("src/main/resources/de/uniks/se1ss19teamb/rbsg/Dummy.der");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
