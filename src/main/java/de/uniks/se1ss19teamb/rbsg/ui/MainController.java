package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;

import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;

public class MainController {
    @FXML
    private AnchorPane mainScreen;

    @FXML
    private AnchorPane errorContainer;

    public void initialize(){
        mainScreen.setOpacity(0);
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

    private void makeFadeInTransition() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(mainScreen);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }
}
