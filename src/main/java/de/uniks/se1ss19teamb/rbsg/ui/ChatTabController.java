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
    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnSend)) {
            textArea.appendText("SomeName: " + message.getText() + "\n");
            message.setText("");
        }
    }

    @FXML
    public void onEnter(ActionEvent event) {
        btnSend.fire();
    }


}
