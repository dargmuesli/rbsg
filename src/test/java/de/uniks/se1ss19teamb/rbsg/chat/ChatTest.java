package de.uniks.se1ss19teamb.rbsg.chat;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.crypto.CryptoTest;
import de.uniks.se1ss19teamb.rbsg.crypto.GenerateKeys;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatMessageHandler;
import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtil;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.*;
import org.apache.logging.log4j.Logger;
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
@PrepareForTest({Chat.class, LoginController.class, NotificationHandler.class, SerializeUtil.class})
@PowerMockIgnore({"javax.management.*", "javax.script.*", "javax.swing.*", "javax.crypto.*"})
public class ChatTest {

    @BeforeAll
    static void beforeAll() throws URISyntaxException, IOException {
        GenerateKeys.setPublicKey(Files.readAllBytes(Paths.get(
            CryptoTest.class.getResource("/de/uniks/se1ss19teamb/rbsg/crypto/public-key_myself.der").toURI())));
        GenerateKeys.setPrivateKey(Files.readAllBytes(Paths.get(
            CryptoTest.class.getResource("/de/uniks/se1ss19teamb/rbsg/crypto/private-key_myself.der").toURI())));
    }

    @Test
    public void defaultMessageHandlerTest() throws Exception {
        PowerMockito.spy(NotificationHandler.class);
        PowerMockito.doNothing().when(NotificationHandler.class, "send", anyString(), anyString());
        PowerMockito.spy(LoginController.class);
        Mockito.when(LoginController.getUserName())
            .thenReturn("myself");
        PowerMockito.spy(SerializeUtil.class);
        Mockito.when(SerializeUtil.getAppDataPath())
            .thenReturn(Paths.get(CryptoTest.class.getResource(
                "/de/uniks/se1ss19teamb/rbsg/crypto/private-key_myself.der").toURI()).getParent());

        String username = "test-username";
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("msg", "msg_value");
        responseJsonObject.addProperty("from", username);
        responseJsonObject.addProperty("message", "[tbe]C9Xa0Tq5tIxr/Eqv+RKtrjedhlVyWyHza1gIIzEYcb64qo"
            + "tiNJ4r/mWrDNz26R8ySyDNzDfbYejbRgbKwLUmQepXNzgbPStK1fyL/HH1RHVoqw4Q92ScOLiDquUVpSogFWA1hPNvMK5ZXM1/7zfE+u"
            + "5jb6JJWFpCwUhGU3R7TcruNgiztDJKw+hRnWF2HqZNwL4+Bjpm7QGpSTkEvZZBSwbD/p4FR/3FpSuPW+TjpFdmPL2YvcBwS2DFVLC8VV"
            + "XhUHozaGf2o1lmB30huqHVVlTSu8u7QPwPWyXSdPr4kpQU3R8banOOU00dl6HHgb0AHoVXYb/Mq1riNqBvgJ9g3g==");
        responseJsonObject.addProperty("channel", "private");
        List<ChatMessageHandler> chatMessageHandlers = new ArrayList<>();
        chatMessageHandlers.add((message, from, isPrivate, wasEncrypted) -> {
            if (!message.equals("g\u00E5 til helvete!!!")) {
                Assert.fail();
            }
        });

        Chat.defaultMessageHandler(responseJsonObject, false, username, chatMessageHandlers);
        PowerMockito.verifyStatic(NotificationHandler.class);
        NotificationHandler.sendWarning(anyString(), any());

        responseJsonObject.remove("msg");
        Chat.defaultMessageHandler(responseJsonObject, true, username, chatMessageHandlers);
        Chat.defaultMessageHandler(responseJsonObject, false, username, chatMessageHandlers);
    }

    @Test
    public void executeCommandsOnMessageTest() throws Exception {
        PowerMockito.spy(NotificationHandler.class);
        PowerMockito.doNothing().when(NotificationHandler.class, "send", anyString(), anyString());
        PowerMockito.spy(LoginController.class);
        Mockito.when(LoginController.getUserName())
            .thenReturn("myself");
        PowerMockito.spy(SerializeUtil.class);
        Mockito.when(SerializeUtil.getAppDataPath())
            .thenReturn(Paths.get(CryptoTest.class.getResource(
                "/de/uniks/se1ss19teamb/rbsg/crypto/private-key_myself.der").toURI()).getParent());
        PowerMockito.mockStatic(Files.class);
        Mockito.when(Files.write(any(Path.class), any(byte[].class))).then(invocation -> null);

        String receiver = "myself";
        File file = new File(CryptoTest.class.getResource(
            "/de/uniks/se1ss19teamb/rbsg/crypto/private-key_myself.der").toURI());

        // Mock the JFrame to prevent a HeadlessException on CI
        JFrame frameMock = mock(JFrame.class);
        JFileChooser fileChooserMock = mock(JFileChooser.class);
        whenNew(JFrame.class).withNoArguments().thenReturn(frameMock);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(fileChooserMock);
        when(fileChooserMock.showSaveDialog(any())).thenReturn(0);
        when(fileChooserMock.getSelectedFile()).thenReturn(file);

        when(fileChooserMock.getSelectedFile()).thenReturn(file);
        Optional<String> optional = Chat.executeCommandsOnMessage("/enc_export", receiver);
        Assert.assertEquals(Optional.empty(), optional);
        PowerMockito.verifyStatic(NotificationHandler.class);
        NotificationHandler.sendSuccess(eq("Public key exported successfully."), any(Logger.class));

        optional = Chat.executeCommandsOnMessage("/enc 123", receiver);
        Assert.assertTrue(optional.isPresent() && optional.get().startsWith("[tbe]"));
    }
}
