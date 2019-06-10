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

    /* TODO
        after some time it automaticly disconnects system and chatSocket
     */

    public ChatTabController() {

    }

    @FXML
    public void initialize() {
        message.textProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue.contains("/w ")){
                System.out.println("oldValue contains");
            }
            if(newValue.contains("/w ")){
                System.out.println("newVlaues contains");
            }
        });
        chatSocket.registerChatMessageHandler((message, from, isPrivate) -> {
            if (isPrivate) {
                boolean newTab = true;
                for (Tab t : chatPane.getTabs()) {
                    if (t.getText().equals(from)) {
                        getPrivate(from, message, t);
                        newTab = false;
                    }
                }
                if (newTab) {
                    Platform.runLater(
                        () -> {
                            try {
                                addNewPane(from, message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    );
                }
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

    private void addNewPane(String from, String message) throws IOException {
        Tab newTab = FXMLLoader.load(this.getClass().getResource("/de/uniks/se1ss19teamb/rbsg/newChatTab.fxml"));
        newTab.setText(from);
        chatPane.getTabs().add(newTab);
        getPrivate(from, message, newTab);
    }

    private void getPrivate(String from, String message, Tab tab) {
        System.out.println();
        ScrollPane scrollPane = (ScrollPane) tab.getContent();
        TextArea area = (TextArea) scrollPane.getContent();
        area.appendText(from + " to me: " + message + "\n");
    }

    public static void sendPrivate(String message, String from) {
        // chatClass.sendMessage(message, from);
    }

    @FXML
    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnSend)) {
            if (!message.getText().isEmpty()) {
                chat.sendMessage(message.getText());
                // if private
                // chat.sendMessage(message.getText(), "sendTo");
                message.setText("");
            }
        }
    }

    @FXML
    public void onEnter() {
        btnSend.fire();
    }

}
