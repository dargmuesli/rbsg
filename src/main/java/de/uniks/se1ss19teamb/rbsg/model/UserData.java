package de.uniks.se1ss19teamb.rbsg.model;

public class UserData {

    private String loginPassword;
    private boolean loginRemember;
    private String loginUserName;

    private String registerPassword;
    private String registerPasswordRepeat;
    private String registerUsername;

    // contains only data from login screen, because this screen is always shown and exited first
    public UserData(String loginUserName, String loginPassword, boolean loginRemember) {
        this.loginPassword = loginPassword;
        this.loginRemember = loginRemember;
        this.loginUserName = loginUserName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public boolean getLoginRemember() {
        return loginRemember;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public String getRegisterPassword() {
        return registerPassword;
    }

    public void setRegisterPassword(String registerPassword) {
        this.registerPassword = registerPassword;
    }

    public String getRegisterPasswordRepeat() {
        return registerPasswordRepeat;
    }

    public void setRegisterPasswordRepeat(String registerPasswordRepeat) {
        this.registerPasswordRepeat = registerPasswordRepeat;
    }

    public String getRegisterUsername() {
        return registerUsername;
    }

    public void setRegisterUsername(String registerUsername) {
        this.registerUsername = registerUsername;
    }
}
