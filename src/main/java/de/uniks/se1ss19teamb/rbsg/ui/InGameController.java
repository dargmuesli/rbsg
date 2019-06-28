package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import de.uniks.se1ss19teamb.rbsg.model.InGameMetadata;
import de.uniks.se1ss19teamb.rbsg.model.InGameTile;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;


public class InGameController {

    @FXML
    private AnchorPane inGameScreen;
    @FXML
    private JFXHamburger ham;
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
        Theming.setTheme(inGameScreen, inGameScreen1);
        Theming.hamburgerMenuTransition(ham, btnBack);
        Theming.hamburgerMenuTransition(ham, btnLogout);
        Theming.hamburgerMenuTransition(ham, btnFullscreen);

        GameSocket.instance = new GameSocket(
            LoginController.getUserKey(),
            GameFieldController.joinedGame.getId(),
            ArmyManagerController.currentArmy.getId());

        GameSocket.instance.registerGameMessageHandler((message, from, isPrivate) -> {
            // TODO route incoming messages to ingame chat
        });

        GameSocket.instance.connect();

        UserInterfaceUtils.makeFadeInTransition(inGameScreen);
    }

    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnBack)) {
            switch (((JFXButton) event.getSource()).getId()) {
                case "btnBack":
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
