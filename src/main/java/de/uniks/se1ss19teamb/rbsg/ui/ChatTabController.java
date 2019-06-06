package de.uniks.se1ss19teamb.rbsg.ui;

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

    public ChatTabController() {

    }

    @FXML
    void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnSend)) {
            System.out.println(message.getText());
            textArea.appendText("SomeName: " + message.getText() + "\n");
        }
    }


}
