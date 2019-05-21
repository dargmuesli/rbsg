package de.uniks.se1ss19teamb.rbsg.crypto;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class CipherConstant {
    //Constant
    static PublicKey publicKey;
    static PrivateKey privateKey;

    static {
        try {
            publicKey = CipherUtils.readPublicKey("src/main/java/de/uniks/se1ss19teamb/rbsg/crypto/public.der");
            privateKey = CipherUtils.readPrivateKey("src/main/java/de/uniks/se1ss19teamb/rbsg/crypto/private.der");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
