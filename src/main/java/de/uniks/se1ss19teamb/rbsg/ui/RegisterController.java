package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.uniks.se1ss19teamb.rbsg.model.UserData;
import de.uniks.se1ss19teamb.rbsg.request.RegisterUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
    private JFXButton btnConfirm;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXPasswordField passwordRepeat;
    private NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();
    private UserData userData;

    public void initialize() {
        // load user data
        userData = UserData.loadUserData(notificationHandler);

        if (userData == null) {
            notificationHandler.sendWarning("User data couldn't be deserialized!", logger);
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

        UserData.deleteUserData(notificationHandler);

        registerScreen.setOpacity(0);
        UserInterfaceUtils.makeFadeInTransition(registerScreen);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/ErrorPopup.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            errorContainer.getChildren().add(parent);

            PopupController controller = fxmlLoader.getController();
            notificationHandler.setPopupController(controller);

        } catch (IOException e) {
            notificationHandler.sendError("Fehler beim Laden der FXML-Datei für die Registrierung!", logger, e);
        }
    }

    @FXML
    void eventHandler(ActionEvent event) {
        if (event.getSource().equals(btnConfirm)) {
            register();
        } else if (event.getSource().equals(btnCancel)) {
            goToLogin();
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
            notificationHandler.sendWarning("Bitte geben Sie etwas ein.", logger);
            return;
        }

        if (!password.getText().equals(passwordRepeat.getText())) {
            notificationHandler.sendWarning("Die Passwörter sind verschieden!", logger);
            return;
        }

        RegisterUserRequest register = new RegisterUserRequest(
            username.getText(), password.getText());
        register.sendRequest();

        if (!register.getSuccessful()) {
            notificationHandler.sendWarning("Die Registrierung ist fehlgeschlagen!", logger);
        }

        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", registerScreen);

        // TODO success
        notificationHandler.sendWarning("Registrierung erfolgreich!", logger);
    }

    private void goToLogin() {
        // save user data
        userData.setRegisterUsername(username.getText());
        userData.setRegisterPassword(password.getText());
        userData.setRegisterPasswordRepeat(passwordRepeat.getText());

        SerializeUtils.serialize(UserData.USER_DATA_PATH.toString(), userData);

        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", registerScreen);
    }
}