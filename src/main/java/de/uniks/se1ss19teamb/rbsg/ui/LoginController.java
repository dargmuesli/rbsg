package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.features.ChuckNorrisJokeTicker;
import de.uniks.se1ss19teamb.rbsg.features.ZuendorfMeme;
import de.uniks.se1ss19teamb.rbsg.model.UserData;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.*;

import java.util.Arrays;
import java.util.Random;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginController {

    private static final Logger logger = LogManager.getLogger();

    private static String user;
    private static String userKey;
    private static UserData userData;

    @FXML
    private AnchorPane loginScreen;
    @FXML
    private JFXTextField userName;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private Button btnRegistration;
    @FXML
    private AnchorPane errorContainer;
    @FXML
    private JFXCheckBox rememberLogin;
    @FXML
    private AnchorPane loginScreen1;
    @FXML
    private Label jokeLabel;

    static String getUser() {
        return user;
    }

    private static void setUser(String name) {
        user = name;
    }

    public static String getUserKey() {
        return userKey;
    }

    public static void setUserKey(String key) {
        userKey = key;
    }

    public void initialize() {

        Theming.setTheme(Arrays.asList(new Pane[]{loginScreen, loginScreen1}));
        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);

        ChuckNorrisJokeTicker.setLabelPosition(jokeLabel);
        ChuckNorrisJokeTicker.moveLabel(jokeLabel);
      
        UserInterfaceUtils.initialize(loginScreen, loginScreen1, LoginController.class, btnFullscreen, errorContainer);

        // load user data
        userData = UserData.loadUserData(NotificationHandler.getInstance());

        if (userData == null) {
            userData = new UserData();
        }

        userName.setText(userData.getLoginUsername());
        password.setText(userData.getLoginPassword());
        rememberLogin.setSelected(userData.isLoginRemember());

        Platform.runLater(() -> {
            if (userName.getText().equals("")) {
                userName.requestFocus();
            } else if (password.getText().equals("")) {
                password.requestFocus();
            } else {
                btnLogin.requestFocus();
            }
        });

        UserData.deleteUserData(NotificationHandler.getInstance());

        loginScreen.setOpacity(0);

        // 1% meme chance
        if (new Random().nextFloat() < 0.01) {
            // needed because of root.getWidth/Height
            Platform.runLater(() -> ZuendorfMeme.setup(loginScreen1));
        }
    }

    @FXML
    void eventHandler(ActionEvent event) {
        if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        } else if (event.getSource().equals(btnLogin)) {
            login();
        } else if (event.getSource().equals(btnRegistration)) {
            goToRegister();
            ChuckNorrisJokeTicker.stopAnimation();
        }
    }

    @FXML
    public void onEnter() {
        login();
    }

    private void login() {
        if (userName.getText().isEmpty() || password.getText().isEmpty()) {
            NotificationHandler.getInstance().sendWarning("Bitte geben Sie Benutzernamen und Passwort ein.", logger);
            return;
        }

        LoginUserRequest login = new LoginUserRequest(userName.getText(), password.getText());
        login.sendRequest();

        if (!login.getSuccessful()) {
            NotificationHandler.getInstance().sendError("Login fehlgeschlagen!", logger);
            return;
        }

        if (rememberLogin.isSelected()) {
            saveUserData();
        } else {
            UserData.deleteUserData(NotificationHandler.getInstance());
        }

        setUserKey(login.getData());
        setUser(userName.getText());
        ChuckNorrisJokeTicker.stopAnimation();
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", loginScreen);
    }

    private void goToRegister() {
        saveUserData();
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/register.fxml", loginScreen);
    }

    private void saveUserData() {
        userData.setLoginUsername(userName.getText());
        userData.setLoginPassword(password.getText());
        userData.setLoginRemember(rememberLogin.isSelected());

        SerializeUtils.serialize(UserData.USER_DATA_PATH.toString(),
            userData);
    }
}