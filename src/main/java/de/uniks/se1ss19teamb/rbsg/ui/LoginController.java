package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class LoginController {

    @FXML
    private AnchorPane loginScreen;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXTextField passwort;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private Button btnRegistration;

    @FXML
    private AnchorPane errorContainer;

    public void initialize(){
        loginScreen.setOpacity(0);
        UserInterfaceUtils.makeFadeInTransition(loginScreen);
    
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/uniks/se1ss19teamb/rbsg/ErrorPopup.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            // controller not used yet, but it's good to have it for later purposes.
            ErrorPopupController controller = fxmlLoader.getController();
            ErrorHandler errorHandler = new ErrorHandler();
            errorHandler.setErrorPopupController(controller);
            errorContainer.getChildren().add(parent);
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }

    @FXML
    void setOnAction(ActionEvent event) throws IOException {
        if (event.getSource().equals(btnLogin)) {
            //slideNextScene("main.fxml",100);
            UserInterfaceUtils.makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/main.fxml", loginScreen);
        }
        if(event.getSource().equals(btnRegistration)){
            //slideNextScene("register.fxml",400);
            UserInterfaceUtils.makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/register.fxml", loginScreen);
        }

    }
}
