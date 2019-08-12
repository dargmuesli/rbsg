package de.uniks.se1ss19teamb.rbsg.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NotificationHandler.class)
@PowerMockIgnore({"javax.management.*", "javax.script.*", "javax.swing.*"})
public class StringUtilTest {
    private JsonObject hasObject = new JsonObject();
    private JsonObject hasStatusObject = new JsonObject();
    private Logger logger = mock(Logger.class);

    @Before
    public void before() {
        hasObject.addProperty("key", "value");
        hasStatusObject.addProperty("status", "look");
        hasStatusObject.addProperty("message", "this is what happened.");
    }

    @Test
    public void checkHasNotTrueTest() {
        PowerMockito.mockStatic(NotificationHandler.class);

        Assert.assertTrue(StringUtil.checkHasNot(new JsonObject(), "key", logger));

        verifyStatic(NotificationHandler.class);
        NotificationHandler.sendError(eq("There was unexpected data!"), eq(logger), any());
    }

    @Test
    public void checkHasNotFalseTest() {
        PowerMockito.mockStatic(NotificationHandler.class);

        Assert.assertTrue(StringUtil.checkHasNot(hasStatusObject, "key", logger));

        verifyStatic(NotificationHandler.class);
        NotificationHandler.sendError("look: this is what happened.", logger);
    }
}
