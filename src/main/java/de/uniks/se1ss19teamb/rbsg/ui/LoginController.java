package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
import de.uniks.se1ss19teamb.rbsg.util.UserData;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

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
    
    private static final String USER_DATA = "./userData.txt";
    
    private static final String PASSWORD_DATA = "./passwordData.txt";
    
    public void initialize() {
        File un = new File(USER_DATA);
        File pw = new File(PASSWORD_DATA);
        if (un.exists() && pw.exists()) {
            UserData.loadUserData(USER_DATA, PASSWORD_DATA, rememberLogin,btnLogin, userName, password);
            un.delete();
            pw.delete();
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
            e.printStackTrace();
        }
    }
    
    
    
    @FXML
    void eventHandler(ActionEvent event) throws IOException {
        
        if (event.getSource().equals(btnLogin)) {
            login();
        }
        if (event.getSource().equals(btnRegistration)) {
            goToRegister();
        }
    }
    
    public void keyEventHandler(KeyEvent keyEvent) throws IOException {
        
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
    
    private void login() throws IOException {
        //slideNextScene("main.fxml",100);
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
                    UserData.saveUserData(userName.getText(), password.getText(), USER_DATA, PASSWORD_DATA);
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
        //slideNextScene("register.fxml",400);
        UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/register.fxml", loginScreen);
    }
    
    public static String getUserKey() {
        return userKey;
    }
    
    public static void setUserKey(String key) {
        userKey = key;
    }
}
