package de.uniks.se1ss19teamb.rbsg.model;

import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserData {

    private static final Logger logger = LogManager.getLogger();
    public static final Path USER_DATA_PATH =
        Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "rbsg_user-data.json");

    private String loginPassword = "";
    private boolean loginRemember = false;
    private String loginUserName = "";

    private String registerPassword = "";
    private String registerPasswordRepeat = "";
    private String registerUsername = "";

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public boolean isLoginRemember() {
        return loginRemember;
    }

    public void setLoginRemember(boolean loginRemember) {
        this.loginRemember = loginRemember;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
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

    public static UserData loadUserData(NotificationHandler notificationHandler) {
        if (!UserData.USER_DATA_PATH.toFile().exists()) {
            if (notificationHandler != null) {
                notificationHandler.sendError("User data doesn't exist!", logger);
            }

            return null;
        }

        return SerializeUtils.deserialize(UserData.USER_DATA_PATH.toFile(), UserData.class);
    }

    public static void deleteUserData(NotificationHandler notificationHandler) {
        File userDataFile = UserData.USER_DATA_PATH.toFile();

        if (userDataFile.exists()) {
            if (!userDataFile.delete()) {
                notificationHandler.sendError("User data file could not be deleted!", logger);
            }
        }
    }
}
