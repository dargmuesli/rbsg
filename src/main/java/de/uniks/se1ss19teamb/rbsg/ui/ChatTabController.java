package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.chat.Chat;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.SystemSocket;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class ChatTabController {

    @FXML
    private AnchorPane tabPane;

    @FXML
    private Button btnSend;

    @FXML
    private TextField message;

    @FXML
    private TextArea textArea;

    public static String userKey = LoginController.getUserKey();

    public static String userName = LoginController.getUser();

    public final ChatSocket chatSocket = new ChatSocket(userName, userKey);

    public final SystemSocket system = new SystemSocket(userKey);

    private Path chatLogPath = Paths.get("src/java/resources/de/uniks/se1ss19teamb/rbsg/chatLog.txt");

    private Chat chat = new Chat(this.chatSocket, chatLogPath);

    /* TODO
        after some time it automaticly disconnects system and chatSocket
     */

    public ChatTabController() {

    }

    @FXML
    public void initialize() {
        chatSocket.registerChatMessageHandler((message, from, isPrivate) -> {
            if (isPrivate) {
                // TODO privates tab
                textArea.appendText(from + " to me: " + message + "\n");
            } else {
                textArea.appendText(from + ": " + message + "\n");
            }
        });

        system.registerUserJoinHandler((name) -> {
            textArea.appendText("userJoin|" + name + "\n");
        });

        system.registerUserLeftHandler((name) -> {
            textArea.appendText("userLeft|" + name + "\n");
        });

        system.registerGameCreateHandler((name, id, neededPlayers) -> {
            textArea.appendText("gameCreate|" + name + '|' + id + '|' + neededPlayers + "\n");
        });

        system.registerGameDeleteHandler((id) -> {
            textArea.appendText("gameDelete|" + id + "\n");
        });

        system.connect();

        LoginController.setChatSocket(chatSocket);
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
