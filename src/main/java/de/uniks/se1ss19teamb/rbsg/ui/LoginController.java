package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;


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
        if (event.getSource().equals(btnLogin)) {
            //slideNextScene("main.fxml",100);
            makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/main.fxml");
        }
        if(event.getSource().equals(btnRegistration)){
            //slideNextScene("register.fxml",400);
            makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/register.fxml");
        }

    }


    private void slideNextScene(String path, int xValue) throws IOException {
        Random r = new Random();
        boolean randomValue = r.nextBoolean();
        if(randomValue){
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginScreen.getScene().getWindow();

            root.translateYProperty().setValue(-400);

            Timeline timeline = new Timeline();
            KeyValue keyValue = new KeyValue(root.translateYProperty(),0,Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();

            stage.setScene(scene);
        } else {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginScreen.getScene().getWindow();

            root.translateXProperty().setValue(-xValue);

            Timeline timeline = new Timeline();
            KeyValue keyValue = new KeyValue(root.translateXProperty(),0,Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();

            stage.setScene(scene);
        }
    }


    private void makeFadeOutTransition(String path){
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
    
    private void makeFadeInTransition () {
        FadeTransition fadeTransition = new FadeTransition();
        setFadeParameters(fadeTransition);
        fadeTransition.play();
    }
    
    private void setFadeParameters(FadeTransition fadeTransition) {
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(loginScreen);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
    }
    
    private void fadeNextScene(String path) throws IOException {
        Parent nextScene = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(nextScene);
        Stage currentStage = (Stage) loginScreen.getScene().getWindow();

        currentStage.setScene(scene);
    }

}
