package de.uniks.se1ss19teamb.rbsg.model;

import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserData {

    public static final Path USER_DATA_PATH =
        Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "rbsg_user-data.json");

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

    public static UserData loadUserData(ErrorHandler errorHandler) {
        if (!UserData.USER_DATA_PATH.toFile().exists()) {
            errorHandler.sendError("User data doesn't exist!");
            return null;
        }

        return SerializeUtils.deserialize(UserData.USER_DATA_PATH.toFile(), UserData.class);
    }

    public static void deleteUserData(ErrorHandler errorHandler) {
        File userDataFile = UserData.USER_DATA_PATH.toFile();

        if (userDataFile.exists()) {
            if (!userDataFile.delete()) {
                errorHandler.sendError("User data file could not be deleted!");
            }
        }
    }
}
