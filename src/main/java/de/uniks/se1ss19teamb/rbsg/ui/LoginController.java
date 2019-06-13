package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.model.UserData;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.util.*;

import java.io.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginController {

    private static final Logger logger = LogManager.getLogger();
    private static String userKey;
    private static String user;
    private static ChatSocket chatSocket;
    UserData userData;
    @FXML
    private AnchorPane loginScreen;
    @FXML
    private JFXTextField userName;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private Button btnRegistration;
    @FXML
    private AnchorPane errorContainer;
    @FXML
    private JFXCheckBox rememberLogin;
    private NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();

    public static String getUserKey() {
        return userKey;
    }

    public static void setUserKey(String key) {
        userKey = key;
    }

    static String getUser() {
        return user;
    }

    private static void setUser(String name) {
        user = name;
    }

    static ChatSocket getChatSocket() {
        return chatSocket;
    }

    static void setChatSocket(ChatSocket chatSocket) {
        LoginController.chatSocket = chatSocket;
    }

    public void initialize() {
        // load user data
        userData = UserData.loadUserData(notificationHandler);

        if (userData == null) {
            userData = new UserData();
        }

        userName.setText(userData.getLoginUserName());
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
        if (event.getSource().equals(btnLogin)) {
            login();
        }

        if (event.getSource().equals(btnRegistration)) {
            goToRegister();
        }
    }

    @FXML
    public void onEnter() {
        login();
    }

    public void keyEventHandler(KeyEvent keyEvent) {
        if (keyEvent.getSource().equals(btnLogin) && keyEvent.getCode().equals(KeyCode.ENTER)) {
            login();
        }

        if (keyEvent.getSource().equals(btnRegistration)
            && keyEvent.getCode().equals(KeyCode.ENTER)) {
            goToRegister();
        }

        if (keyEvent.getSource().equals(rememberLogin)
            && keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (rememberLogin.isSelected()) {
                rememberLogin.setSelected(false);
            } else {
                rememberLogin.setSelected(true);
            }
        }
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
        userData.setLoginUserName(userName.getText());
        userData.setLoginPassword(password.getText());
        userData.setLoginRemember(rememberLogin.isSelected());

        SerializeUtils.serialize(UserData.USER_DATA_PATH.toString(),
            userData);
    }
}
