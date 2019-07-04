package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.uniks.se1ss19teamb.rbsg.model.InGameMetadata;
import de.uniks.se1ss19teamb.rbsg.model.InGameTile;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class InGameController {

    @FXML
    private AnchorPane inGameScreen;
    @FXML
    private JFXHamburger hamburgerMenu;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private AnchorPane inGameScreen1;

    public static InGameMetadata inGameMetadata;
    public static Map<Pair<Integer, Integer>, InGameTile> inGameTiles = new HashMap<>();
    public static boolean gameInitFinished = false;

    private VBox chatWindow;
    private JFXButton btnMinimize;
    private VBox chatBox;
    private JFXTabPane chatPane;
    private ScrollPane allPane;
    private VBox textArea;
    private JFXTextField message;
    private JFXButton btnSend;

    private String sendTo = null;
    private String userName = LoginController.getUser();
    private static final Logger logger = LogManager.getLogger();
    private SingleSelectionModel<Tab> selectionModel;

    private double chatWindowWidth;
    private double chatWindowHeight;

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{inGameScreen, inGameScreen1}));
        Theming.hamburgerMenuTransition(hamburgerMenu, btnBack);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnLogout);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnFullscreen);

        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);

        UserInterfaceUtils.makeFadeInTransition(inGameScreen);

        Platform.runLater(() -> {
            this.chatWindow = (VBox) btnLogout.getScene().lookup("#chatWindow");
            this.btnMinimize = (JFXButton) this.chatWindow.lookup("#btnMinimize");
            this.chatBox = (VBox) this.chatWindow.lookup("#chatBox");
            this.chatPane = (JFXTabPane) this.chatBox.lookup("#chatPane");
            this.allPane = (ScrollPane) this.chatPane.getTabs().get(0).getContent().lookup("#allPane");
            this.textArea = (VBox) this.allPane.lookup("#textArea");
            this.message = (JFXTextField) this.chatBox.lookup("#message");
            this.btnSend = (JFXButton) this.chatBox.lookup("#btnSend");

            GameSocket.instance = new GameSocket(
                LoginController.getUserKey(),
                GameFieldController.joinedGame.getId(),
                ArmyManagerController.currentArmy.getId());

            GameSocket.instance.registerGameMessageHandler((message, from, isPrivate) -> {
                if (isPrivate) {
                    addNewPane(from, message, false, chatPane);
                } else {
                    addElement(from, message, textArea, false);
                }
            });

            GameSocket.instance.connect();
        });
    }

    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(this.btnBack)) {
            switch (((JFXButton) event.getSource()).getId()) {
                // TODO handshake error
                case "btnBack":
                    GameSocket.instance.disconnect();
                    UserInterfaceUtils.makeFadeOutTransition(
                        "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", inGameScreen);
                    break;
                default:
            }
        } else if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        } else if (event.getSource().equals(btnLogout)) {
            LogoutUserRequest logout = new LogoutUserRequest(LoginController.getUserKey());
            logout.sendRequest();
            if (logout.getSuccessful()) {
                LoginController.setUserKey(null);
                UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", inGameScreen);
            }
        } else if (event.getSource().equals(this.btnSend)) {
            if (!this.message.getText().isEmpty()) {
                if (checkInput(this.message.getText())) {
                    return;
                }

                if (this.sendTo != null) {
                    if (this.sendTo.trim().equals("")) {
                        this.sendTo = null;
                        GameSocket.instance.sendMessage(this.message.getText());
                    } else {
                        GameSocket.instance.sendPrivateMessage(this.message.getText(), this.sendTo);
                        addNewPane(this.sendTo, this.message.getText(), true, this.chatPane);
                    }
                } else {
                    GameSocket.instance.sendMessage(this.message.getText());
                }

                this.message.setText("");
            }
        } else if (event.getSource().equals(this.btnMinimize)) {
            if (this.chatBox.isVisible()) {
                this.chatBox.setVisible(false);
                this.chatWindowWidth = this.chatWindow.getWidth();
                this.chatWindowHeight = this.chatWindow.getHeight();
                this.chatWindow.setPrefWidth(0);
                this.chatWindow.setPrefHeight(0);
                this.btnMinimize.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.WINDOW_MAXIMIZE));
            } else {
                this.chatBox.setVisible(true);
                this.chatWindow.setPrefWidth(this.chatWindowWidth);
                this.chatWindow.setPrefHeight(this.chatWindowHeight);
                this.btnMinimize.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.WINDOW_MINIMIZE));
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

        this.message.requestFocus();

    }

    private void addElement(String player, String message, VBox box, boolean whisper) {

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
                    getPrivate(this.userName, message, t);
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
        this.selectionModel.select(tab);
    }

    private boolean checkInput(String input) {
        if (input.length() < 2) {
            return false;
        } else if (input.length() < 4) {
            if (input.substring(0, 2).equals("//")) {
                Window window = this.btnFullscreen.getScene().getWindow();
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
            this.selectionModel.select(this.chatPane.getTabs().get(0));
            setAll();
            return true;
        } else {
            return false;
        }
    }

    private void setPrivate(String input, int count) {
        if (count == -1) {
            this.sendTo = input;
        } else if (count == 0) {
            this.sendTo = input.substring(3);
        } else {
            this.sendTo = input.substring(3, count);
        }
        Platform.runLater(() -> {
            addNewPane(this.sendTo, null, true, this.chatPane);
            this.message.clear();
            this.message.setStyle("-fx-text-fill: -fx-privatetext;"
                + "-jfx-focus-color: -fx-privatetext;");
        });
    }

    private void setAll() {
        this.sendTo = null;

        Platform.runLater(() -> {
            this.message.clear();
            this.message.setStyle("-fx-text-fill: " + (Theming.darkModeActive()
                ? "-fx-secondary;" : "black;") + "-jfx-focus-color: "
                + (Theming.darkModeActive()
                ? "-fx-secondary;" : "black;"));
        });
    }

    @FXML
    public void onEnter() {
        this.btnSend.fire();
    }
}
