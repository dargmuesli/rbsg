package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.units.*;
import de.uniks.se1ss19teamb.rbsg.request.LogoutUserRequest;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    private BazookaTrooper bazookaTrooper = new BazookaTrooper();
    private Chopper chopper = new Chopper();
    private HeavyTank heavyTank = new HeavyTank();
    private Infantry infantry = new Infantry();
    private Jeep jeep = new Jeep();
    private LightTank lightTank = new LightTank();
    private ArrayList<Unit> units = new ArrayList<>(Arrays.asList(bazookaTrooper, chopper,
        heavyTank, infantry, jeep, lightTank));
    private ArrayList<UnitConfigController> configControllers = new ArrayList<>();
    private String armysavePath = "./src/main/resources/de/uniks/se1ss19teamb/rbsg/armySaves/armySave%d.json";

    public void initialize() {
        UserInterfaceUtils.makeFadeInTransition(gameLobby);
        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);


        Theming.setTheme(Arrays.asList(new Pane[]{gameLobby, gameLobby1}));
        Theming.hamburgerMenuTransition(hamburgerMenu, btnBack);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnLogout);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnFullscreen);
        showArmyConfig();


    }


    private void showArmyConfig(){
        for (Unit unit: units){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/unitConfig.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                UnitConfigController configController = fxmlLoader.getController();
                configController.showUnitConfig(loadArmyConfig());
                configController.showUnitImage(unit,this);
                configControllers.add(configController);
                armyList.getItems().add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Army loadArmyConfig() {
        Army army = null;
        for (int i = 1; i <= 3; i++) {
            army = SerializeUtils.deserialize(new File(String.format(armysavePath, i)), Army.class);
        }
        return army;
    }


    @FXML
    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(btnBack)) {
            switch (((JFXButton) event.getSource()).getId()) {
                case "btnBack":
                    GameSocket.instance.disconnect();
                    UserInterfaceUtils.makeFadeOutTransition(
                        "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", gameLobby);
                    break;
                default:
            }
        } else if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        } else if (event.getSource().equals(btnLogout)) {
            LogoutUserRequest logout = new LogoutUserRequest(LoginController.getUser());
            logout.sendRequest();
            if (logout.getSuccessful()) {
                LoginController.setUserKey(null);
                UserInterfaceUtils.makeFadeOutTransition(
                    "/de/uniks/se1ss19teamb/rbsg/fxmls/login.fxml", gameLobby);
            }
        }
    }

}

