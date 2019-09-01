package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.uniks.se1ss19teamb.rbsg.model.UserData;
import de.uniks.se1ss19teamb.rbsg.request.RegisterUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.*;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistrationController {

    private static final Logger logger = LogManager.getLogger();

    @FXML
    private AnchorPane apnFade;
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
    private AnchorPane apnRoot;
    private UserData userData;

    @FXML
    private void initialize() {
        UserInterfaceUtils.initialize(apnFade, apnRoot, RegistrationController.class, btnFullscreen);

        // load user data
        userData = UserData.loadUserData();

        if (userData == null) {
            NotificationHandler.sendWarning("User data couldn't be deserialized!", logger);
            return;
        }

        username.setText(userData.getRegistrationUsername());
        password.setText(userData.getRegistrationPassword());
        passwordRepeat.setText(userData.getRegistrationPasswordRepeat());

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

        UserData.deleteUserData();

        apnFade.setOpacity(0);
    }

    @FXML
    private void cancel() {
        btnCancel.setDisable(true);
        goToLogin();
    }

    @FXML
    private void confirm() {
        btnConfirm.setDisable(true);
        register();
    }

    @FXML
    private void toggleFullscreen() {
        UserInterfaceUtils.toggleFullscreen(btnFullscreen);
    }

    @FXML
    public void onEnter() {
        register();
    }

    @FXML
    private void keyEventHandler(KeyEvent keyEvent) {
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
            NotificationHandler.sendWarning("Please enter everything.", logger);
            return;
        }

        if (!password.getText().equals(passwordRepeat.getText())) {
            Image image = new Image(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/memes/MemeCopy.jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            Scene scene = new Scene(new VBox(imageView),250,200);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> stage.close());
            delay.play();
          
            NotificationHandler.sendWarning("The password do not match!", logger);

            return;
        }

        if (!RequestUtil.request(new RegisterUserRequest(username.getText(), password.getText()))) {
            return;
        }

        // save user data for login screen
        userData.setLoginUsername(username.getText());
        userData.setLoginPassword(password.getText());

        SerializeUtil.serialize(UserData.USER_DATA_PATH.toString(), userData);

        NotificationHandler.sendSuccess("Registered successfully!", logger);

        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", apnFade);
    }

    private void goToLogin() {
        // save user data for registration screen
        userData.setRegistrationUsername(username.getText());
        userData.setRegistrationPassword(password.getText());
        userData.setRegistrationPasswordRepeat(passwordRepeat.getText());

        SerializeUtil.serialize(UserData.USER_DATA_PATH.toString(), userData);

        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", apnFade);
    }
}
