package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXTabPane;
import de.uniks.se1ss19teamb.rbsg.chat.Chat;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.SystemSocket;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChatTabController {

    private static String userKey = LoginController.getUserKey();
    private static String userName = LoginController.getUser();
    private final ChatSocket chatSocket = new ChatSocket(userName, userKey);
    private final SystemSocket system = new SystemSocket(userKey);
    @FXML
    private Button btnSend;
    @FXML
    private TextField message;
    @FXML
    private VBox textArea;
    @FXML
    private JFXTabPane chatPane;
    private SingleSelectionModel<Tab> selectionModel;
    private Path chatLogPath = Paths.get("src/java/resources/de/uniks/se1ss19teamb/rbsg/chatLog.txt");

    private Chat chat = new Chat(this.chatSocket, chatLogPath);

    private String sendTo = null;

    /* TODO
        after some time it automaticly disconnects system and chatSocket
     */

    public ChatTabController() {

    }

    @FXML
    public void initialize() {
        message.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (observable.getValue().substring(0,3).toLowerCase().contains("/w ")) {
                    for (int i = 4; i < observable.getValue().length(); i++) {
                        if (observable.getValue().toCharArray()[i] == ' ') {
                            setPrivate(observable.getValue(), i);
                            break;
                        }
                    }
                } else if (observable.getValue().substring(0,4).toLowerCase().contains("/all")) {
                    if (observable.getValue().substring(4,5).contains(" ")) {
                        selectionModel.select(chatPane.getTabs().get(0));
                        setAll();
                    }
                }
            } catch (Exception e) {
                // do nothing
            }
        });

        chatSocket.registerChatMessageHandler((message, from, isPrivate) -> {
            if (isPrivate) {
                addNewPane(from, message, false);
            } else {
                addElemet(from, message, textArea, false);
                // textArea.appendText(from + ": " + message + "\n");
            }
        });

        system.registerUserJoinHandler((name) -> addElemet("system", "userJoin|" + name, textArea, false));

        system.registerUserLeftHandler((name) -> addElemet("system", "userLeft|" + name, textArea, false));

        system.registerGameCreateHandler((name, id, neededPlayers)
            -> addElemet("system", "gameCreate|" + name + '|' + id + '|' + neededPlayers, textArea, false));

        system.registerGameDeleteHandler((id) -> addElemet("system", "gameDelete|" + id, textArea, false));

        system.connect();

        LoginController.setChatSocket(chatSocket);

        chatPane.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Tab>() {
                @Override
                public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                    if (t1.getText().equals("All")) {
                        setAll();
                    } else {
                        setPrivate(t1.getText(), -1);
                    }
                }
            }
        );

        selectionModel = chatPane.getSelectionModel();
    }

    private void addElemet(String player, String message, VBox box, boolean whisper) {
        Label name = new Label(player + ":");
        name.setPadding(new Insets(5));
        name.setWrapText(true);
        if (whisper) {
            name.setStyle("-fx-text-fill: -fx-privatetext;");
        } else {
            name.setStyle("-fx-text-fill: black;");
        }
        // whisper on double click
        name.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        setPrivate(player, -1);
                    }
                }
            }
        });

        Label text = new Label(message);
        text.setPadding(new Insets(5));
        text.setWrapText(true);
        text.setStyle("-fx-text-fill: black;");

        HBox container = new HBox(name, text);
        Platform.runLater(() -> {
            box.getChildren().add(container);
        });
    }

    private void addNewPane(String from, String message, boolean mymessage) {
        boolean createTab = true;
        for (Tab t : chatPane.getTabs()) {
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
                            .load(this.getClass().getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/PrivateTab.fxml"));
                        newTab.setText(from);
                        chatPane.getTabs().add(newTab);
                        if (mymessage) {
                            getPrivate(userName, message, newTab);
                        } else {
                            getPrivate(from, message, newTab);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        // TODO Logger.
                    }
                }
            );
        }
    }

    private void getPrivate(String from, String message, Tab tab) {
        ScrollPane scrollPane = (ScrollPane) tab.getContent();
        VBox area = (VBox) scrollPane.getContent();
        if (message != null) {
            addElemet(from, message, area, true);
        }
        selectionModel.select(tab);
    }

    private boolean checkInput(String input) {
        if (input.length() < 4) {
            return false;
        } else if (input.substring(0,3).toLowerCase().contains("/w ")) {
            setPrivate(input, 0);
            return true;
        } else if (input.substring(0,4).toLowerCase().contains("/all") && input.length() == 4) {
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
            sendTo = input.substring(3,count);
        }
        Platform.runLater(() -> {
            addNewPane(sendTo, null, true);
            message.clear();
            message.setStyle("-fx-text-fill: -fx-privatetext;"
                + "-jfx-focus-color: -fx-privatetext;");
        });
    }

    private void setAll() {
        sendTo = null;
        Platform.runLater(() -> {
            message.clear();
            message.setStyle("-fx-text-fill: -fx-secondary;"
                + "-jfx-focus-color: -fx-secondary;");
        });
    }

    @FXML
    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnSend)) {
            if (!message.getText().isEmpty()) {
                if (checkInput(message.getText())) {
                    return;
                }
                if (sendTo != null) {
                    if (sendTo.trim() == "") {
                        sendTo = null;
                        chat.sendMessage(message.getText());
                    } else {
                        chat.sendMessage(message.getText(), sendTo);
                        addNewPane(sendTo, message.getText(), true);
                    }
                } else {
                    chat.sendMessage(message.getText());
                }
                message.setText("");
            }
        }
    }

    @FXML
    public void onEnter() {
        btnSend.fire();
    }

}
