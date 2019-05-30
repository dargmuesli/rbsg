package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        makeFadeInTransition();
        
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
            makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/login.fxml");
        }
        if (event.getSource().equals(btnConfirm)) {
            //TODO register user
        }
    }
    
    private void makeFadeInTransition () {
        FadeTransition fadeTransition = new FadeTransition();
        setFadeParameters(fadeTransition);
        fadeTransition.play();
    }
    
    private void setFadeParameters(FadeTransition fadeTransition) {
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(registerScreen);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
    }
    
    private void makeFadeOutTransition(String path) {
        FadeTransition fadeTransition = new FadeTransition();
        setFadeParameters(fadeTransition);
        fadeTransition.setOnFinished(event -> {
            try {
                fadeNextScene(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fadeTransition.play();
    }
    
    private void fadeNextScene(String path) throws IOException {
        Parent nextScene = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(nextScene);
        Stage currentStage = (Stage) registerScreen.getScene().getWindow();
        
        currentStage.setScene(scene);
    }
}