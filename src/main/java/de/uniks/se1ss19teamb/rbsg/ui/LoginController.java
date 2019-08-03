package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.features.ChuckNorrisJokeTicker;
import de.uniks.se1ss19teamb.rbsg.features.ZuendorfMeme;
import de.uniks.se1ss19teamb.rbsg.model.UserData;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.*;

import java.util.Arrays;
import java.util.Random;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class LoginController {

    private static final Logger logger = LogManager.getLogger();

    private static String userName;
    private static String userToken;
    private static UserData userData;

    @FXML
    private AnchorPane apnFade;
    @FXML
    private JFXTextField txtUserName;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private Button btnRegistration;
    @FXML
    private AnchorPane errorContainer;
    @FXML
    private JFXCheckBox rememberLogin;
    @FXML
    private AnchorPane apnRoot;
    @FXML
    private Label jokeLabel;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        LoginController.userName = userName;
    }

    public static String getUserToken() {
        return userToken;
    }

    public static void setUserToken(String key) {
        userToken = key;
    }

    public void initialize() {

        Theming.setTheme(Arrays.asList(new Pane[]{apnFade, apnRoot}));
        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);

        ChuckNorrisJokeTicker.setLabelPosition(jokeLabel);
        jokeLabel.setTranslateY(jokeLabel.getLayoutY() + 75);
        ChuckNorrisJokeTicker.moveLabel(jokeLabel);

        UserInterfaceUtils.initialize(apnFade, apnRoot, LoginController.class, btnFullscreen, errorContainer);

        // load user data
        userData = UserData.loadUserData();

        if (userData == null) {
            userData = new UserData();
        }

        txtUserName.setText(userData.getLoginUsername());
        password.setText(userData.getLoginPassword());
        rememberLogin.setSelected(userData.isLoginRemember());

        Platform.runLater(() -> {
            if (txtUserName.getText().equals("")) {
                txtUserName.requestFocus();
            } else if (password.getText().equals("")) {
                password.requestFocus();
            } else {
                btnLogin.requestFocus();
            }
        });

        UserData.deleteUserData();

        apnFade.setOpacity(0);

        // 1% meme chance
        if (new Random().nextFloat() < 0.01) {
            // needed because of root.getWidth/Height
            Platform.runLater(() -> ZuendorfMeme.setup(apnRoot));
        }
    }

    @FXML
    private void goToRegistration() {
        btnRegistration.setDisable(true);
        saveUserData();
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/register.fxml", apnFade);
        ChuckNorrisJokeTicker.stopAnimation();
    }

    @FXML
    private void login() {

        if (txtUserName.getText().isEmpty() || password.getText().isEmpty()) {
            NotificationHandler.getInstance().sendWarning("Please enter a username and a password.", logger);
            return;
        }

        if (rememberLogin.isSelected()) {
            saveUserData();
        } else {
            UserData.deleteUserData();
        }
        
        LoginUserRequest userRequest = new LoginUserRequest(txtUserName.getText(), password.getText());
        RequestUtil.request(userRequest).ifPresent(LoginController::setUserToken);

        if (userRequest.getSuccessful()) {
            btnLogin.setDisable(true);
            setUserName(txtUserName.getText());
            ChuckNorrisJokeTicker.stopAnimation();
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", apnFade);
        } else {
            NotificationHandler.getInstance().sendError("Login Failed, User data doesn't exist! ", logger);
            Image image = new Image(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/memes/joda.jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            Scene scene = new Scene(new VBox(imageView),510,375);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> stage.close());
            delay.play();
        }
    }

    @FXML
    private void toggleFullscreen() {
        UserInterfaceUtils.toggleFullscreen(btnFullscreen);
    }

    @FXML
    public void onEnter() {
        login();
    }

    private void saveUserData() {
        userData.setLoginUsername(txtUserName.getText());
        userData.setLoginPassword(password.getText());
        userData.setLoginRemember(rememberLogin.isSelected());

        SerializeUtil.serialize(UserData.USER_DATA_PATH.toString(),
            userData);
    }
}
