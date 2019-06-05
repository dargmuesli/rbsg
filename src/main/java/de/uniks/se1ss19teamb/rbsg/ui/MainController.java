package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;

public class MainController {
    @FXML
    private AnchorPane mainScreen;
    
    @FXML
    private AnchorPane errorContainer;

    @FXML
    private ListView playerListView;

    @FXML
    private ScrollPane scrollPanePlayer;
    
    public void initialize() {
        mainScreen.setOpacity(0);
        UserInterfaceUtils.makeFadeInTransition(mainScreen);
        setPlayerListView();
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

    private void setPlayerListView(){
        scrollPanePlayer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPanePlayer.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPanePlayer.setStyle("-fx-background-color:transparent;");
        playerListView.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;");
        //(Test player) TODO : put the real player into the playerListView
        playerListView.getItems().add("test player");
        playerListView.getItems().add("test player 2");
        playerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
}
