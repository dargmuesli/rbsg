package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import de.uniks.se1ss19teamb.rbsg.request.CreateGameRequest;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.QueryUsersInLobbyRequest;
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
    private ListView<String> listViewPlayer;

    @FXML
    private ScrollPane scrollPanePlayer;

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
    
    @FXML
    private JFXButton btnLogout;

    private ErrorHandler errorHandler;

    private static final Logger logger = LogManager.getLogger(MainController.class);

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
            errorHandler = ErrorHandler.getErrorHandler();
            errorHandler.setErrorPopupController(controller);
            errorContainer.getChildren().add(parent);
            
        } catch (IOException e) {
            errorHandler.sendError("Fehler beim Laden der FXML-Datei für die Lobby!");
            logger.error(e);
        }
    }


    private void setPlayerListView() {
        scrollPanePlayer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPanePlayer.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPanePlayer.setStyle("-fx-background-color:transparent;");
        listViewPlayer.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;");
        listViewPlayer.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ArrayList<String> existingPlayers = getExistingPlayers();
        for (String name :existingPlayers ){
            listViewPlayer.getItems().add(name);
        }
    }
    
    public void eventHandler(ActionEvent event) throws IOException {
        
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
        
        if (event.getSource().equals(btnLogout)) {
            LogoutUserRequest logout = new LogoutUserRequest(LoginController.getUserKey());
            logout.sendRequest();
            if (logout.getSuccessful()) {
                LoginController.setUserKey(null);
                UserInterfaceUtils.makeFadeOutTransition(
                        "/de/uniks/se1ss19teamb/rbsg/login.fxml", mainScreen);
            }
        }
    }

    private ArrayList<String> getExistingPlayers(){
        String userKey = LoginController.getUserKey();
        QueryUsersInLobbyRequest usersInLobbyRequest = new QueryUsersInLobbyRequest(userKey);
        usersInLobbyRequest.sendRequest();
       return usersInLobbyRequest.getUsersInLobby();
    }
}
