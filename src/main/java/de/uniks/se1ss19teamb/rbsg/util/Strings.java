package de.uniks.se1ss19teamb.rbsg.util;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.Logger;

public class Strings {
    public static boolean checkHas(JsonObject jsonObject, String key, Logger logger) {
        if (!jsonObject.has(key)) {
            if (jsonObject.has("status") && jsonObject.has("message")) {
                NotificationHandler.getInstance().sendError(jsonObject.get("status").getAsString() + ": "
                    + jsonObject.get("message").getAsString(), logger);
            } else {
                NotificationHandler.getInstance()
                    .sendError("There was unexpected data!", logger,
                        new NoSuchFieldException("Json does not have an \"" + key + "\" key!"));
            }
            return false;
        }

        return true;
    }
}
