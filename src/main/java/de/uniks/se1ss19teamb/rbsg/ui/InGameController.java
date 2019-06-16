package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;



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
        private JFXButton btnFullScreen;

    ArmyManagerController armyManagerController = new ArmyManagerController();

    public void initialize() {
        UserInterfaceUtils.makeFadeInTransition(inGameScreen);
        armyManagerController.hamTran(ham, btnBack);
        armyManagerController.hamTran(ham, btnLogout);
        armyManagerController.hamTran(ham, btnFullScreen);

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
        } else if (event.getSource().equals(btnFullScreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullScreen);
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
