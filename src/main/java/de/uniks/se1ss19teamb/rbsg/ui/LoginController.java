package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
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

import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController {
    
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

    Logger logger = LogManager.getLogger(LoginController.class);
    
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
            //slideNextScene("/de/uniks/se1ss19teamb/rbsg/main.fxml",400);
            makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/main.fxml");
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
            //slideNextScene("/de/uniks/se1ss19teamb/rbsg/register.fxml",600);
            makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/register.fxml");
        }

    }


    private void slideNextScene(String path, int value) throws IOException {
        Random r = new Random();
        boolean randomValue = r.nextBoolean();
        if (randomValue) {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            root.translateYProperty().setValue(-value);

            Timeline timeline = new Timeline();
            KeyValue keyValue = new KeyValue(root.translateYProperty(),0,Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();

            Scene scene = new Scene(root);
            Stage stage = (Stage) loginScreen.getScene().getWindow();

            stage.setScene(scene);
        } else {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            root.translateXProperty().setValue(-value);

            Timeline timeline = new Timeline();
            KeyValue keyValue = new KeyValue(root.translateXProperty(),0,Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();

            Scene scene = new Scene(root);
            Stage stage = (Stage) loginScreen.getScene().getWindow();

            stage.setScene(scene);
          
            //slideNextScene("register.fxml",400);
            UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/register.fxml", loginScreen);
        }
        
    }
}
