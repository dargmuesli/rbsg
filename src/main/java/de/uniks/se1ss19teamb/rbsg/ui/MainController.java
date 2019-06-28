package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.uniks.se1ss19teamb.rbsg.chat.Chat;
import de.uniks.se1ss19teamb.rbsg.model.GameMeta;

import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.SystemSocket;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainController {

    private static final Logger logger = LogManager.getLogger();
    private static NotificationHandler notificationHandler = NotificationHandler.getNotificationHandler();

    private static Path chatLogPath = Paths.get("src/java/resources/de/uniks/se1ss19teamb/rbsg/chatLog.txt");
    private static Chat chat;
    private static SingleSelectionModel<Tab> selectionModel;
    private static String userKey = LoginController.getUserKey();
    private static String userName = LoginController.getUser();
    private static String sendTo = null;
    private static HashMap<String, GameMeta> existingGames;

    @FXML
    private AnchorPane errorContainer;
    @FXML
    private AnchorPane mainScreen1;
    @FXML
    private AnchorPane mainScreen;
    @FXML
    private JFXButton btnArmyManager;
    @FXML
    private JFXButton btnCreate;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnMinimize;
    @FXML
    private JFXButton btnMode;
    @FXML
    private JFXButton btnSend;
    @FXML
    private JFXHamburger hamburgerMenu;
    @FXML
    private JFXTabPane chatPane;
    @FXML
    private JFXTextField gameName;
    @FXML
    private ListView<Label> playerListView;
    @FXML
    private ListView<Parent> gameListView;
    @FXML
    private ScrollPane allPane;
    @FXML
    private TextField message;
    @FXML
    private Toggle fourPlayers;
    @FXML
    private Toggle twoPlayers;
    @FXML
    private ToggleGroup playerNumberToggleGroup;
    @FXML
    private VBox chatBox;
    @FXML
    private VBox chatWindow;
    @FXML
    private VBox textArea;

    public void initialize() {
        UserInterfaceUtils.makeFadeInTransition(mainScreen);

        Theming.setTheme(Arrays.asList(new Pane[]{mainScreen, mainScreen1}));

        // TODO - after some time it automaticly disconnects system and chatSocket
        if (SystemSocket.instance == null) {
            SystemSocket.instance = new SystemSocket(userKey);

            SystemSocket.instance.registerUserJoinHandler(
                (name) -> {
                    updatePlayerView();
                    addElement(name, " joined.", textArea, false);
                });

            SystemSocket.instance.registerUserLeftHandler(
                (name) -> {
                    addElement(name, " left.", textArea, false);
                    updatePlayerView();
                });

            SystemSocket.instance.registerGameCreateHandler(
                (gameName, id, neededPlayers) -> {
                    updateGameView();
                    addElement(null, "Game \"" + gameName + "\" was created for " + neededPlayers + " players.",
                        textArea, false);
                });

            SystemSocket.instance.registerGameDeleteHandler(
                (id) -> {
                    addElement(null, "Game \"" + existingGames.get(id).getName() + "\" was deleted.",
                        textArea, false);
                    updateGameView();
                });

            SystemSocket.instance.connect();
        }

        if (ChatSocket.instance == null) {
            ChatSocket.instance = new ChatSocket(userName, userKey);

            ChatSocket.instance.registerChatMessageHandler((message, from, isPrivate) -> {
                if (isPrivate) {
                    addNewPane(from, message, false, chatPane);
                } else {
                    addElement(from, message, textArea, false);
                }
            });
        }

        if (MainController.chat == null) {
            MainController.chat = new Chat(ChatSocket.instance, chatLogPath);
        }

        updateGameView();
        updatePlayerView();

        Platform.runLater(() -> {
            Theming.hamburgerMenuTransition(hamburgerMenu, btnFullscreen);
            Theming.hamburgerMenuTransition(hamburgerMenu, btnLogout);
            Theming.hamburgerMenuTransition(hamburgerMenu, btnMode);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/popup.fxml"));

            try {
                Parent parent = fxmlLoader.load();
                // controller not used yet, but it's good to have it for later purposes.
                PopupController controller = fxmlLoader.getController();
                notificationHandler.setPopupController(controller);
                Platform.runLater(() -> errorContainer.getChildren().add(parent));
            } catch (IOException e) {
                notificationHandler.sendError("Fehler beim Laden der FXML-Datei für die Lobby!", logger, e);
            }

            // ChatTabController
            chatPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    if (t1.getText().equals("All")) {
                        setAll();
                    } else {
                        setPrivate(t1.getText(), -1);
                    }
                }
            );

            message.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (observable.getValue().substring(0, 3).toLowerCase().contains("/w ")) {
                        for (int i = 4; i < observable.getValue().length(); i++) {
                            if (observable.getValue().toCharArray()[i] == ' ') {
                                setPrivate(observable.getValue(), i);
                                break;
                            }
                        }
                    } else if (observable.getValue().substring(0, 4).toLowerCase().contains("/all")) {
                        if (observable.getValue().substring(4, 5).contains(" ")) {
                            selectionModel.select(chatPane.getTabs().get(0));
                            setAll();
                        }
                    }
                } catch (Exception e) {
                    // do nothing
                }
            });

            selectionModel = chatPane.getSelectionModel();
        });

        textArea.heightProperty().addListener(observable -> allPane.setVvalue(1D));

        chatWindow.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> this.message.requestFocus());
    }

    @FXML
    void eventHandler(ActionEvent event) {
        if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
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
            } else {
                notificationHandler.sendWarning("Bitte geben Sie einen Namen für das Spiel ein.", logger);
            }
        } else if (event.getSource().equals(btnLogout)) {
            LogoutUserRequest logout = new LogoutUserRequest(LoginController.getUserKey());
            logout.sendRequest();

            if (logout.getSuccessful()) {
                chatWindow.setId("none"); // renaming id so it will not be give to login
                LoginController.setUserKey(null);
                UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", mainScreen);
            }
        } else if (event.getSource().equals(btnArmyManager)) {
            ArmyManagerController.joiningGame = false;
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/armyManager.fxml", mainScreen);
        } else if (event.getSource().equals(btnSend)) {
            if (!message.getText().isEmpty()) {
                if (checkInput(message.getText())) {
                    return;
                }

                if (sendTo != null) {
                    if (sendTo.trim().equals("")) {
                        sendTo = null;
                        chat.sendMessage(message.getText());
                    } else {
                        chat.sendMessage(message.getText(), sendTo);
                        addNewPane(sendTo, message.getText(), true, chatPane);
                    }
                } else {
                    chat.sendMessage(message.getText());
                }

                message.setText("");
            }
        } else if (event.getSource().equals(btnMode)) {
            SerializeUtils.serialize(Theming.cssModeFile.getAbsolutePath(), !Theming.darkModeActive());
            Theming.setTheme(Arrays.asList(new Pane[]{mainScreen, mainScreen1}));

            // the game view contains sub-fxmls and thus needs to be updated separately
            updateGameView();
        } else if (event.getSource().equals(btnMinimize)) {
            if (chatBox.isVisible()) {
                chatBox.setVisible(false);
                chatBox.setMaxHeight(0);
                chatBox.setMaxWidth(0);
                chatWindow.setAlignment(Pos.BOTTOM_LEFT);
                chatWindow.setPadding(new Insets(0, 0, 0, 15));
                btnMinimize.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.WINDOW_MAXIMIZE));
            } else {
                chatBox.setVisible(true);
                chatBox.setMaxHeight(Region.USE_COMPUTED_SIZE);
                chatBox.setMaxWidth(Region.USE_COMPUTED_SIZE);
                chatWindow.setAlignment(Pos.CENTER);
                chatWindow.setPadding(new Insets(0));
                btnMinimize.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.WINDOW_MINIMIZE));
            }
            // TODO: find a better place for tictactoe, or add hotkeys like for easter eggs
            /*} else if (event.getSource().equals(btnTicTacToe)) {
                try {
                    Parent root = FXMLLoader
                        .load(getClass().getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/tictactoe.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root, 800, 600));
                    stage.show();
                    stage.setResizable(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
        }

        hamburgerMenu.requestFocus();
    }

    private static HashMap<String, GameMeta> getExistingGames() {
        String userKey = LoginController.getUserKey();
        QueryGamesRequest queryGamesRequest = new QueryGamesRequest(userKey);
        queryGamesRequest.sendRequest();
        return queryGamesRequest.getGames();
    }

    private void updateGameView() {
        existingGames = getExistingGames();

        Platform.runLater(() -> {
            ObservableList items = gameListView.getItems();

            while (items.size() != 0) {
                items.remove(0);
            }

            existingGames.forEach((s, gameMeta) -> {
                FXMLLoader fxmlLoader = new FXMLLoader(MainController.class
                    .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/gameField.fxml"));

                try {
                    Parent parent = fxmlLoader.load();
                    GameFieldController controller = fxmlLoader.getController();
                    controller.setUpGameLabel(gameMeta);
                    gameListView.getItems().add(parent);
                } catch (IOException e) {
                    notificationHandler.sendError("Ein GameField konnte nicht geladen werden!", logger, e);
                }
            });
        });
    }

    private void updatePlayerView() {
        Platform.runLater(() -> {
            ObservableList playerList = playerListView.getItems();

            while (playerList.size() != 0) {
                playerList.remove(0);
            }

            for (String player : getExistingPlayers()) {
                playerListView.getItems().add(addPlayerlabel(player));
            }
        });
    }

    private ArrayList<String> getExistingPlayers() {
        String userKey = LoginController.getUserKey();
        QueryUsersInLobbyRequest usersInLobbyRequest = new QueryUsersInLobbyRequest(userKey);
        usersInLobbyRequest.sendRequest();
        return usersInLobbyRequest.getUsersInLobby();
    }

    private Label addPlayerlabel(String player) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem whisperMenuItem = new MenuItem("whisper");
        contextMenu.getItems().add(whisperMenuItem);
        contextMenu.setOnAction(e -> setPrivate(player, -1));

        contextMenu.setStyle("-fx-background-color:transparent;");
        contextMenu.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;"
            + "-fx-padding: 0px;");

        Label plabel = new Label(player);
        plabel.setContextMenu(contextMenu);
        plabel.setMaxWidth(Double.MAX_VALUE);
        plabel.setMaxHeight(Double.MAX_VALUE);

        plabel.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    setPrivate(player, -1);
                }
            }
        });

        return plabel;
    }

    // ChatTabController
    private void addElement(String player, String message, VBox box, boolean whisper) {

        VBox container = new VBox();
        container.maxWidthProperty().bind(chatPane.widthProperty().multiply(0.98));

        if (player != null) {
            Label name = new Label(player + ":");
            name.setPadding(new Insets(5));
            name.setWrapText(true);
            if (whisper) {
                name.setStyle("-fx-text-fill: -fx-privatetext;");
            } else {
                name.setStyle("-fx-text-fill: black;");
            }
            // whisper on double click
            name.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        setPrivate(player, -1);
                    }
                }
            });
            Label text = new Label(message);
            text.setPadding(new Insets(5));
            text.setWrapText(true);
            setChatStyle(text);

            Platform.runLater(() -> {
                name.setMaxWidth(Region.USE_COMPUTED_SIZE);
                setChatStyle(name);
            });

            container.getChildren().addAll(name, text);
        } else {
            Label text = new Label(message);
            text.setPadding(new Insets(5));
            text.setWrapText(true);
            setChatStyle(text);

            container.getChildren().add(text);
        }

        Platform.runLater(() -> box.getChildren().add(container));
    }

    private void setChatStyle(Label label) {
        label.setStyle("-fx-text-fill: " + (Theming.darkModeActive()
            ? "-fx-primary" : "black") + ";"
            + "-fx-background-color: " + (Theming.darkModeActive()
            ? "-fx-secondary" : "white") + ";"
            + "-fx-border-radius: 20px;"
            + "-fx-background-radius: 10px;");
    }

    private void addNewPane(String from, String message, boolean mymessage, JFXTabPane pane) {
        boolean createTab = true;
        for (Tab t : pane.getTabs()) {
            if (t.getText().equals(from)) {
                if (mymessage) {
                    getPrivate(userName, message, t);
                    createTab = false;
                } else {
                    getPrivate(from, message, t);
                    createTab = false;
                }
            }
        }
        if (createTab) {
            Platform.runLater(
                () -> {
                    try {
                        Tab newTab = FXMLLoader
                            .load(this.getClass().getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/privateTab.fxml"));
                        newTab.setText(from);
                        pane.getTabs().add(newTab);
                        if (mymessage) {
                            getPrivate(userName, message, newTab);
                        } else {
                            getPrivate(from, message, newTab);
                        }
                    } catch (IOException e) {
                        notificationHandler.sendError("Ein GameField konnte nicht geladen werden!", logger, e);
                    }
                }
            );
        }
    }

    private void getPrivate(String from, String message, Tab tab) {
        ScrollPane scrollPane = (ScrollPane) tab.getContent();
        VBox area = (VBox) scrollPane.getContent();
        if (message != null) {
            addElement(from, message, area, true);
        }
        selectionModel.select(tab);
    }

    private boolean checkInput(String input) {
        if (input.length() < 4) {
            if (input.substring(0, 2).equals("//")) {
                Window window = btnFullscreen.getScene().getWindow();
                Image image = new Image(getClass()
                    .getResource("/de/uniks/se1ss19teamb/rbsg/memes/Comment.jpg").toExternalForm());
                ImageView imageView = new ImageView(image);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("IF ERROR");
                alert.setHeaderText(null);
                alert.setContentText("IF error THEN me");
                alert.setGraphic(imageView);
                alert.initOwner(window);
                alert.show();
            }
            return false;
        } else if (input.substring(0, 3).toLowerCase().contains("/w ")) {
            setPrivate(input, 0);
            return true;
        } else if (input.substring(0, 4).toLowerCase().contains("/all") && input.length() == 4) {
            selectionModel.select(chatPane.getTabs().get(0));
            setAll();
            return true;
        } else {
            return false;
        }
    }

    private void setPrivate(String input, int count) {
        if (count == -1) {
            sendTo = input;
        } else if (count == 0) {
            sendTo = input.substring(3);
        } else {
            sendTo = input.substring(3, count);
        }
        Platform.runLater(() -> {
            addNewPane(sendTo, null, true, chatPane);
            message.clear();
            message.setStyle("-fx-text-fill: -fx-privatetext;"
                + "-jfx-focus-color: -fx-privatetext;");
        });
    }

    private void setAll() {
        sendTo = null;

        Platform.runLater(() -> {
            message.clear();
            message.setStyle("-fx-text-fill: " + (Theming.darkModeActive()
                ? "-fx-secondary;" : "black;") + "-jfx-focus-color: "
                + (Theming.darkModeActive()
                ? "-fx-secondary;" : "black;"));
        });
    }

    @FXML
    public void onEnter() {
        btnSend.fire();
    }
}

