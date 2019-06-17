package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import de.uniks.se1ss19teamb.rbsg.model.Game;

import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    private TabPane chat;
    private NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();

    private Game joinedGame;

    private String path = "./src/main/resources/de/uniks/se1ss19teamb/rbsg/cssMode.json";

    @FXML
    private AnchorPane mainScreen1;

    private String whiteMode = "-fx-control-inner-background: white;" + "-fx-background-insets: 0;" +
        "-fx-padding: 0px;";
    private String darkMode = "-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0;" +
        "-fx-padding: 0px;";
    @FXML
    public JFXHamburger ham;
    @FXML
    private JFXButton logoutBtn;
    @FXML
    private JFXButton whiteModeBtn;

    LoginController loginController = new LoginController();

    public void initialize() {
        UserInterfaceUtils.makeFadeInTransition(mainScreen);

        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(ham);
        transition.setRate(-1);
        ham.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
            transition.setRate(transition.getRate()*-1);
            transition.play();
            if(transition.getRate() == 1) {
                whiteModeBtn.setVisible(true);
                logoutBtn.setVisible(true);
            }else{
                whiteModeBtn.setVisible(false);
                logoutBtn.setVisible(false);
            }
        });

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
            .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/popup.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            // controller not used yet, but it's good to have it for later purposes.
            PopupController controller = fxmlLoader.getController();
            notificationHandler.setPopupController(controller);
            errorContainer.getChildren().add(parent);

        } catch (IOException e) {
            notificationHandler.sendError("Fehler beim Laden der FXML-Datei für die Lobby!", logger, e);
        }
        if(!SerializeUtils.deserialize(new File(path), boolean.class)) {
            gameListView.setStyle(whiteMode);
            playerListView.setStyle(whiteMode);
            loginController.changeTheme(mainScreen, mainScreen1, path);
        } else {
            gameListView.setStyle(darkMode);
            playerListView.setStyle(darkMode);
            loginController.changeTheme(mainScreen, mainScreen1, path);
        }
    }

    private void setGameListView() {
        gameScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        gameScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        gameScrollPane.setStyle("-fx-background-color:transparent;");
        gameListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        updateGameView();
        updatePlayerView();
    }

    private void setPlayerListView() {
        playerScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        playerScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        playerListView.setStyle("-fx-background-color:transparent;");
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
                notificationHandler.sendWarning("Bitte geben Sie einen Namen für das Spiel ein.", logger);
            }
        }

        if(event.getSource().equals(whiteModeBtn) && whiteModeBtn.isVisible()){
            if(SerializeUtils.deserialize(new File(path), boolean.class)){
                playerListView.setStyle(whiteMode);
                gameListView.setStyle(whiteMode);
                loginController.changeTheme(mainScreen, mainScreen1, path);
                SerializeUtils.serialize(path, false);
            }else if (!SerializeUtils.deserialize(new File(path), boolean.class)) {
                playerListView.setStyle(darkMode);
                gameListView.setStyle(darkMode);
                loginController.changeTheme(mainScreen, mainScreen1, path);
                SerializeUtils.serialize(path, true);
            }

        }else if(event.getSource().equals(logoutBtn)){
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
                notificationHandler.sendWarning("Ein GameField konnte nicht geladen werden!", logger);
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

