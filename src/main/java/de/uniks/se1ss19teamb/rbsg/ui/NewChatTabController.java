package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class NewChatTabController {

    @FXML
    private Tab newTab;
    @FXML
    private TextArea textArea;

    public NewChatTabController() {

    }

    @FXML
    public void initialize() {
        textArea.appendText("Private Chat started!\n");
        addContextMenu();

    }

    private void addContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem closeMenuItem = new MenuItem("close");
        closeMenuItem.setOnAction((e) -> newTab.getTabPane().getTabs().remove(newTab));
        contextMenu.getItems().add(closeMenuItem);
        contextMenu.setStyle("-fx-background-color:transparent;");
        contextMenu.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;"
            + "-fx-padding: 0px;");
        newTab.setContextMenu(contextMenu);

    }
}
