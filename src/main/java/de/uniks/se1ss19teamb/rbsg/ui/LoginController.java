package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;

import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class LoginController {
    
    public static final String USER_DATA = "./userData.txt";
    
    @FXML
    private AnchorPane loginScreen;
    
    @FXML
    private JFXTextField userName;
    
    @FXML
    private JFXPasswordField password;
    
    @FXML
    private JFXButton btnLogin;
    
    @FXML
    private JFXButton btnCancel;
    
    @FXML
    private Button btnRegistration;
    
    @FXML
    private AnchorPane errorContainer;
    
    @FXML
    private JFXCheckBox rememberLogin;
    
    private ErrorHandler errorHandler;
    
    private ErrorPopupController controller;
    
    public static String userKey;
    
    public static String getUserKey() {
        return userKey;
    }
    
    public static void setUserKey(String key) {
        userKey = key;
    }
    
    public void initialize() {
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
    void setOnAction(ActionEvent event) throws IOException {
        if (event.getSource().equals(btnLogin)) {
            //slideNextScene("main.fxml",100);
            if (!userName.getText().isEmpty() && !password.getText().isEmpty()) {
                LoginUserRequest login = new LoginUserRequest(
                        userName.getText(), password.getText());
                login.sendRequest();
                if (login.getSuccessful()) {
                    
                    setUserKey(login.getUserKey());
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
        
        if (event.getSource().equals(btnRegistration)) {
            //slideNextScene("register.fxml",400);
            UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/register.fxml", loginScreen);
        }
        
    }
}
