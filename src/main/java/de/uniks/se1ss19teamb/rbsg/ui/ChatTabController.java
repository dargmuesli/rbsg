package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXTabPane;
import de.uniks.se1ss19teamb.rbsg.chat.Chat;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.SystemSocket;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

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
    private TextArea textArea;
    @FXML
    private JFXTabPane chatPane;
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
                if (observable.getValue().substring(0,3).contains("/w ")
                    || observable.getValue().substring(0,3).contains("/W ")) {
                    for (int i = 4; i < observable.getValue().length(); i++) {
                        if (observable.getValue().toCharArray()[i] == ' ') {
                            sendTo = observable.getValue().substring(3,i);
                            Platform.runLater(() -> {
                                message.clear();
                                message.setStyle("-fx-text-fill: -fx-privatetext;"
                                    + "-jfx-focus-color: -fx-privatetext;");
                            });
                            break;
                        }
                    }
                } else if (observable.getValue().substring(0,4).contains("/all")
                    || observable.getValue().contains("/All")
                    || observable.getValue().contains("/ALL")) {
                    if (observable.getValue().substring(4,5).contains(" ")) {
                        sendTo = null;
                        Platform.runLater(() -> {
                            message.clear();
                            message.setStyle("-fx-text-fill: -fx-secondary;"
                                + "-jfx-focus-color: -fx-secondary;");
                        });
                    }
                } else {
                    // do nothing
                }
            } catch (Exception e) {
                // do nothing
            }
        });

        chatSocket.registerChatMessageHandler((message, from, isPrivate) -> {
            if (isPrivate) {
                addNewPane(from, message);
            } else {
                textArea.appendText(from + ": " + message + "\n");
            }
        });

        system.registerUserJoinHandler((name) -> textArea.appendText("userJoin|" + name + "\n"));

        system.registerUserLeftHandler((name) -> textArea.appendText("userLeft|" + name + "\n"));

        system.registerGameCreateHandler((name, id, neededPlayers)
            -> textArea.appendText("gameCreate|" + name + '|' + id + '|' + neededPlayers + "\n"));

        system.registerGameDeleteHandler((id) -> textArea.appendText("gameDelete|" + id + "\n"));

        system.connect();

        LoginController.setChatSocket(chatSocket);
    }

    private void addNewPane(String from, String message) {
        boolean createTab = true;
        for (Tab t : chatPane.getTabs()) {
            if (t.getText().equals(from)) {
                getPrivate(from, message, t);
                createTab = false;
            }
        }
        if (createTab) {
            Platform.runLater(
                () -> {
                    try {
                        Tab newTab = FXMLLoader
                            .load(this.getClass().getResource("/de/uniks/se1ss19teamb/rbsg/newChatTab.fxml"));
                        newTab.setText(from);
                        chatPane.getTabs().add(newTab);
                        getPrivate(from, message, newTab);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // TODO Logger
                    }
                }
            );
        }
    }

    private void getPrivate(String from, String message, Tab tab) {
        System.out.println();
        ScrollPane scrollPane = (ScrollPane) tab.getContent();
        TextArea area = (TextArea) scrollPane.getContent();
        area.appendText(from + ": " + message + "\n");
    }

    @FXML
    public void setOnAction(ActionEvent event) throws IOException {
        if (event.getSource().equals(btnSend)) {
            if (!message.getText().isEmpty()) {
                if (sendTo != null) {
                    if (sendTo.trim() == "") {
                        sendTo = null;
                        chat.sendMessage(message.getText());
                    } else {
                        chat.sendMessage(message.getText(), sendTo);
                        addNewPane(sendTo, message.getText());
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
