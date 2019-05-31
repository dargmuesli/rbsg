package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;

public class RegisterController {
    
    @FXML
    private AnchorPane registerScreen;
    
    @FXML
    private JFXButton btnCancel;
    
    @FXML
    private JFXButton btnConfirm;
    
    @FXML
    AnchorPane errorContainer;
    
    public void initialize() {
        registerScreen.setOpacity(0);
        UserInterfaceUtils.makeFadeInTransition(registerScreen);
        
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
        if (event.getSource().equals(btnCancel)) {
            UserInterfaceUtils.makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/login.fxml", registerScreen);
        }
        if (event.getSource().equals(btnConfirm)) {
            //TODO register user
        }
    }
}