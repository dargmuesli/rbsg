package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.util.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginController {

    private static final Logger logger = LogManager.getLogger();
    private static final Path USER_DATA =
        Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "rbsg_user-data.json");
    private static String userKey;
    private static String user;
    private static ChatSocket chatSocket;
    @FXML
    private AnchorPane loginScreen;
    @FXML
    private JFXTextField userName;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private FontAwesomeIconView icoFullscreen;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private Button btnRegistration;
    @FXML
    private AnchorPane errorContainer;
    @FXML
    private JFXCheckBox rememberLogin;
    private ErrorHandler errorHandler = ErrorHandler.getErrorHandler();

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
        File userData = USER_DATA.toFile();
        if (userData.exists()) {
            loadUserData();
            userData.delete();
        }
        loginScreen.setOpacity(0);
        UserInterfaceUtils.makeFadeInTransition(loginScreen);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/ErrorPopup.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            errorContainer.getChildren().add(parent);

            ErrorPopupController controller = fxmlLoader.getController();
            errorHandler.setErrorPopupController(controller);

        } catch (IOException e) {
            errorHandler.sendError("Fehler beim Laden der FXML-Datei für den Login!", logger, e);
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
        }
    }

    @FXML
    public void onEnter() {
        login();
    }

    public void keyEventHandler(KeyEvent keyEvent) {

        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (keyEvent.getSource().equals(btnFullscreen)) {
                UserInterfaceUtils.toggleFullscreen(btnFullscreen);
            } else if (keyEvent.getSource().equals(btnLogin)) {
                login();
            } else if (keyEvent.getSource().equals(btnRegistration)) {
                goToRegister();
            } else if (keyEvent.getSource().equals(rememberLogin)) {
                if (rememberLogin.isSelected()) {
                    rememberLogin.setSelected(false);
                } else {
                    rememberLogin.setSelected(true);
                }
            }
        }
    }

    private void login() {
        if (!userName.getText().isEmpty() && !password.getText().isEmpty()) {
            LoginUserRequest login = new LoginUserRequest(
                userName.getText(), password.getText());
            login.sendRequest();
            if (login.getSuccessful()) {
                File file = USER_DATA.toFile();
                if (file.exists()) {
                    file.delete();
                }
                if (rememberLogin.isSelected()) {
                    saveUserData();
                }

                setUserKey(login.getUserKey());
                setUser(userName.getText());
                UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", loginScreen);

            } else {
                errorHandler.sendError("Login fehlgeschlagen!");
            }
        } else {
            errorHandler.sendError("Bitte geben Sie Benutzernamen und Passwort ein.");
        }
    }

    private void goToRegister() {
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/register.fxml", loginScreen);
    }

    private void saveUserData() {
        UserData userData = new UserData(userName.getText(), password.getText());
        SerializeUtils.serialize(USER_DATA.toString(), userData);
    }

    private void loadUserData() {
        UserData userData = SerializeUtils.deserialize(USER_DATA.toFile(), UserData.class);
        userName.setText(userData.getUserName());
        password.setText(userData.getPassword());
        rememberLogin.setSelected(true);
        Platform.runLater(() -> btnLogin.requestFocus());
    }
}
