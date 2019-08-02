package de.uniks.se1ss19teamb.rbsg.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import de.uniks.se1ss19teamb.rbsg.ui.PopupController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringUtilTest {
    private JsonObject hasObject = new JsonObject();
    private JsonObject hasStatusObject = new JsonObject();
    private Logger logger = mock(LogManager.getLogger().getClass());
    private PopupController popupControllerMock = mock(PopupController.class);

    @Before
    public void before() {
        hasObject.addProperty("key", "value");
        hasStatusObject.addProperty("status", "look");
        hasStatusObject.addProperty("message", "this is what happened.");
        NotificationHandler.getInstance().setPopupController(popupControllerMock);
    }

    @Test
    public void checkHasNotTest() {
        Assert.assertTrue(StringUtil.checkHasNot(new JsonObject(), "key", logger));
        verify(popupControllerMock).displayError("There was unexpected data!");

        Assert.assertFalse(StringUtil.checkHasNot(hasObject, "key", LogManager.getLogger()));

        Assert.assertTrue(StringUtil.checkHasNot(hasStatusObject, "key", LogManager.getLogger()));
        verify(popupControllerMock).displayError("look: this is what happened.");
    }
}
