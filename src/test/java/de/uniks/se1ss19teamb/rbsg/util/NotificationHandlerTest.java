package de.uniks.se1ss19teamb.rbsg.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.uniks.se1ss19teamb.rbsg.ui.NotificationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NotificationHandlerTest {
    private Exception e = new Exception();
    private Logger logger = mock(LogManager.getLogger().getClass());
    private NotificationHandler instance;
    private String errorMessage = "Test error!";

    @Before
    public void before() {
        this.instance = NotificationHandler.getInstance();
    }

    @Test
    public void getInstanceTest() {
        Assert.assertNotNull(this.instance);
        Assert.assertEquals(NotificationHandler.class, this.instance.getClass());
    }

    @Test
    public void getSetPopupController() {
        this.instance.setNotificationController(null);
        NotificationController notificationController = this.instance.getNotificationController();

        Assert.assertNull(notificationController);

        notificationController = new NotificationController();
        this.instance.setNotificationController(notificationController);

        Assert.assertEquals(notificationController, this.instance.getNotificationController());

        this.instance.setNotificationController(null);

        Assert.assertNull(this.instance.getNotificationController());
    }

    @Test
    public void sendErrorTest() {
        this.instance.sendError(errorMessage, logger);
        verify(logger).error(errorMessage);

        this.instance.sendError(errorMessage, logger, e);
        verify(logger).error(errorMessage, e);

        NotificationController notificationControllerMock = mock(NotificationController.class);
        this.instance.setNotificationController(notificationControllerMock);
        this.instance.sendError(errorMessage, logger);
        verify(notificationControllerMock).displayError(errorMessage);
    }

    @Test
    public void sendInfoTest() {
        this.instance.sendInfo(errorMessage, logger);
        verify(logger).info(errorMessage);

        NotificationController notificationControllerMock = mock(NotificationController.class);
        this.instance.setNotificationController(notificationControllerMock);
        this.instance.sendInfo(errorMessage, logger);
        verify(notificationControllerMock).displayInformation(errorMessage);
    }

    @Test
    public void sendSuccessTest() {
        this.instance.sendSuccess(errorMessage, logger);
        verify(logger).debug(errorMessage);

        NotificationController notificationControllerMock = mock(NotificationController.class);
        this.instance.setNotificationController(notificationControllerMock);
        this.instance.sendSuccess(errorMessage, logger);
        verify(notificationControllerMock).displaySuccess(errorMessage);
    }

    @Test
    public void sendWarningTest() {
        this.instance.sendWarning(errorMessage, logger);
        verify(logger).warn(errorMessage);

        NotificationController notificationControllerMock = mock(NotificationController.class);
        this.instance.setNotificationController(notificationControllerMock);
        this.instance.sendWarning(errorMessage, logger);
        verify(notificationControllerMock).displayWarning(errorMessage);
    }
}
