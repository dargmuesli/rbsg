package de.uniks.se1ss19teamb.rbsg.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class PrivateTabController {

    @FXML
    public ScrollPane scroll;
    @FXML
    private Tab tbRoot;
    @FXML
    private VBox textArea;

    public PrivateTabController() {

    }

    @FXML
    public void initialize() {
        Label start = new Label("Private Chat started!");
        start.setStyle("-fx-text-fill: -fx-privatetext;");
        textArea.getChildren().add(start);
        addContextMenu();
        textArea.heightProperty().addListener(observable -> scroll.setVvalue(1D));
    }

    private void addContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem closeMenuItem = new MenuItem("close");
        closeMenuItem.setOnAction((e) -> tbRoot.getTabPane().getTabs().remove(tbRoot));
        contextMenu.getItems().add(closeMenuItem);
        contextMenu.setStyle("-fx-background-color:transparent;");
        contextMenu.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;"
            + "-fx-padding: 0px;");
        tbRoot.setContextMenu(contextMenu);

    }
}
