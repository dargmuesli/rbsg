package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import de.uniks.se1ss19teamb.rbsg.model.InGameMetadata;
import de.uniks.se1ss19teamb.rbsg.model.InGameTile;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
    @FXML
    private GridPane gameGrid;

    public static InGameMetadata inGameMetadata;
    public static Map<Pair<Integer, Integer>, InGameTile> inGameTiles = new HashMap<>();
    public static boolean gameInitFinished = false;

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{inGameScreen, inGameScreen1}));
        Theming.hamburgerMenuTransition(hamburgerMenu, btnBack);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnLogout);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnFullscreen);

        GameSocket.instance = new GameSocket(
            LoginController.getUserKey(),
            GameFieldController.joinedGame.getId(),
            ArmyManagerController.currentArmy.getId());

        GameSocket.instance.registerGameMessageHandler((message, from, isPrivate) -> {
            // TODO route incoming messages to ingame chat
        });

        GameSocket.instance.connect();

        UserInterfaceUtils.makeFadeInTransition(inGameScreen);
        fillGameGrid();
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

    private void fillGameGrid() {
        int maxX = 0;
        int maxY = 0;
        int tryCounter = 0;

        while (maxX * maxY != 1024) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (inGameTiles.get(new Pair<>(0, maxX)) != null) {
                maxX++;
            }
            while (inGameTiles.get(new Pair<>(maxY, 0)) != null) {
                maxY++;
            }
            tryCounter++;
            if (tryCounter == 5) {
                NotificationHandler.getNotificationHandler().sendError("The tiles couldn't be load.",
                    LogManager.getLogger());
                break;
            }
        }

        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                InGameTile tile = inGameTiles.get(new Pair<>(j, i));
                if (tile != null) {
                    System.out.println(tile.getName());
                    if (tile.getName().equals("Unit")) {
                        continue;
                    }
                }

                gameGrid.add(TextureManager.computeTerrainTextureInstance(inGameTiles, j, i), j, i);
            }
        }
    }

}
