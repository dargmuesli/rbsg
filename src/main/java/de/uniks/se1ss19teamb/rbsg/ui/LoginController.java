package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.features.Snake;
import de.uniks.se1ss19teamb.rbsg.model.UserData;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.util.*;

import java.io.*;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginController {

    private static final Logger logger = LogManager.getLogger();
    private static NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();

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
    private JFXButton btnSnake;

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

        // load user data
        userData = UserData.loadUserData(notificationHandler);

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

        UserData.deleteUserData(notificationHandler);

        loginScreen.setOpacity(0);
        UserInterfaceUtils.makeFadeInTransition(loginScreen);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/popup.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            errorContainer.getChildren().add(parent);

            PopupController controller = fxmlLoader.getController();
            notificationHandler.setPopupController(controller);

        } catch (IOException e) {
            notificationHandler.sendError("Fehler beim Laden der FXML-Datei für den Login!", logger, e);
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
        } else if (event.getSource().equals(btnSnake)) {
            new Snake().start(Snake.classStage);
        }
    }

    @FXML
    public void onEnter() {
        login();
    }

    private void login() {
        if (userName.getText().isEmpty() || password.getText().isEmpty()) {
            notificationHandler.sendWarning("Bitte geben Sie Benutzernamen und Passwort ein.", logger);
            return;
        }

        LoginUserRequest login = new LoginUserRequest(userName.getText(), password.getText());
        login.sendRequest();

        if (!login.getSuccessful()) {
            notificationHandler.sendWarning("Login fehlgeschlagen!", logger);
            return;
        }

        if (rememberLogin.isSelected()) {
            saveUserData();
        } else {
            UserData.deleteUserData(notificationHandler);
        }

        setUserKey(login.getUserKey());
        setUser(userName.getText());

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