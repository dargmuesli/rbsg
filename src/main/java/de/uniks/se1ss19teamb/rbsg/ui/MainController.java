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
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.SystemSocket;
import de.uniks.se1ss19teamb.rbsg.util.*;

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
    public static MainController instance;
    private static Chat chat;
    private static HashMap<String, GameMeta> existingGames;
    private static Path chatLogPath = Paths.get("src/java/resources/de/uniks/se1ss19teamb/rbsg/chatLog.txt");
    private static SingleSelectionModel<Tab> selectionModel;
    private static String sendTo = null;

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
    private JFXButton btnColorMode;
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
    private double chatWindowWidth;
    private double chatWindowHeight;
    @FXML
    private VBox textArea;

    static void setGameChat(GameSocket gameSocket) {
        MainController.chat = new Chat(gameSocket, chatLogPath);
        gameSocket.connect();
    }

    private static HashMap<String, GameMeta> getExistingGames() {
        return RequestUtil.request(new QueryGamesRequest(LoginController.getUserToken())).orElse(null);
    }

    public void initialize() {
        UserInterfaceUtils.initialize(mainScreen, mainScreen1, MainController.class, btnFullscreen, errorContainer);

        MainController.instance = this;

        // TODO - after some time it automaticly disconnects system and chatSocket
        if (SystemSocket.instance == null) {
            SystemSocket.instance = new SystemSocket();
        }

        SystemSocket.instance.registerUserJoinHandler(
            (name) -> {
                updatePlayerView();
                addElement(name, " joined.", textArea, false);
            });

        SystemSocket.instance.registerUserLeftHandler(
            (name) -> {
                addElement(name, " left.", textArea, false);
                if (!name.equals(LoginController.getUserName())) {
                    updatePlayerView();
                }
            });

        SystemSocket.instance.registerGameCreateHandler(
            (gameName, id, neededPlayers) -> {
                updateGameView();
                addElement(null, "Game \"" + gameName + "\" was created for " + neededPlayers + " players.",
                    textArea, false);
            });

        SystemSocket.instance.registerGameDeleteHandler(
            (id) -> {
                String deletedGameName = existingGames.get(id).getName();
                Platform.runLater(() -> {
                    addElement(null, "Game \"" + deletedGameName + "\" was deleted.",
                        textArea, false);
                    updateGameView();
                });
            });

        SystemSocket.instance.registerPlayerJoinedGameHandler(
            (id, joinedPlayer) -> {
                MainController.getExistingGames().get(id).setJoinedPlayers((long) joinedPlayer);
                Platform.runLater(this::updateGameView);
            }
        );

        if (SystemSocket.instance.websocket == null || SystemSocket.instance.websocket.mySession == null) {
            SystemSocket.instance.connect();
        }

        if (ChatSocket.instance == null) {
            ChatSocket.instance = new ChatSocket(LoginController.getUserName(), LoginController.getUserToken());
        }

        ChatSocket.instance.registerMessageHandler((message, from, isPrivate) -> {
            if (isPrivate) {
                addNewPane(from, message, false, chatPane);
            } else {
                addElement(from, message, textArea, false);
            }
        });

        MainController.chat = new Chat(ChatSocket.instance, chatLogPath);

        if (ChatSocket.instance.websocket == null || ChatSocket.instance.websocket.mySession == null) {
            ChatSocket.instance.connect();
        }

        updateGameView();
        updatePlayerView();

        Platform.runLater(() -> {
            Theming.hamburgerMenuTransition(hamburgerMenu, btnFullscreen);
            Theming.hamburgerMenuTransition(hamburgerMenu, btnLogout);
            Theming.hamburgerMenuTransition(hamburgerMenu, btnColorMode);

            // ChatTabController
            chatPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    if (t1.getText().equals("All")) {
                        setAll();
                    } else {
                        setPrivate(t1.getText(), -1);
                    }
                    t1.setGraphic(null);
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

        btnMinimize.setDisable(true);
    }

    @FXML
    private void changeTheme() {
        SerializeUtils.serialize(Theming.cssModeFile.getAbsolutePath(), !Theming.darkModeActive());
        Theming.setTheme(Arrays.asList(new Pane[]{mainScreen, mainScreen1}));

        // the game view contains sub-fxmls and thus needs to be updated separately
        updateGameView();
    }

    @FXML
    private void createGame() {
        if (!gameName.getText().isEmpty()) {
            Toggle selected = playerNumberToggleGroup.getSelectedToggle();
            String userKey = LoginController.getUserToken();

            if (selected.equals(twoPlayers)) {
                new CreateGameRequest(gameName.getText(), 2, userKey).sendRequest();
            } else if (selected.equals(fourPlayers)) {
                new CreateGameRequest(gameName.getText(), 4, userKey).sendRequest();
            }
        } else {
            NotificationHandler.getInstance().sendWarning("Bitte geben Sie einen Namen für das Spiel ein.", logger);
        }
    }

    @FXML
    private void goToArmyManager() {
        btnArmyManager.setDisable(true);
        btnMinimize.setDisable(false);
        btnMinimize.fire();
        UserInterfaceUtils.makeFadeOutTransition(
            "/de/uniks/se1ss19teamb/rbsg/fxmls/armyManagerContainer.fxml", mainScreen, chatWindow);
    }

    @FXML
    private void logout() {
        UserInterfaceUtils.logout(mainScreen, btnLogout);
    }

    @FXML
    private void minimizeChat() {
        if (chatBox.isVisible()) {
            chatBox.setVisible(false);
            chatWindowWidth = chatWindow.getWidth();
            chatWindowHeight = chatWindow.getHeight();
            chatWindow.setPrefWidth(0);
            chatWindow.setPrefHeight(0);
            Platform.runLater(() ->
                btnMinimize.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.WINDOW_MAXIMIZE)));
        } else {
            chatBox.setVisible(true);
            chatWindow.setPrefWidth(chatWindowWidth);
            chatWindow.setPrefHeight(chatWindowHeight);
            Platform.runLater(() ->
                btnMinimize.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.WINDOW_MINIMIZE)));
        }
    }

    @FXML
    private void sendChatMessage() {
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

        message.requestFocus();
    }

    @FXML
    private void toggleFullscreen() {
        UserInterfaceUtils.toggleFullscreen(btnFullscreen);
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
                    .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/gameSelection.fxml"));

                try {
                    Parent parent = fxmlLoader.load();
                    GameSelectionController controller = fxmlLoader.getController();
                    controller.setUpGameLabel(gameMeta);
                    gameListView.getItems().add(parent);
                } catch (IOException e) {
                    NotificationHandler.getInstance()
                        .sendError("Ein GameField konnte nicht geladen werden!", logger, e);
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
        return RequestUtil.request(new QueryUsersInLobbyRequest(LoginController.getUserToken())).orElse(null);
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
    void addElement(String player, String message, VBox box, boolean whisper) {

        VBox container = new VBox();

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
        if (!chatPane.getTabs().get(0).isSelected() && !whisper) {
            Platform.runLater(() ->
                chatPane.getTabs().get(0).setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_CIRCLE)));
        }

        if (!chatBox.isVisible()) {
            Platform.runLater(() ->
                btnMinimize.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_CIRCLE)));
        }
    }

    private void setChatStyle(Label label) {
        label.setStyle("-fx-text-fill: " + (Theming.darkModeActive()
            ? "-fx-primary" : "black") + ";"
            + "-fx-background-color: " + (Theming.darkModeActive()
            ? "-fx-secondary" : "white") + ";"
            + "-fx-border-radius: 20px;"
            + "-fx-background-radius: 10px;");
    }

    void addNewPane(String from, String message, boolean mymessage, JFXTabPane pane) {
        boolean createTab = true;
        for (Tab t : pane.getTabs()) {
            if (t.getText().equals(from)) {
                if (!t.isSelected()) {
                    Platform.runLater(() -> t.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_CIRCLE)));
                }
                if (mymessage) {
                    getPrivate(LoginController.getUserName(), message, t);
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
                            getPrivate(LoginController.getUserName(), message, newTab);
                        } else {
                            getPrivate(from, message, newTab);
                        }
                        MainController.selectionModel.select(newTab);
                    } catch (IOException e) {
                        NotificationHandler.getInstance()
                            .sendError("Ein GameField konnte nicht geladen werden!", logger, e);
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
        MainController.selectionModel.select(tab);
    }

    private boolean checkInput(String input) {
        if (input.length() < 2) {
            return false;
        } else if (input.length() < 4) {
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
            MainController.sendTo = input;
        } else if (count == 0) {
            MainController.sendTo = input.substring(3);
        } else {
            MainController.sendTo = input.substring(3, count);
        }
        Platform.runLater(() -> {
            addNewPane(MainController.sendTo, null, true, chatPane);
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

