package de.uniks.se1ss19teamb.rbsg.util;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.ui.DragMoveResize;
import de.uniks.se1ss19teamb.rbsg.ui.GameLobbyController;
import de.uniks.se1ss19teamb.rbsg.ui.LoginController;
import de.uniks.se1ss19teamb.rbsg.ui.PopupController;

import java.io.IOException;
import java.util.Arrays;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserInterfaceUtils {
    private static final Logger logger = LogManager.getLogger();

    public static void logout(Pane pane, JFXButton btnLogout) {
        btnLogout.setDisable(true);

        if (!RequestUtil.request(new LogoutUserRequest(LoginController.getUserToken()))) {
            return;
        }

        LoginController.setUserToken(null);
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", pane);
    }

    public static void makeFadeOutTransition(String path, Node node) {
        FadeTransition fadeTransition = setTransition(node);
        fadeTransition.setOnFinished(event -> {
            try {
                node.getScene().setRoot(FXMLLoader.load(UserInterfaceUtils.class.getResource(path)));
            } catch (IOException e) {
                NotificationHandler.getInstance().sendError(
                    "\u00DCbergang in die n\u00E4chste Szene konnte nicht ausgef\u00FChrt werden!", logger, e);
            }
        });
        fadeTransition.play();
    }

    public static void makeFadeOutTransition(String path, Node node, Node chatWindow) {
        FadeTransition fadeTransition = setTransition(node);
        fadeTransition.setOnFinished(event -> {
            try {
                DragMoveResize.makeChangeable((Region) chatWindow);
                AnchorPane pane = FXMLLoader.load(UserInterfaceUtils.class.getResource(path));
                pane.getChildren().add(chatWindow);
                node.getScene().setRoot(pane);
            } catch (IOException e) {
                NotificationHandler.getInstance().sendError(
                    "\u00DCbergang in die n\u00E4chste Szene konnte nicht ausgef\u00FChrt werden!", logger, e);
            }
        });
        fadeTransition.play();
    }

    private static FadeTransition setTransition(Node node) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(750));
        fadeTransition.setNode(node);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        return fadeTransition;
    }

    private static void makeFadeInTransition(Node node) {
        node.setOpacity(0);
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(node);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    public static void toggleFullscreen(JFXButton btnFullscreen) {
        Stage stage = ((Stage) btnFullscreen.getScene().getWindow());

        stage.setFullScreen(!stage.isFullScreen());

        updateBtnFullscreen(btnFullscreen);
    }

    public static void updateBtnFullscreen(JFXButton btnFullscreen) {
        Platform.runLater(() -> {
            Stage stage = ((Stage) btnFullscreen.getScene().getWindow());

            if (stage.isFullScreen()) {
                btnFullscreen.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.COMPRESS));
            } else {
                btnFullscreen.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EXPAND));
            }
        });
    }

    public static void initialize(
        Pane root, Pane rootChild, Class clazz, JFXButton btnFullscreen, Pane errorContainer) {

        UserInterfaceUtils.makeFadeInTransition(root);

        Theming.setTheme(Arrays.asList(root, rootChild));

        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);

        FXMLLoader fxmlLoader = new FXMLLoader(clazz.getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/popup.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            // controller not used yet, but it's good to have it for later purposes.
            PopupController controller = fxmlLoader.getController();
            NotificationHandler.getInstance().setPopupController(controller);
            Platform.runLater(() -> {
                errorContainer.getChildren().add(parent);
                errorContainer.toFront();
            });
        } catch (IOException e) {
            NotificationHandler.getInstance()
                .sendError("Fehler beim Laden der FXML-Datei für die Lobby!", logger, e);
        }
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
