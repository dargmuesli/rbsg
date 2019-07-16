package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.request.QueryArmiesRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class GameLobbyController {

    private static final Logger logger = LogManager.getLogger();

    @FXML
    private AnchorPane gameLobby1;

    @FXML
    private AnchorPane gameLobby;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnLogout;

    @FXML
    private JFXButton btnFullscreen;

    @FXML
    private JFXHamburger hamburgerMenu;

    @FXML
    private Label gameName;

    @FXML
    private ListView<?> playerList;

    @FXML
    private JFXButton btnReady;

    @FXML
    private JFXButton select1;

    @FXML
    private JFXButton select2;

    @FXML
    private JFXButton select3;

    @FXML
    private ListView<Parent> armyList;

    @FXML
    private JFXButton btnMyReady;

    private static final Path ARMY_SAVE_PATH =
        Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "rbsg_army-save-%d.json");
    private ArrayList<UnitConfigController> configControllers = new ArrayList<>();


    public void initialize() {
        UserInterfaceUtils.makeFadeInTransition(gameLobby);
        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);

        Theming.setTheme(Arrays.asList(new Pane[]{gameLobby, gameLobby1}));
        Theming.hamburgerMenuTransition(hamburgerMenu, btnBack);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnLogout);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnFullscreen);


    }


    private void showArmyConfig() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
            .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/unitConfig.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            UnitConfigController configController = fxmlLoader.getController();
            configControllers.add(configController);
            armyList.getItems().add(parent);
            armyList.setOrientation(Orientation.HORIZONTAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private Army loadArmyConfig() {
        return SerializeUtils.deserialize(new File(String.format(ARMY_SAVE_PATH.toString())), Army.class);
    }


    @FXML
    private void eventHandler(ActionEvent event) {
        if (event.getSource().equals(btnBack)) {
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", gameLobby);
        }
    }

    @FXML
    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnLogout)) {
            LogoutUserRequest logout = new LogoutUserRequest(LoginController.getUserKey());
            logout.sendRequest();

            if (logout.getSuccessful()) {
                LoginController.setUserKey(null);
                UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", gameLobby);
            }
        }else if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        }else if (event.getSource().equals(btnMyReady)) {
            QueryArmiesRequest req = new QueryArmiesRequest(LoginController.getUserKey());
            req.sendRequest();
            VBox chatWindow = (VBox) gameLobby.getScene().lookup("#chatWindow");
            JFXButton btnMinimize = (JFXButton) chatWindow.lookup("#btnMinimize");
            btnMinimize.setDisable(false);

            UserInterfaceUtils.makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/fxmls/inGame.fxml",gameLobby);
            gameLobby.getScene().lookup("#chatWindow");
    }
}


}

