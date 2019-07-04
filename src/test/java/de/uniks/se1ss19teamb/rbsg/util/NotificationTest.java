package de.uniks.se1ss19teamb.rbsg.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class NotificationTest {

    // This Test class is for coverage purpose only

    private Logger logger = LogManager.getLogger();

    @Test
    public void sendErrorTest() {

        int[] test = new int[1];


        // Actual Test
        try {
            NotificationHandler.getInstance().setPopupController(null);
            System.out.println(test[2]);
        } catch (Exception e) {
            NotificationHandler.getInstance().sendError("Test ERROR NullPointerException", logger, e);
            NotificationHandler.getInstance().sendError("Test ERROR NullPointerException Without e Exception", logger);
            NotificationHandler.getInstance().sendWarning("Test WARNING", logger);
            NotificationHandler.getInstance().sendInfo("Test INFO", logger);
            NotificationHandler.getInstance().sendSuccess("Test SUCCESS", logger);
        }
    }
}
