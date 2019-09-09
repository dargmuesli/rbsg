package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.ui.modules.ArmyManagerController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.RequestUtil;
import de.uniks.se1ss19teamb.rbsg.util.StringUtil;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArmyManagerContainerController {

    private static final Logger logger = LogManager.getLogger();

    @FXML
    private AnchorPane apnFade;
    @FXML
    private AnchorPane apnRoot;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnFullscreen;

    @FXML
    private void initialize() {
        UserInterfaceUtils.initialize(apnFade, apnRoot, this.getClass(), btnFullscreen);
    }

    @FXML
    private void goBack() {
        btnBack.setDisable(true);

        if ((ArmyManagerController.army != null
            && ArmyManagerController.army.getId() != null
            && !ArmyManagerController.army.getId().equals(""))
            || ArmyManagerController.getInstance().discardConfirmation) {

            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", apnFade);
        } else {
            NotificationHandler.sendWarning(StringUtil.DISCARD_CONFIRMATION, logger);
            ArmyManagerController.getInstance().discardConfirmation = true;
        }
    }

    @FXML
    private void logout() {
        if ((ArmyManagerController.army.getId() != null
            && !ArmyManagerController.army.getId().equals(""))
            || ArmyManagerController.getInstance().discardConfirmation) {

            if (!RequestUtil.request(new LogoutUserRequest(LoginController.getUserToken()))) {
                return;
            }

            btnLogout.setDisable(true);
            LoginController.setUserToken(null);
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", apnFade);
        } else {
            NotificationHandler.sendWarning(StringUtil.DISCARD_CONFIRMATION, logger);
            ArmyManagerController.getInstance().discardConfirmation = true;
        }
    }

    @FXML
    private void toggleFullscreen() {
        UserInterfaceUtils.toggleFullscreen(btnFullscreen);
    }
}
