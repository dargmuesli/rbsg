package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import de.uniks.se1ss19teamb.rbsg.request.*;


public class ArmyManagerController {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnWhiteMode;
    @FXML
    private JFXHamburger ham;
    @FXML
    private JFXButton btnBack;

    public void initialize() {

        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(ham);
        transition.setRate(-1);
        ham.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
            transition.setRate(transition.getRate()*-1);
            transition.play();
        });
        if(transition.getRate() == 1){
            btnLogout.isVisible();
            btnWhiteMode.isVisible();
            btnBack.isVisible();
        }else if(transition.getRate() == -1){
            btnLogout.setVisible(false);
            btnWhiteMode.setVisible(false);
            btnBack.setVisible(false);
        }

        UserInterfaceUtils.makeFadeInTransition(mainPane);
    }

    @FXML
    private void eventHandler(ActionEvent event) {
        switch (((JFXButton) event.getSource()).getId()) {
            case "btnBack":
                UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", mainPane);
                break;
            default:
        }
    }

    public void setOnAction(ActionEvent event){
        if (event.getSource().equals(btnLogout)) {
            LogoutUserRequest logout = new LogoutUserRequest(LoginController.getUserKey());
            logout.sendRequest();
            if (logout.getSuccessful()) {
                LoginController.setUserKey(null);
                UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", mainPane);
            }
        } else if (event.getSource().equals(btnWhiteMode)) {
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/armyManager.fxml", mainPane);
        }
    }
}

