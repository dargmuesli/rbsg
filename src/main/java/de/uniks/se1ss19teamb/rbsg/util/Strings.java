package de.uniks.se1ss19teamb.rbsg.util;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.Logger;

public class Strings {
    public static boolean checkHas(JsonObject jsonObject, String key, Logger logger) {
        if (!jsonObject.has(key)) {
            NotificationHandler.getInstance()
                .sendError("There was unexpected data in JSON " + jsonObject.toString() + "!", logger,
                    new NoSuchFieldException("Json does not have an \"" + key + "\" key!"));
            return false;
        }

        return true;
    }
}
