package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class NewChatTabController {

    @FXML
    private TextArea textArea;

    public NewChatTabController() {

    }

    @FXML
    public void initialize() {
        textArea.appendText("Private Chat started!\n");
    }
}
