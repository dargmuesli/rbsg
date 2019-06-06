package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

public class ChatTabController {
    @FXML
    private Button send;

    @FXML
    private TextField message;

    @FXML
    private ScrollPane textArea;

    public void handleSend() {
        send.onKeyPressedProperty();
    }

    public void handleMessage() {

    }

    public void handleTextArea() {

    }
}
