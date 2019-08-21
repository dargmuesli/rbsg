package de.uniks.se1ss19teamb.rbsg.sockets;

import java.util.ArrayList;

public class UserKeys {

    static String currentUserKey;
    static String originalUserKey;

    public static void addUserKey(String userKey) {
        currentUserKey = userKey;
    }

    public static String getUserKey() {
        return currentUserKey;
    }

    public static void setBotUserKey(String botUserKey) {
        originalUserKey = currentUserKey;
        currentUserKey = botUserKey;
    }

    public static void revertUserKey() {
        currentUserKey = originalUserKey;
    }
}
