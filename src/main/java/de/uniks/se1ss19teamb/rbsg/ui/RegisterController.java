package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.uniks.se1ss19teamb.rbsg.request.RegisterUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterController {

    private static final Logger logger = LogManager.getLogger();
    @FXML
    AnchorPane errorContainer;
    @FXML
    private AnchorPane registerScreen;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnConfirm;
    @FXML
    private JFXTextField userName;
    @FXML
    private JFXPasswordField password;
    @FXML
    private NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();
    @FXML
    private AnchorPane registerScreen1;

    LoginController loginController = new LoginController();

    private JFXPasswordField confirmPassword;



    public void initialize() {

        if(loginController.getBoolean()){
            registerScreen.getStylesheets().clear();
            registerScreen.getStylesheets().add("/de/uniks/se1ss19teamb/rbsg/css/dark-design.css");
            registerScreen1.getStylesheets().clear();
            registerScreen1.getStylesheets().add("/de/uniks/se1ss19teamb/rbsg/css/dark-design.css");
        }else{
            registerScreen.getStylesheets().clear();
            registerScreen.getStylesheets().add("/de/uniks/se1ss19teamb/rbsg/css/white-design.css");
            registerScreen1.getStylesheets().clear();
            registerScreen1.getStylesheets().add("/de/uniks/se1ss19teamb/rbsg/css/white-design.css");
        }

        UserInterfaceUtils.makeFadeInTransition(registerScreen);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/popup.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            errorContainer.getChildren().add(parent);

            PopupController controller = fxmlLoader.getController();
            notificationHandler.setPopupController(controller);

        } catch (IOException e) {
            notificationHandler.sendError("Fehler beim Laden der FXML-Datei für die Registrierung!", logger, e);
        }
    }


    @FXML
    void eventHandler(ActionEvent event) {
        if (event.getSource().equals(btnCancel)) {
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", registerScreen, loginController.getBoolean());
        }
        if (event.getSource().equals(btnConfirm)) {
            if (!userName.getText().isEmpty()
                && !password.getText().isEmpty()
                && !confirmPassword.getText().isEmpty()) {

                if (password.getText().equals(confirmPassword.getText())) {
                    RegisterUserRequest register = new RegisterUserRequest(
                        userName.getText(), password.getText());

                    register.sendRequest();
                    if (register.getSuccessful()) {
                        UserInterfaceUtils.makeFadeOutTransition(
                            "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", registerScreen, loginController.getBoolean());

                        notificationHandler.sendSuccess("Registrierung erfolgreich.", logger);
                    } /*else {
                        notificationHandler.sendError("Entschuldigung.
                         Es ist etwas bei der Registrierung schief gelaufen.
                        Bitte versuchen Sie er erneut.");
                    }*/
                } else {
                    notificationHandler.sendWarning("Die Passwörter sind verschieden!", logger);
                }

            } else {
                notificationHandler.sendWarning("Nicht alle Eingabefelder sind ausgefüllt!", logger);
            }
        }
    }
}