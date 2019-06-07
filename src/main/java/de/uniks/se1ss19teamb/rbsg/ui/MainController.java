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

    @FXML
    private ScrollPane scrollPaneGame;

    @FXML
    private ListView gameListView;
    
    public void initialize() {
        mainScreen.setOpacity(0);
        UserInterfaceUtils.makeFadeInTransition(mainScreen);
        setGameListView();
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

    private void setPlayerListView() {
        scrollPanePlayer.setStyle("-fx-background-color:transparent;");
        playerListView.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;"
                + "-fx-padding: 0px;");
        playerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // TODO : put the real player into the playerListView, (i create something to check the method)
        playerListView.getItems().add("test player");
        for (int i = 0; i < 100; i++) {
            playerListView.getItems().add("new Item");
        }

    }

    private void setGameListView() {
        scrollPaneGame.setStyle("-fx-background-color:transparent;");
        gameListView.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;"
                + "-fx-padding: 0px;");
        gameListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //TODO : create Player and put into the gameListView; ( i put something to check the method )
        gameListView.getItems().add("Dies ist ein Test");
        for (int i = 0; i < 120; i++) {
            gameListView.getItems().add("mytest");
        }

    }
}
