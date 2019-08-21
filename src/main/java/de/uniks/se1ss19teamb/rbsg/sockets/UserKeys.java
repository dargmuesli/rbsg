package de.uniks.se1ss19teamb.rbsg.sockets;

import java.util.ArrayList;

public class UserKeys {

    ArrayList<String> userKeys = new ArrayList<>();

    void addUserKey(int position, String userKey) {
        userKeys.add(position, userKey);
    }

    String getUserKey(int position) {
        return userKeys.get(position);
    }
}
