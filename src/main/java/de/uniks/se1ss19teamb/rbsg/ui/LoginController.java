package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.model.UserData;
import de.uniks.se1ss19teamb.rbsg.request.LoginUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginController {

    private static final Logger logger = LogManager.getLogger();

    private static String user;
    private static String userKey;
    private static UserData userData;
    private String[] jokes = {"Chuck Norris hat bis zur Unendlichkeit gezählt ... 2-mal.", "Chuck Norris kann schwarze " +
        "Filzstifte nach Farbe sortieren.", "Chuck Norris hat alle Farben erfunden. Außer Rosa! Tom Cruise hat Rosa " +
        "erfunden.", "Einige Leute tragen Superman Schlafanzüge. Superman trägt Chuck Norris Schlafanzüge.", "Chuck " +
        "Norris kann ein Feuer entfachen, indem er zwei Eiswürfel aneinander reibt.", "Chuck Norris kann Drehtüren " +
        "zuschlagen!", "Manche Menschen können viele Liegestützen — Chuck Norris kann alle.", "Chuck Norris wurde " +
        "gestern geblitzt — beim Einparken", "Chuck Norris verzichtet auf seine Rechte — seine Linke ist sowieso " +
        "schneller ...", "Chuck Norris kennt die letzte Ziffer von Pi.", "Chuck Norris trinkt seinen Kaffee am liebsten" +
        " schwarz. Ohne Wasser.", "Chuck Norris wurde letztens von der Polizei angehalten ... — Die Polizisten sind " +
        "mit einer Verwarnung davon gekommen.", "Chuck Norris ist Fallschirmspringen gegangen. Sein Fallschirm hat sich" +
        " nicht geöffnet. Er ist den Fallschirm danach umtauschen gegangen.", "Arnold Schwarzenegger musste wegen " +
        "schweren Verletzungen ins Krankenhaus eingeliefert werden. Chuck Norris hatte ihn auf Facebook angestupst."};
    private int random;
    private static AnimationTimer animationTimer;


    @FXML
    private AnchorPane loginScreen;
    @FXML
    private JFXTextField userName;
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
    private AnchorPane loginScreen1;
    @FXML
    private Label jokeLabel;

    static String getUser() {
        return user;
    }

    private static void setUser(String name) {
        user = name;
    }

    public static String getUserKey() {
        return userKey;
    }

    public static void setUserKey(String key) {
        userKey = key;
    }

    public void initialize() {
        random = (int) (Math.random() * jokes.length);
        jokeLabel.setText(jokes[random]);
        jokeLabel.setLayoutX(-500);
        Theming.setTheme(Arrays.asList(new Pane[]{loginScreen, loginScreen1}));

        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (jokeLabel.getLayoutX() != 2200) {
                    jokeLabel.setLayoutX(jokeLabel.getLayoutX()+2);
                    return;
                } else if (jokeLabel.getLayoutX() == 2200) {
                    random = (int) (Math.random() * jokes.length);
                    jokeLabel.setText(jokes[random]);
                    jokeLabel.setLayoutX(-500);
                }
            }
        };
        animationTimer.start();

        // load user data
        userData = UserData.loadUserData(NotificationHandler.getInstance());

        if (userData == null) {
            userData = new UserData();
        }

        userName.setText(userData.getLoginUsername());
        password.setText(userData.getLoginPassword());
        rememberLogin.setSelected(userData.isLoginRemember());

        Platform.runLater(() -> {
            if (userName.getText().equals("")) {
                userName.requestFocus();
            } else if (password.getText().equals("")) {
                password.requestFocus();
            } else {
                btnLogin.requestFocus();
            }
        });

        UserData.deleteUserData(NotificationHandler.getInstance());

        loginScreen.setOpacity(0);
        UserInterfaceUtils.makeFadeInTransition(loginScreen);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/popup.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            errorContainer.getChildren().add(parent);

            PopupController controller = fxmlLoader.getController();
            NotificationHandler.getInstance().setPopupController(controller);

        } catch (IOException e) {
            NotificationHandler.getInstance().sendError("Fehler beim Laden der FXML-Datei für den Login!", logger, e);
        }
    }

    @FXML
    void eventHandler(ActionEvent event) {
        if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        } else if (event.getSource().equals(btnLogin)) {
            login();
        } else if (event.getSource().equals(btnRegistration)) {
            goToRegister();
            animationTimer.stop();
        }
    }

    @FXML
    public void onEnter() {
        login();
    }

    private void login() {
        if (userName.getText().isEmpty() || password.getText().isEmpty()) {
            NotificationHandler.getInstance().sendWarning("Bitte geben Sie Benutzernamen und Passwort ein.", logger);
            return;
        }

        LoginUserRequest login = new LoginUserRequest(userName.getText(), password.getText());
        login.sendRequest();

        if (!login.getSuccessful()) {
            NotificationHandler.getInstance().sendWarning("Login fehlgeschlagen!", logger);
            return;
        }

        if (rememberLogin.isSelected()) {
            saveUserData();
        } else {
            UserData.deleteUserData(NotificationHandler.getInstance());
        }

        setUserKey(login.getUserKey());
        setUser(userName.getText());
        animationTimer.stop();
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", loginScreen);
    }

    private void goToRegister() {
        saveUserData();
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/register.fxml", loginScreen);
    }

    private void saveUserData() {
        userData.setLoginUsername(userName.getText());
        userData.setLoginPassword(password.getText());
        userData.setLoginRemember(rememberLogin.isSelected());

        SerializeUtils.serialize(UserData.USER_DATA_PATH.toString(),
            userData);
    }
}