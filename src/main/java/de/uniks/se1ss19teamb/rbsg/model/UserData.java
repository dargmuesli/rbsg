package de.uniks.se1ss19teamb.rbsg.model;

public class UserData {

    private String password;
    private boolean remember;
    private String userName;

    public UserData(String userName, String password, boolean remember) {
        this.password = password;
        this.remember = remember;
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean getRemember() {
        return remember;
    }

    public String getUserName() {
        return userName;
    }
}
