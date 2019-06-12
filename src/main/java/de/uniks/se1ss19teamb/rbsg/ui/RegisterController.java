package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.uniks.se1ss19teamb.rbsg.request.RegisterUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
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
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private ErrorHandler errorHandler = ErrorHandler.getErrorHandler();
    private JFXPasswordField passwordRepeat;

    public void initialize() {
        registerScreen.setOpacity(0);
        UserInterfaceUtils.makeFadeInTransition(registerScreen);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/ErrorPopup.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            errorContainer.getChildren().add(parent);

            ErrorPopupController controller = fxmlLoader.getController();
            errorHandler.setErrorPopupController(controller);
        } catch (IOException e) {
            errorHandler.sendError("Fehler beim Laden der FXML-Datei für die Registrierung!", logger, e);
        }
    }

    @FXML
    void eventHandler(ActionEvent event) {
        if (event.getSource().equals(btnCancel)) {
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", registerScreen);
        } else if (event.getSource().equals(btnConfirm)) {
            if (username.getText().isEmpty()
                || password.getText().isEmpty()
                || passwordRepeat.getText().isEmpty()) {
                errorHandler.sendError("Bitte geben Sie etwas ein.");
                return;
            }

            if (!password.getText().equals(passwordRepeat.getText())) {
                errorHandler.sendError("Die Passwörter sind verschieden!");
                return;
            }

            RegisterUserRequest register = new RegisterUserRequest(
                username.getText(), password.getText());
            register.sendRequest();

            if (!register.getSuccessful()) {
                errorHandler.sendError("Die Registrierung ist fehlgeschlagen!");
            }

            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", registerScreen);

            // TODO success
            errorHandler.sendError("Registrierung erfolgreich!");
        }
    }
}