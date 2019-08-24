package de.uniks.se1ss19teamb.rbsg.crypto;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CryptoTest {

    @BeforeAll
    static void beforeAll() throws IOException, URISyntaxException {
        GenerateKeys.setPublicKey(Files.readAllBytes(Paths.get(
            CryptoTest.class.getResource("/de/uniks/se1ss19teamb/rbsg/crypto/public-key.der").toURI())));
        GenerateKeys.setPrivateKey(Files.readAllBytes(Paths.get(
            CryptoTest.class.getResource("/de/uniks/se1ss19teamb/rbsg/crypto/private-key.der").toURI())));
    }

    @Test
    void enDecryptTest() {
        String message = "g\u00E5 til helvete!!!";
        String encryptedMessage = CipherUtils.encryptMessage(message, "myself");

        Assert.assertNotEquals(message, encryptedMessage);
        Assert.assertEquals(message, CipherUtils.decryptMessage(encryptedMessage));
    }
}
