package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainController {
    @FXML
    private AnchorPane mainScreen;

    @FXML
    private AnchorPane errorContainer;

    @FXML
    private TabPane chat;

    public void initialize() {
        mainScreen.setOpacity(0);
        makeFadeInTransition();

        chat.getTabs().add(addTab("tab1"));
        chat.getTabs().add(addTab("tab2"));
        chat.getTabs().add(addTab("tab3"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/ErrorPopup.fxml"));
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

    private Tab addTab(String tabname) {

        Pane pane = new Pane();
        pane.setMaxHeight(140);
        pane.setMaxWidth(180);
        pane.setMinHeight(100);
        pane.setMinWidth(140);

        Button send = new Button();
        send.setText("senden");
        send.setMaxHeight(40);
        send.setMaxWidth(40);

        TextField textField = new TextField();
        textField.setMaxHeight(40);
        textField.setMaxWidth(140);

        HBox hBox = new HBox();
        hBox.setMaxHeight(40);
        hBox.setMaxWidth(180);
        hBox.getChildren().addAll(textField, send);

        VBox vBox = new VBox();
        vBox.getChildren().setAll(pane, hBox);

        Tab tab = new Tab();
        tab.setText(tabname);
        tab.setContent(vBox);

        return tab;
    }
}
