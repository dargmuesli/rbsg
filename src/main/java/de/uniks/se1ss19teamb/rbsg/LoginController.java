package de.uniks.se1ss19teamb.rbsg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
            setNextScene("main.fxml", 100);
        }
        if(event.getSource().equals(btnRegistration)){
            setNextScene("register.fxml", 400);
        }

    }


    private void setNextScene(String path, int xValue) throws IOException {
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
}
