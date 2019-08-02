package de.uniks.se1ss19teamb.rbsg.util;

import de.uniks.se1ss19teamb.rbsg.ui.PopupController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        PopupController popupController = this.instance.getPopupController();

        Assert.assertNull(popupController);

        popupController = new PopupController();
        this.instance.setPopupController(popupController);

        Assert.assertEquals(popupController, this.instance.getPopupController());

        this.instance.setPopupController(null);

        Assert.assertNull(this.instance.getPopupController());
    }

    @Test
    public void sendErrorTest() {
        this.instance.sendError(errorMessage, logger);
        verify(logger).error(errorMessage);

        this.instance.sendError(errorMessage, logger, e);
        verify(logger).error(errorMessage, e);

        PopupController popupControllerMock = mock(PopupController.class);
        this.instance.setPopupController(popupControllerMock);
        this.instance.sendError(errorMessage, logger);
        verify(popupControllerMock).displayError(errorMessage);
    }

    @Test
    public void sendInfoTest() {
        this.instance.sendInfo(errorMessage, logger);
        verify(logger).info(errorMessage);

        PopupController popupControllerMock = mock(PopupController.class);
        this.instance.setPopupController(popupControllerMock);
        this.instance.sendInfo(errorMessage, logger);
        verify(popupControllerMock).displayInformation(errorMessage);
    }

    @Test
    public void sendSuccessTest() {
        this.instance.sendSuccess(errorMessage, logger);
        verify(logger).debug(errorMessage);

        PopupController popupControllerMock = mock(PopupController.class);
        this.instance.setPopupController(popupControllerMock);
        this.instance.sendSuccess(errorMessage, logger);
        verify(popupControllerMock).displaySuccess(errorMessage);
    }

    @Test
    public void sendWarningTest() {
        this.instance.sendWarning(errorMessage, logger);
        verify(logger).warn(errorMessage);

        PopupController popupControllerMock = mock(PopupController.class);
        this.instance.setPopupController(popupControllerMock);
        this.instance.sendWarning(errorMessage, logger);
        verify(popupControllerMock).displayWarning(errorMessage);
    }
}
