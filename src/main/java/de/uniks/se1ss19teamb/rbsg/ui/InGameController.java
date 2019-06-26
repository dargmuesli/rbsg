package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import de.uniks.se1ss19teamb.rbsg.Main;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.SystemSocket;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;


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
    private String cssDark = "/de/uniks/se1ss19teamb/rbsg/css/dark-design2.css";
    private String cssWhite = "/de/uniks/se1ss19teamb/rbsg/css/white-design2.css";
    private String path = "./src/main/resources/de/uniks/se1ss19teamb/rbsg/cssMode.json";

    private MainController mainController;
    private final GameSocket gameSocket = new GameSocket(
        LoginController.getUserKey(), MainController.joinedGame.getId(), ArmyManagerController.selectedArmyId);

    public void initialize() {
        gameSocket.registerGameMessageHandler((message, from, isPrivate) -> {
            // TODO route incoming messages to ingame chat
        });

        loginController.changeTheme(inGameScreen, inGameScreen1, path, cssDark, cssWhite);
        UserInterfaceUtils.makeFadeInTransition(inGameScreen);
        armyManagerController.hamTran(ham, btnBack);
        armyManagerController.hamTran(ham, btnLogout);
        armyManagerController.hamTran(ham, btnFullscreen);

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
