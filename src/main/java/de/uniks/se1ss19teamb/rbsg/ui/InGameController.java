package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import de.uniks.se1ss19teamb.rbsg.model.InGameMetadata;
import de.uniks.se1ss19teamb.rbsg.model.InGameTile;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

public class InGameController {

    @FXML
    private AnchorPane inGameScreen;
    @FXML
    private JFXHamburger hamburgerMenu;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private AnchorPane inGameScreen1;

    public static InGameMetadata inGameMetadata;
    public static Map<Pair<Integer, Integer>, InGameTile> inGameTiles = new HashMap<>();
    public static boolean gameInitFinished = false;

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{inGameScreen, inGameScreen1}));
        Theming.hamburgerMenuTransition(hamburgerMenu, btnBack);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnLogout);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnFullscreen);

        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);

        UserInterfaceUtils.makeFadeInTransition(inGameScreen);

        GameSocket.instance = new GameSocket(
            LoginController.getUserKey(),
            GameFieldController.joinedGame.getId(),
            ArmyManagerController.currentArmy.getId());

        GameSocket.instance.registerGameMessageHandler((message, from, isPrivate) -> {
        });

        GameSocket.instance.connect();

    }

    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnBack)) {
            switch (((JFXButton) event.getSource()).getId()) {
                // TODO handshake error
                case "btnBack":
                    GameSocket.instance.disconnect();
                    UserInterfaceUtils.makeFadeOutTransition(
                        "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", inGameScreen);
                    break;
                default:
            }
        } else if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        } else if (event.getSource().equals(btnLogout)) {
            LogoutUserRequest logout = new LogoutUserRequest(LoginController.getUserKey());
            logout.sendRequest();
            if (logout.getSuccessful()) {
                LoginController.setUserKey(null);
                UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", inGameScreen);
            }
        }
    }
}
