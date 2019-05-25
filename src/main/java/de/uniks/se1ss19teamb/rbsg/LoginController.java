package de.uniks.se1ss19teamb.rbsg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
    void setOnAction(ActionEvent event) throws IOException {
        if (event.getSource().equals(btnLogin)) {
            //slideNextScene("main.fxml",100);
            //makeFadeOutTransition();
        }
        if(event.getSource().equals(btnRegistration)){
            //slideNextScene("register.fxml",400);
            //makeFadeOutTransition();
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
        }else {
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


    private void makeFadeOutTransition(){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(loginScreen);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> {
            try {
                fadeNextScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fadeTransition.play();
    }

    private void fadeNextScene() throws IOException {
        Parent nextScene = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(nextScene);
        Stage currentStage = (Stage) loginScreen.getScene().getWindow();

        currentStage.setScene(scene);

    }

}
