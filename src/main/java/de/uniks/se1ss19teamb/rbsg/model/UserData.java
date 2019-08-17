package de.uniks.se1ss19teamb.rbsg.model;

import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtil;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A model for login and registration usernames and passwords.
 */
public class UserData {

    /**
     * The default path to save the user's data to: {@code <tmp-dir>/rbsg_user-data.json}.
     */
    public static final Path USER_DATA_PATH =
        Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "rbsg_user-data.json");
    private static final Logger logger = LogManager.getLogger();
    private String loginPassword = "";
    private boolean loginRemember = false;
    private String loginUsername = "";

    private String registrationPassword = "";
    private String registrationPasswordRepeat = "";
    private String registrationUsername = "";

    /**
     * Checks the default user data file's existence and returns its deserialization.
     *
     * @return The deserialized user data file.
     */
    public static UserData loadUserData() {
        if (!UserData.USER_DATA_PATH.toFile().exists()) {
            Platform.runLater(() -> logger.info("User data doesn't exist!"));
            return null;
        }

        return SerializeUtil.deserialize(UserData.USER_DATA_PATH.toFile(), UserData.class);
    }

    /**
     * Deletes the default user data file.
     */
    public static void deleteUserData() {
        File userDataFile = UserData.USER_DATA_PATH.toFile();

        if (userDataFile.exists() && !userDataFile.delete()) {
            Platform.runLater(() -> NotificationHandler.sendError("User data file could not be deleted!", logger));
        }
    }

    /**
     * Standard getter.
     *
     * @return The user data's login password.
     */
    public String getLoginPassword() {
        return loginPassword;
    }

    /**
     * Standard setter.
     *
     * @param loginPassword The user data's login password.
     */
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    /**
     * Standard checker.
     *
     * @return The user data's remember data boolean.
     */
    public boolean isLoginRemember() {
        return loginRemember;
    }

    /**
     * Standard setter.
     *
     * @param loginRemember The user data's remember data boolean.
     */
    public void setLoginRemember(boolean loginRemember) {
        this.loginRemember = loginRemember;
    }

    /**
     * Standard getter.
     *
     * @return The user data's login username.
     */
    public String getLoginUsername() {
        return loginUsername;
    }

    /**
     * Standard setter.
     *
     * @param loginUsername The user data's login username.
     */
    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    /**
     * Standard getter.
     *
     * @return The user data's registration password.
     */
    public String getRegistrationPassword() {
        return registrationPassword;
    }

    /**
     * Standard setter.
     *
     * @param registrationPassword The user data's registration password.
     */
    public void setRegistrationPassword(String registrationPassword) {
        this.registrationPassword = registrationPassword;
    }

    /**
     * Standard getter.
     *
     * @return The user data's registration password repetition.
     */
    public String getRegistrationPasswordRepeat() {
        return registrationPasswordRepeat;
    }

    /**
     * Standard setter.
     *
     * @param registrationPasswordRepeat The user data's registration password repetition.
     */
    public void setRegistrationPasswordRepeat(String registrationPasswordRepeat) {
        this.registrationPasswordRepeat = registrationPasswordRepeat;
    }

    /**
     * Standard getter.
     *
     * @return The user data's registration username.
     */
    public String getRegistrationUsername() {
        return registrationUsername;
    }

    /**
     * Standard setter.
     *
     * @param registrationUsername The user data's registration username.
     */
    public void setRegistrationUsername(String registrationUsername) {
        this.registrationUsername = registrationUsername;
    }
}
