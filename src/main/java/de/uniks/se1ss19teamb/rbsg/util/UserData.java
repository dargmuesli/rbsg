package de.uniks.se1ss19teamb.rbsg.util;

public class UserData {

    private String userName;

    private String password;

    public UserData(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
