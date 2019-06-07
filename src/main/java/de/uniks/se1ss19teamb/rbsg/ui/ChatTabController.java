package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.chat.Chat;
import de.uniks.se1ss19teamb.rbsg.sockets.ChatSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.SystemSocket;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ChatTabController {
    @FXML
    private Button btnSend;

    @FXML
    private TextField message;

    @FXML
    private TextArea textArea;

    public static String userKey;

    public static String userName;

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
            if(isPrivate) {
                // TODO private
                System.out.println(message);
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
    }

    @FXML
    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnSend)) {
            //textArea.appendText("SomeName: " + message.getText() + "\n");
            chat.sendMessage(message.getText());
            message.setText("");
        }
    }

    @FXML
    public void onEnter(ActionEvent event) {
        btnSend.fire();
    }

}
