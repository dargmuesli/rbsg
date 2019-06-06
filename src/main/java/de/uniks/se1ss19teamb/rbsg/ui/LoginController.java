package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;

import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class LoginController {
    
    @FXML
    private AnchorPane loginScreen;
    
    @FXML
    private JFXTextField userName;
    
    @FXML
    private JFXTextField password;
    
    @FXML
    private JFXButton btnLogin;
    
    @FXML
    private JFXButton btnCancel;
    
    @FXML
    private Button btnRegistration;
    
    @FXML
    private AnchorPane errorContainer;
    
    private ErrorHandler errorHandler;
    
    private ErrorPopupController controller;
    
    public static String userKey;
    
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
        
        if (event.getSource().equals(btnRegistration)) {
            //slideNextScene("register.fxml",400);
            UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/register.fxml", loginScreen);
        }
        
    }
}
