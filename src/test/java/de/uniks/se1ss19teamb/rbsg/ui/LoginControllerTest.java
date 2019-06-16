package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class LoginControllerTest {

    Parent root;

    @Start
    public void start(Stage primaryStage) throws Exception {

        root = FXMLLoader
            .load(getClass().getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml"));
        primaryStage.setTitle("RSBG-Team B");
        primaryStage.setScene(new Scene(root, 800, 650));
        primaryStage.setMinWidth(533);
        primaryStage.setMinHeight(621);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();

        TextureManager.init();
    }

    @Test
    public void etwasTest(FxRobot robot) {
        System.out.println();
        AnchorPane anchorPane = robot.lookup("#errorContainer").queryAs(AnchorPane.class);
        System.out.println(anchorPane);
    }

}
