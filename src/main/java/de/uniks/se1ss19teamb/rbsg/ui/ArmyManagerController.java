package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class ArmyManagerController {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXHamburger ham;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnFullScreen;
    @FXML
    private Label labelLeftUnits;
    @FXML
    private ScrollPane scrollPaneUnitOverview;

    private UnitOverviewController unitOverviewController;

    public void initialize() {

        hamTran(ham, btnBack);
        hamTran(ham, btnLogout);
        hamTran(ham, btnFullScreen);
        UserInterfaceUtils.makeFadeInTransition(mainPane);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
            .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/unitOverview.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            unitOverviewController = fxmlLoader.getController();
            unitOverviewController.setArmyManagerController(this);
            scrollPaneUnitOverview.setContent(parent);

        } catch (IOException e) {
            e.printStackTrace();
        }
        setLabelLeftUnits(10);
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

    public void hamTran(JFXHamburger ham, JFXButton button) {
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(ham);
        transition.setRate(-1);
        ham.addEventHandler(MouseEvent.MOUSE_PRESSED, (event -> {
            transition.setRate(transition.getRate() * -1);
            if (transition.getRate() == -1) {
                button.setVisible(false);
            } else if (transition.getRate() == 1) {
                button.setVisible(true);
            }
            transition.play();
        }));
    }

    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnLogout)) {
            LogoutUserRequest logout = new LogoutUserRequest(LoginController.getUserKey());
            logout.sendRequest();
            if (logout.getSuccessful()) {
                LoginController.setUserKey(null);
                UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", mainPane);
            }
        } else if (event.getSource().equals(btnFullScreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullScreen);
        }
    }

    void setLabelLeftUnits(int count) {
        labelLeftUnits.setText(Integer.toString(count));
    }
}

