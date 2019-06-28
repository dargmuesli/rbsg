package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import de.uniks.se1ss19teamb.rbsg.model.InGameMetadata;
import de.uniks.se1ss19teamb.rbsg.model.InGameTile;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.textures.TextureManager;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;


public class InGameController {

    LoginController loginController = new LoginController();
    ArmyManagerController armyManagerController = new ArmyManagerController();
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
    @FXML
    private GridPane gameGrid;

    // TODO: store those paths in a single location?!
    private String cssDark = "/de/uniks/se1ss19teamb/rbsg/css/dark-design2.css";
    private String cssWhite = "/de/uniks/se1ss19teamb/rbsg/css/white-design2.css";
    private String path = "./src/main/resources/de/uniks/se1ss19teamb/rbsg/cssMode.json";

    private final GameSocket gameSocket = new GameSocket(
        LoginController.getUserKey(), GameFieldController.joinedGame.getId(), ArmyManagerController.selectedArmyId);

    public static InGameMetadata inGameMetadata;
    public static Map<Pair<Integer, Integer>, InGameTile> inGameTiles = new HashMap<>();
    public static boolean gameInitFinished = false;

    public void initialize() {
        gameSocket.registerGameMessageHandler((message, from, isPrivate) -> {
            // TODO route incoming messages to ingame chat
        });

        gameSocket.connect();

        loginController.changeTheme(inGameScreen, inGameScreen1, path, cssDark, cssWhite);
        UserInterfaceUtils.makeFadeInTransition(inGameScreen);
        armyManagerController.hamTran(ham, btnBack);
        armyManagerController.hamTran(ham, btnLogout);
        armyManagerController.hamTran(ham, btnFullscreen);
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

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(inGameTiles.get(new Pair<>(0,maxX)) != null) {
            maxX++;
        }
        while(inGameTiles.get(new Pair<>(maxY, 0)) != null) {
            maxY++;
        }

        for (int i = 0; i < maxY; i++) {
            for (int j = 0; i < maxX; j++) {
                gameGrid.add(TextureManager.computeTerrainTextureInstance(inGameTiles, j, i), j, i);
            }
        }
    }

}
