package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController {
    
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
    
    private ErrorHandler errorHandler;
    
    private ErrorPopupController controller;
    
    public static String userKey;

    private static final Logger logger = LogManager.getLogger(LoginController.class);
    
    private static final String USER_DATA = "./userData.txt";
    
    public void initialize() {
        File userData = new File(USER_DATA);
        if (userData.exists()) {
            loadUserData();
            userData.delete();
        }
        loginScreen.setOpacity(0);
        UserInterfaceUtils.makeFadeInTransition(loginScreen);
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/de/uniks/se1ss19teamb/rbsg/ErrorPopup.fxml"));
        
        try {
            Parent parent = fxmlLoader.load();
            errorContainer.getChildren().add(parent);
            
            controller = fxmlLoader.getController();
            errorHandler  = new ErrorHandler();
            errorHandler.setErrorPopupController(controller);
            
        } catch (IOException e) {
            errorHandler.sendError("Fehler beim Laden der FXML-Datei für den Login!");
            logger.error(e);
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
            rememberLogin.setSelected(true);
        }
    }
    
    private void login() {
        if (!userName.getText().isEmpty() && !password.getText().isEmpty()) {
            LoginUserRequest login = new LoginUserRequest(
                    userName.getText(), password.getText());
            login.sendRequest();
            if (login.getSuccessful()) {
                File file = new File(USER_DATA);
                if (file.exists()) {
                    file.delete();
                }
                if (rememberLogin.isSelected()) {
                    saveUserData();
                }
                
                setUserKey(login.getUserKey());
                UserInterfaceUtils.makeFadeOutTransition(
                        "/de/uniks/se1ss19teamb/rbsg/main.fxml", loginScreen);
                
            } else {
                errorHandler.sendError("Login fehlgeschlagen!");
            }
        } else {
            errorHandler.sendError("Bitte geben Sie Benutzernamen und Passwort ein.");
        }
    }
    
    private void goToRegister() {
        UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/register.fxml", loginScreen);
    }
    
    private void saveUserData() {
        UserData userData = new UserData(userName.getText(), password.getText());
        SerializeUtils.serialize(USER_DATA, userData);
    }

    private void loadUserData() {
        Path path = Paths.get(USER_DATA);
        UserData userData = SerializeUtils.deserialize(path, UserData.class);
        userName.setText(userData.getUserName());
        password.setText(userData.getPassword());
        rememberLogin.setSelected(true);
        Platform.runLater(() -> btnLogin.requestFocus());
    }
    
    public static String getUserKey() {
        return userKey;
    }
    
    public static void setUserKey(String key) {
        userKey = key;
    }
}
