package de.uniks.se1ss19teamb.rbsg.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class NotificationTest {

    // This Test class is for coverage purpose only

    private NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();
    private Logger logger = LogManager.getLogger();

    @Test
    public void sendErrorTest() {

        int[] test = new int[1];


        // Actual Test
        try {
            notificationHandler.setPopupController(null);
            System.out.println(test[2]);
        } catch (Exception e) {
            notificationHandler.sendError("Test ERROR NullPointerException", logger, e);
            notificationHandler.sendError("Test ERROR NullPointerException Without e Exception", logger);
            notificationHandler.sendWarning("Test WARNING", logger);
            notificationHandler.sendInfo("Test INFO", logger);
            notificationHandler.sendSuccess("Test SUCCESS", logger);
        }
    }
}
