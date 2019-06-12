package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.uniks.se1ss19teamb.rbsg.model.Game;

import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.util.ErrorHandler;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainController {

    private static final Logger logger = LogManager.getLogger();
    @FXML
    private AnchorPane mainScreen;
    @FXML
    private AnchorPane errorContainer;
    @FXML
    private ListView<String> playerListView;
    @FXML
    private ScrollPane playerScrollPane;
    @FXML
    private ScrollPane gameScrollPane;
    @FXML
    private ListView<Parent> gameListView;
    @FXML
    private JFXButton btnCreate;
    @FXML
    private ToggleGroup playerNumberToggleGroup;
    @FXML
    private JFXTextField gameName;
    @FXML
    private Toggle twoPlayers;
    @FXML
    private Toggle fourPlayers;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private TabPane chat;
    private ErrorHandler errorHandler = ErrorHandler.getErrorHandler();

    private Game joinedGame;

    public void initialize() {

        mainScreen.setOpacity(0);
        UserInterfaceUtils.makeFadeInTransition(mainScreen);

        LoginController.getChatSocket().registerChatMessageHandler((message, from, isPrivate) -> {
            if (isPrivate) {
                /* TODO privates tab
                Tab tab = new Tab();
                tab.setText(from);
                tab.setContent(new Label(message));
                chat.getTabs().add(tab);
                System.out.println(message);
                */
            }
        });

        setGameListView();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
            .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/ErrorPopup.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            // controller not used yet, but it's good to have it for later purposes.
            ErrorPopupController controller = fxmlLoader.getController();
            errorHandler.setErrorPopupController(controller);
            errorContainer.getChildren().add(parent);

        } catch (IOException e) {
            errorHandler.sendError("Fehler beim Laden der FXML-Datei für die Lobby!", logger, e);
        }
    }

    private void setGameListView() {
        gameScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        gameScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        gameScrollPane.setStyle("-fx-background-color:transparent;");
        gameListView.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;"
            + "-fx-padding: 0px;");
        gameListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        updateGameView();
        updatePlayerView();
    }

    private void setPlayerListView() {
        playerScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        playerScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        playerListView.setStyle("-fx-background-color:transparent;");
        playerListView.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;"
            + "-fx-padding: 0px;");
        playerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ArrayList<String> existingPlayers = getExistingPlayers();
        for (String name : existingPlayers) {
            playerListView.getItems().add(name);
        }
    }

    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnCreate)) {
            if (!gameName.getText().isEmpty()) {
                Toggle selected = playerNumberToggleGroup.getSelectedToggle();
                String userKey = LoginController.getUserKey();
                if (selected.equals(twoPlayers)) {
                    new CreateGameRequest(gameName.getText(), 2, userKey).sendRequest();
                } else if (selected.equals(fourPlayers)) {
                    new CreateGameRequest(gameName.getText(), 4, userKey).sendRequest();
                }
                updateGameView();
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
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", mainScreen);
            }
        }
    }

    void updateGameView() {
        ObservableList items = gameListView.getItems();
        while (items.size() != 0) {
            items.remove(0);
        }

        ArrayList<Game> existingGames = getExistingGames();
        for (Game game : existingGames) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/gameField.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                GameFieldController controller = fxmlLoader.getController();
                controller.setUpGameLabel(game, this);
                gameListView.getItems().add(parent);
            } catch (IOException e) {
                errorHandler.sendError("Ein GameField konnte nicht geladen werden!");
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Game> getExistingGames() {
        String userKey = LoginController.getUserKey();
        QueryGamesRequest queryGamesRequest = new QueryGamesRequest(userKey);
        queryGamesRequest.sendRequest();
        return queryGamesRequest.getGames();
    }

    private void updatePlayerView() {
        playerScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        playerScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        playerListView.setStyle("-fx-background-color:transparent;");
        playerListView.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;"
            + "-fx-padding: 0px;");
        ObservableList playerList = playerListView.getItems();
        while (playerList.size() != 0) {
            playerList.remove(0);
        }

        ArrayList<String> existingPlayers = getExistingPlayers();
        for (String player : existingPlayers) {
            playerListView.getItems().add(player);
        }
    }

    private ArrayList<String> getExistingPlayers() {
        String userKey = LoginController.getUserKey();
        QueryUsersInLobbyRequest usersInLobbyRequest = new QueryUsersInLobbyRequest(userKey);
        usersInLobbyRequest.sendRequest();
        return usersInLobbyRequest.getUsersInLobby();
    }

    void setJoinedGame(Game joinedGame) {
        this.joinedGame = joinedGame;
    }
}

