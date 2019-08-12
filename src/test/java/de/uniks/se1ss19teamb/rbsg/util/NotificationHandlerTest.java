package de.uniks.se1ss19teamb.rbsg.util;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NotificationHandler.class)
@PowerMockIgnore({"javax.management.*", "javax.script.*", "javax.swing.*"})
public class NotificationHandlerTest {
    private Exception e = new Exception();
    private Logger logger = mock(LogManager.getLogger().getClass());
    private String testMessage = "Test message.";

    @Test
    public void sendErrorTest() throws Exception {
        PowerMockito.spy(NotificationHandler.class);
        PowerMockito.doNothing().when(NotificationHandler.class, "send", anyString(), anyString());

        NotificationHandler.sendError(testMessage, logger);
        verify(logger).error(testMessage);

        NotificationHandler.sendError(testMessage, logger, e);
        verify(logger).error(testMessage, e);

        verifyPrivate(NotificationHandler.class, Mockito.times(2)).invoke("send", "ERROR", testMessage);
    }

    @Test
    public void sendInfoTest() throws Exception {
        PowerMockito.spy(NotificationHandler.class);
        PowerMockito.doNothing().when(NotificationHandler.class, "send", anyString(), anyString());

        NotificationHandler.sendInfo(testMessage, logger);
        verify(logger).info(testMessage);

        verifyPrivate(NotificationHandler.class).invoke("send", "INFO", testMessage);
    }

    @Test
    public void sendSuccessTest() throws Exception {
        PowerMockito.spy(NotificationHandler.class);
        PowerMockito.doNothing().when(NotificationHandler.class, "send", anyString(), anyString());

        NotificationHandler.sendSuccess(testMessage, logger);
        verify(logger).debug(testMessage);

        verifyPrivate(NotificationHandler.class).invoke("send", "SUCCESS", testMessage);
    }

    @Test
    public void sendWarningTest() throws Exception {
        PowerMockito.spy(NotificationHandler.class);
        PowerMockito.doNothing().when(NotificationHandler.class, "send", anyString(), anyString());

        NotificationHandler.sendWarning(testMessage, logger);
        verify(logger).warn(testMessage);

        verifyPrivate(NotificationHandler.class).invoke("send", "WARNING", testMessage);
    }
}
