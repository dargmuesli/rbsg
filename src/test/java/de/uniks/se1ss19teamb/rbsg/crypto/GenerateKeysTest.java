package de.uniks.se1ss19teamb.rbsg.crypto;

import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtil;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;
import org.apache.logging.log4j.LogManager;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GenerateKeys.class, LoginController.class, SerializeUtil.class})
@PowerMockIgnore({"javax.management.*", "javax.script.*", "javax.swing.*", "javax.crypto.*"})
public class GenerateKeysTest {

    @Test
    @Ignore
    public void getterTest() throws Exception {
        PowerMockito.spy(LoginController.class);
        Mockito.when(LoginController.getUserName())
            .thenReturn("myself");
        PowerMockito.spy(SerializeUtil.class);
        Path privateKeyPath = Paths.get(CryptoTest.class.getResource(
            "/de/uniks/se1ss19teamb/rbsg/crypto/private-key_myself.der").toURI());
        Mockito.when(SerializeUtil.getAppDataPath())
            .thenReturn(privateKeyPath.getParent());

        Assert.assertEquals(
            KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(privateKeyPath))),
            GenerateKeys.getPrivateKey());
    }

    @Test
    public void setterTest() throws URISyntaxException {
        PowerMockito.spy(LoginController.class);
        Mockito.when(LoginController.getUserName())
            .thenReturn("myself");
        PowerMockito.spy(SerializeUtil.class);
        Path privateKeyPath = Paths.get(CryptoTest.class.getResource(
            "/de/uniks/se1ss19teamb/rbsg/crypto/private-key_myself.der").toURI());
        Mockito.when(SerializeUtil.getAppDataPath())
            .thenReturn(privateKeyPath.getParent());

        KeyPairGenerator kpg;

        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            NotificationHandler.sendError("Key pair generation failed!", LogManager.getLogger(), e);
            return;
        }

        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        GenerateKeys.setPrivateKey(kp.getPrivate().getEncoded());
        GenerateKeys.setPublicKey(kp.getPublic().getEncoded());

        Assert.assertEquals(kp.getPrivate(), GenerateKeys.getPrivateKey());
    }

    @Test
    public void readPublicKeyTest() throws URISyntaxException {
        PowerMockito.spy(LoginController.class);
        Mockito.when(LoginController.getUserName())
            .thenReturn("myself");
        PowerMockito.spy(SerializeUtil.class);
        Path privateKeyPath = Paths.get(CryptoTest.class.getResource(
            "/de/uniks/se1ss19teamb/rbsg/crypto/private-key_myself.der").toURI());
        Mockito.when(SerializeUtil.getAppDataPath())
            .thenReturn(privateKeyPath.getParent());

        Assert.assertNotNull(GenerateKeys.readPublicKey("myself"));
    }
}
