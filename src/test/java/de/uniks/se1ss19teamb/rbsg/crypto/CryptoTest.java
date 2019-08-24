package de.uniks.se1ss19teamb.rbsg.crypto;

import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GenerateKeys.class, LoginController.class, SerializeUtil.class})
@PowerMockIgnore({"javax.management.*", "javax.script.*", "javax.swing.*", "javax.crypto.*"})
public class CryptoTest {

    @BeforeAll
    static void beforeAll() throws IOException, URISyntaxException {
        GenerateKeys.setPublicKey(Files.readAllBytes(Paths.get(
            CryptoTest.class.getResource("/de/uniks/se1ss19teamb/rbsg/crypto/public-key_myself.der").toURI())));
        GenerateKeys.setPrivateKey(Files.readAllBytes(Paths.get(
            CryptoTest.class.getResource("/de/uniks/se1ss19teamb/rbsg/crypto/private-key_myself.der").toURI())));
    }

    @Test
    public void enDecryptTest() throws Exception {
        PowerMockito.spy(GenerateKeys.class);
        PowerMockito.spy(LoginController.class);
        PowerMockito.spy(SerializeUtil.class);
        Mockito.when(LoginController.getUserName())
            .thenReturn("myself");
        Mockito.when(SerializeUtil.getAppDataPath())
            .thenReturn(Paths.get(CryptoTest.class.getResource(
                "/de/uniks/se1ss19teamb/rbsg/crypto/private-key_myself.der").toURI()).getParent());

        String message = "g\u00E5 til helvete!!!";
        String encryptedMessage = CipherUtils.encryptMessage(message, "myself");

        Assert.assertNotEquals(message, encryptedMessage);
        Assert.assertEquals(message, CipherUtils.decryptMessage(encryptedMessage));
    }
}
