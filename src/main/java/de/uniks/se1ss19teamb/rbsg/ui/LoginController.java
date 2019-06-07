package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
<<<<<<< HEAD
=======
import com.jfoenix.controls.JFXCheckBox;
>>>>>>> 743c448e768b2437ec5cfa27d0b3045882ce3daa
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
<<<<<<< HEAD
import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;
=======
import de.uniks.se1ss19teamb.rbsg.util.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Platform;
>>>>>>> 743c448e768b2437ec5cfa27d0b3045882ce3daa
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
        enterAction();
        UserInterfaceUtils.makeFadeInTransition(loginScreen);
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/de/uniks/se1ss19teamb/rbsg/ErrorPopup.fxml"));
        
        try {
            Parent parent = fxmlLoader.load();
            errorContainer.getChildren().add(parent);
<<<<<<< HEAD

            ErrorPopupController controller = fxmlLoader.getController();
            errorHandler  = new ErrorHandler();
=======
            
            controller = fxmlLoader.getController();
            errorHandler = ErrorHandler.getErrorHandler();
>>>>>>> 743c448e768b2437ec5cfa27d0b3045882ce3daa
            errorHandler.setErrorPopupController(controller);
            
        } catch (IOException e) {
            errorHandler.sendError("Fehler beim Laden der FXML-Datei für den Login!");
            logger.error(e);
        }
    }
    
    
    
    @FXML
    void eventHandler(ActionEvent event) {

        if (event.getSource().equals(btnLogin)) {
<<<<<<< HEAD
            //UserInterfaceUtils.slideNextScene("/de/uniks/se1ss19teamb/rbsg/main.fxml",400,loginScreen);
            UserInterfaceUtils.makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/main.fxml",loginScreen);

            if (!userName.getText().isEmpty() && !password.getText().isEmpty()) {
                LoginUserRequest login = new LoginUserRequest(
                        userName.getText(), password.getText());
                login.sendRequest();
                if (login.getSuccessful()) {
                    userKey = login.getUserKey();
                    UserInterfaceUtils.makeFadeOutTransition(
                            "/de/uniks/se1ss19teamb/rbsg/main.fxml", loginScreen);
                    errorHandler.sendError("Login erfolgreich!");
                    
                } else {
                    errorHandler.sendError("Login fehlgeschlagen.");
=======
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
>>>>>>> 743c448e768b2437ec5cfa27d0b3045882ce3daa
                }
                
                setUserKey(login.getUserKey());
                UserInterfaceUtils.makeFadeOutTransition(
                        "/de/uniks/se1ss19teamb/rbsg/main.fxml", loginScreen);
                
            } else {
                errorHandler.sendError("Login fehlgeschlagen!");
            }
<<<<<<< HEAD

        }
        
        if (event.getSource().equals(btnRegistration)) {
            //UserInterfaceUtils.slideNextScene("/de/uniks/se1ss19teamb/rbsg/register.fxml",600,loginScreen);
            UserInterfaceUtils.makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/register.fxml",loginScreen);
        }


    }

    private void enterAction() {
        loginScreen.addEventHandler(KeyEvent.KEY_PRESSED,event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!userName.getText().isEmpty() && !password.getText().isEmpty()) {
                    LoginUserRequest login = new LoginUserRequest(
                            userName.getText(), password.getText());
                    login.sendRequest();
                    if (login.getSuccessful()) {
                        userKey = login.getUserKey();
                        UserInterfaceUtils.makeFadeOutTransition(
                                "/de/uniks/se1ss19teamb/rbsg/main.fxml", loginScreen);
                        errorHandler.sendError("Login erfolgreich!");

                    } else {
                        errorHandler.sendError("Login fehlgeschlagen.");
                    }
                } else {
                    errorHandler.sendError("Bitte geben Sie etwas ein.");
                }
            }
        });
=======
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
>>>>>>> 743c448e768b2437ec5cfa27d0b3045882ce3daa
    }

}
