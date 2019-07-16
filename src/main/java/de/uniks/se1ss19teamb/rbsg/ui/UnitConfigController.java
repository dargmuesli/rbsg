package de.uniks.se1ss19teamb.rbsg.ui;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Arrays;

public class UnitConfigController {
    @FXML
    private VBox unitConfig;

    @FXML
    private Pane imageUnitType;

    @FXML
    private Label labelUnitSave1;

    @FXML
    private Label labelUnitSave2;

    @FXML
    private Label labelUnitSave3;
    private GameLobbyController gameLobbyController;

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{unitConfig}));

    }

    void loadConfig(Unit unit, GameLobbyController gameLobbyController) {
        this.gameLobbyController = gameLobbyController;
        imageUnitType.getChildren().add(TextureManager.getTextureInstance(unit.getType()));
    }

    void showNumber(int value) {
        labelUnitSave1.setText(Integer.toString(value));
    }


}

