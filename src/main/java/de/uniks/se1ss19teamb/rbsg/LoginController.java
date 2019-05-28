package de.uniks.se1ss19teamb.rbsg;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.util.Random;

import javafx.animation.FadeTransition;
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

    public void initialize() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ErrorPopup.fxml"));
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
            makeFadeOutTransition("main.fxml");
        }
        if (event.getSource().equals(btnRegistration)) {
            //slideNextScene("register.fxml",400);
            makeFadeOutTransition("register.fxml");
        }

    }


    private void slideNextScene(String path, int xvalue) throws IOException {
        Random r = new Random();
        boolean randomValue = r.nextBoolean();
        if (randomValue) {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginScreen.getScene().getWindow();

            root.translateYProperty().setValue(-400);

            Timeline timeline = new Timeline();
            KeyValue keyValue = new KeyValue(root.translateYProperty(),0, Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();

            stage.setScene(scene);
        } else {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginScreen.getScene().getWindow();

            root.translateXProperty().setValue(-xvalue);

            Timeline timeline = new Timeline();
            KeyValue keyValue = new KeyValue(root.translateXProperty(),0,Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();

            stage.setScene(scene);
        }
    }


    private void makeFadeOutTransition(String path) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(loginScreen);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
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
        Stage currentStage = (Stage) loginScreen.getScene().getWindow();

        currentStage.setScene(scene);

    }

}
