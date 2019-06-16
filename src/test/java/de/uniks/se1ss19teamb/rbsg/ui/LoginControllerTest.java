package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
class LoginControllerTest {

    @Start
    void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader
            .load(getClass().getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml"));
        primaryStage.setTitle("RSBG-Team B");
        primaryStage.setScene(new Scene(root, 800, 650));
        primaryStage.setMinWidth(533);
        primaryStage.setMinHeight(621);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        // AnchorPane loginScreen = (AnchorPane) root.lookup("#loginScreen");
        // AnchorPane errorContainer = (AnchorPane) root.lookup("#errorContainer");
        // JFXButton btnFullscreen = (JFXButton) loginScreen.getChildren().get(2);
        // VBox box = (VBox) loginScreen.getChildren().get(1);
        primaryStage.show();

        TextureManager.init();
    }

    @Test
    void contentTest(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#btnLogin").queryAs(JFXButton.class)).hasText("Login");
        Assertions.assertThat(robot.lookup("#btnRegistration").queryAs(JFXButton.class)).hasText("Registration");
        Assertions.assertThat(robot.lookup("#rememberLogin").queryAs(JFXCheckBox.class)).hasText("Remember Login");
        Assertions.assertThat(robot.lookup("#password").queryAs(JFXPasswordField.class)).isVisible();
        Assertions.assertThat(robot.lookup("#userName").queryAs(JFXTextField.class)).isVisible();
        Assertions.assertThat(robot.lookup("#btnFullscreen").queryAs(JFXButton.class)).hasText("Fullscreen");
    }

    @Test
    void clickedTest(FxRobot robot) {
        robot.clickOn("#btnFullscreen");
        robot.clickOn("#btnFullscreen");
    }

    @Test
    void loginTest(FxRobot robot) {
        robot.clickOn("#userName");
        robot.write("MyNewName");
        robot.clickOn("#password");
        robot.write("blutangel90");
        robot.clickOn("#rememberLogin");
        robot.clickOn("#btnLogin");
    }

}
