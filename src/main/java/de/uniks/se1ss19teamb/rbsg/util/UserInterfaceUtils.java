package de.uniks.se1ss19teamb.rbsg.util;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import java.io.IOException;

import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserInterfaceUtils {
    private static ErrorHandler errorHandler = ErrorHandler.getErrorHandler();
    private static final Logger logger = LogManager.getLogger();

    public static void makeFadeOutTransition(String path, AnchorPane node) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(750));
        fadeTransition.setNode(node);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> {
            try {
                node.getScene().setRoot(FXMLLoader.load(UserInterfaceUtils.class.getResource(path)));
            } catch (IOException e) {
                errorHandler.sendError("Übergang in die nächste Szene konnte nicht ausgeführt werden!", logger, e);
            }
        });
        fadeTransition.play();
    }

    public static void makeFadeInTransition(AnchorPane node) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(node);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    public static void toggleFullscreen(JFXButton btnFullscreen) {
        Stage stage = ((Stage) btnFullscreen.getScene().getWindow());

        if (stage.isFullScreen()) {
            btnFullscreen.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EXPAND));
        } else {
            btnFullscreen.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.COMPRESS));
        }

        stage.setFullScreen(!stage.isFullScreen());
    }

    //private void slideNextScene(String path, int value, AnchorPane pane) throws IOException {
    //    Random r = new Random();
    //    boolean randomValue = r.nextBoolean();
    //    Parent root = FXMLLoader.load(getClass().getResource(path));
    //    Timeline timeline;
    //    KeyValue keyValue;

    //    if (randomValue) {
    //        root.translateYProperty().setValue(-400);

    //        timeline = new Timeline();
    //        keyValue = new KeyValue(root.translateYProperty(),0, Interpolator.EASE_IN);
    //    } else {
    //        root.translateXProperty().setValue(-value);

    //        timeline = new Timeline();
    //        keyValue = new KeyValue(root.translateXProperty(),0,Interpolator.EASE_IN);
    //    }

    //    KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),keyValue);
    //    timeline.getKeyFrames().add(keyFrame);
    //    timeline.play();

    //    Scene scene = new Scene(root);
    //    Stage stage = (Stage) pane.getScene().getWindow();
    //    stage.setScene(scene);
    //}
}
