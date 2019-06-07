package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.uniks.se1ss19teamb.rbsg.model.Game;
import de.uniks.se1ss19teamb.rbsg.request.CreateGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.QueryGamesRequest;
import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    @FXML
    private JFXButton btnCreate;
    
    @FXML
    private ToggleGroup playerNumberToggleGroup;
    
    @FXML
    private JFXTextField gameName;
    
    @FXML
    private Toggle twoPlayers;
    
    @FXML
    private Toggle threePlayers;
    
    @FXML
    private Toggle fourPlayers;

    private ErrorHandler errorHandler = new ErrorHandler();

    private static final Logger logger = LogManager.getLogger(MainController.class);

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
            errorHandler = new ErrorHandler();
            errorHandler.setErrorPopupController(controller);
            errorContainer.getChildren().add(parent);
            
        } catch (IOException e) {
            errorHandler.sendError("Fehler beim Laden der FXML-Datei für die Lobby!");
            logger.error(e);
        }
    }

    private void setPlayerListView() {
        scrollPanePlayer.setStyle("-fx-background-color:transparent;");
        playerListView.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;"
                + "-fx-padding: 0px;");
        playerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // TODO : put the real player into the playerListView, (i create something to check the method)
        playerListView.getItems().add("test player");


    }

    private void setGameListView() {
        scrollPaneGame.setStyle("-fx-background-color:transparent;");
        gameListView.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;"
                + "-fx-padding: 0px;");
        gameListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //TODO : create Player and put into the gameListView; ( i put something to check the method )
        gameListView.getItems().add("Dies ist ein Test");
        String userKey = LoginController.getUserKey();
        QueryGamesRequest gamesRequest = new QueryGamesRequest(userKey);
        for (Game game : gamesRequest.getGames()) {
            gameListView.getItems().addAll(game.getName());
        }
    }

    
    public void setOnAction(ActionEvent event) throws IOException {
        
        if (event.getSource().equals(btnCreate)) {
            if (!gameName.getText().isEmpty()) {
                Toggle selected = playerNumberToggleGroup.getSelectedToggle();
                String userKey = LoginController.getUserKey();
                CreateGameRequest game;
                if (selected.equals(twoPlayers)) {
                    game = new CreateGameRequest(gameName.getText(), 2, userKey);
                } else if (selected.equals(threePlayers)) {
                    game = new CreateGameRequest(gameName.getText(), 3, userKey);
                } else {
                    game = new CreateGameRequest(gameName.getText(), 4, userKey);
                }
                game.sendRequest();
            } else {
                errorHandler.sendError("Bitte geben Sie einen Namen für das Spiel ein.");
            }
        }
    }
}
