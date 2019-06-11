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
                if (observable.getValue().substring(0,3).toLowerCase().contains("/w ")) {
                    for (int i = 4; i < observable.getValue().length(); i++) {
                        if (observable.getValue().toCharArray()[i] == ' ') {
                            setPrivate(observable.getValue(), i);
                            break;
                        }
                    }
                } else if (observable.getValue().substring(0,4).toLowerCase().contains("/all")) {
                    if (observable.getValue().substring(4,5).contains(" ")) {
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
                            .load(this.getClass().getResource("/de/uniks/se1ss19teamb/rbsg/newChatTab.fxml"));
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
        TextArea area = (TextArea) scrollPane.getContent();
        if (message != null) {
            area.appendText(from + ": " + message + "\n");
        }
    }

    private boolean checkInput(String input) {
        if (input.length() < 4) {
            return false;
        } else if (input.substring(0,3).toLowerCase().contains("/w ")) {
            setPrivate(input, 0);
            return true;
        } else if (input.substring(0,4).toLowerCase().contains("/all") && input.length() == 4) {
            setAll();
            return true;
        } else {
            return false;
        }
    }

    private void setPrivate(String input, int count) {
        if (count == 0) {
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
