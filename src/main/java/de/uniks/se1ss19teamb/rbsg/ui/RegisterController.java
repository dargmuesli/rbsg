package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.uniks.se1ss19teamb.rbsg.request.RegisterUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class RegisterController {
    @FXML
    JFXPasswordField password;
    @FXML
    JFXPasswordField confirmPassword;
    @FXML
    private AnchorPane registerScreen;
    
    @FXML
    private JFXButton btnCancel;
    
    @FXML
    private JFXButton btnConfirm;
    
    @FXML
    AnchorPane errorContainer;
    
    @FXML
    private JFXTextField userName;

    private ErrorHandler errorHandler;
    
    public void initialize() {
        registerScreen.setOpacity(0);
        UserInterfaceUtils.makeFadeInTransition(registerScreen);
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/de/uniks/se1ss19teamb/rbsg/ErrorPopup.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            errorContainer.getChildren().add(parent);

            ErrorPopupController controller = fxmlLoader.getController();
            errorHandler = new ErrorHandler();
            errorHandler.setErrorPopupController(controller);
            
        } catch (IOException e) {
            e.printStackTrace();
        }      
    }
    
    @FXML
    void setOnAction(ActionEvent event) throws IOException {
        if (event.getSource().equals(btnCancel)) {
            UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/login.fxml", registerScreen);
        }
        if (event.getSource().equals(btnConfirm)) {
            if (!userName.getText().isEmpty()
                    && !password.getText().isEmpty()
                    && !confirmPassword.getText().isEmpty()) {
                
                if (password.getText().equals(confirmPassword.getText())) {
                    RegisterUserRequest register = new RegisterUserRequest(
                            userName.getText(), password.getText());
                    
                    register.sendRequest();
                    if (register.getSuccessful()) {
                        UserInterfaceUtils.makeFadeOutTransition(
                                "/de/uniks/se1ss19teamb/rbsg/login.fxml", registerScreen);
                        
                        errorHandler.sendError("Registrierung erfolgreich!");
                    } /*else {
                        errorHandler.sendError("Entschuldigung.
                         Es ist etwas bei der Registrierung schief gelaufen.
                        Bitte versuchen Sie er erneut.");
                    }*/
                } else {
                    errorHandler.sendError("Die Passwörter sind verschieden!");
                }
            
            } else {
                errorHandler.sendError("Bitte geben Sie etwas ein.");
            }
        }
    }
}