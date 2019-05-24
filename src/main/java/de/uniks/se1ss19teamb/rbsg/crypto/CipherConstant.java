package de.uniks.se1ss19teamb.rbsg.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;

public class CipherConstant {
    //Constant
    static PublicKey publicKey;
    static PrivateKey privateKey;

    static {
        try {
            CipherConnect.connect();
            publicKey = CipherUtils.readPublicKey("src/main/java/de/uniks/se1ss19teamb/rbsg/crypto/public.der");
            privateKey = CipherUtils.readPrivateKey("src/main/java/de/uniks/se1ss19teamb/rbsg/crypto/Dummy.der");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
