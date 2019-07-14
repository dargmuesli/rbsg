package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.uniks.se1ss19teamb.rbsg.model.UserData;
import de.uniks.se1ss19teamb.rbsg.request.RegisterUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterController {

    private static final Logger logger = LogManager.getLogger();
    @FXML
    AnchorPane errorContainer;
    @FXML
    private AnchorPane registerScreen;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private JFXButton btnConfirm;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXPasswordField passwordRepeat;
    @FXML
    private AnchorPane registerScreen1;
    private UserData userData;

    public void initialize() {
        UserInterfaceUtils.initialize(
            registerScreen, registerScreen1, RegisterController.class, btnFullscreen, errorContainer);

        // load user data
        userData = UserData.loadUserData(NotificationHandler.getInstance());

        if (userData == null) {
            NotificationHandler.getInstance().sendWarning("User data couldn't be deserialized!", logger);
            return;
        }

        username.setText(userData.getRegisterUsername());
        password.setText(userData.getRegisterPassword());
        passwordRepeat.setText(userData.getRegisterPasswordRepeat());

        Platform.runLater(() -> {
            if (username.getText().equals("")) {
                username.requestFocus();
            } else if (password.getText().equals("")) {
                password.requestFocus();
            } else if (passwordRepeat.getText().equals("")) {
                passwordRepeat.requestFocus();
            } else {
                btnConfirm.requestFocus();
            }
        });

        UserData.deleteUserData(NotificationHandler.getInstance());

        registerScreen.setOpacity(0);
    }

    @FXML
    void eventHandler(ActionEvent event) {
        if (event.getSource().equals(btnConfirm)) {
            register();
        } else if (event.getSource().equals(btnCancel)) {
            goToLogin();
        } else if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        }
    }

    @FXML
    public void onEnter() {
        register();
    }

    public void keyEventHandler(KeyEvent keyEvent) {
        if (keyEvent.getSource().equals(btnConfirm) && keyEvent.getCode().equals(KeyCode.ENTER)) {
            register();
        }

        if (keyEvent.getSource().equals(btnCancel)
            && keyEvent.getCode().equals(KeyCode.ENTER)) {
            goToLogin();
        }
    }

    private void register() {
        if (username.getText().isEmpty()
            || password.getText().isEmpty()
            || passwordRepeat.getText().isEmpty()) {
            NotificationHandler.getInstance().sendWarning("Bitte geben Sie etwas ein.", logger);
            return;
        }

        if (!password.getText().equals(passwordRepeat.getText())) {
            NotificationHandler.getInstance().sendWarning("Die Passwörter sind verschieden!", logger);
            return;
        }

        RegisterUserRequest register = new RegisterUserRequest(
            username.getText(), password.getText());
        register.sendRequest();

        if (!register.getSuccessful()) {
            NotificationHandler.getInstance().sendWarning("Die Registrierung ist fehlgeschlagen!", logger);
            return;
        }

        // save user data for login screen
        userData.setLoginUsername(username.getText());
        userData.setLoginPassword(password.getText());

        SerializeUtils.serialize(UserData.USER_DATA_PATH.toString(), userData);

        NotificationHandler.getInstance().sendSuccess("Registrierung erfolgreich!", logger);

        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", registerScreen);
    }

    private void goToLogin() {
        // save user data for registration screen
        userData.setRegisterUsername(username.getText());
        userData.setRegisterPassword(password.getText());
        userData.setRegisterPasswordRepeat(passwordRepeat.getText());

        SerializeUtils.serialize(UserData.USER_DATA_PATH.toString(), userData);

        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", registerScreen);
    }
}