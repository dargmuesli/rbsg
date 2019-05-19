package de.uniks.se1ss19teamb.rbsg.Serialize;

import com.google.gson.Gson;

/* TODO
 *   complete Serializatioon test for all the Objects in the game */

public class SerializeUtils {

    private static Object deserialization(String jsonString, Object obj) {
        Gson gson = new Gson();
        Object fromJson = gson.fromJson(jsonString, obj.getClass());
        return fromJson;
    }

    private static String serialization(Object obj) {
        Gson gson = new Gson();
        String toJson = gson.toJson(obj);
        return toJson;
    }
}
