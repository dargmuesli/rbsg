package de.uniks.se1ss19teamb.rbsg.util;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.Logger;

public class StringUtil {
    public static final String DISCARD_CONFIRMATION
        = "You did not save yet! Click again to confirm you want discard the changes.";

    public static boolean checkHasNot(JsonObject jsonObject, String key, Logger logger) {
        if (!jsonObject.has(key)) {
            if (jsonObject.has("status") && jsonObject.has("message")) {
                NotificationHandler.sendError(jsonObject.get("status").getAsString() + ": "
                    + jsonObject.get("message").getAsString(), logger);
            } else {
                NotificationHandler.sendError("There was unexpected data!", logger,
                        new NoSuchFieldException("Json does not have an \"" + key + "\" key!"));
            }

            return true;
        }

        return false;
    }
}
