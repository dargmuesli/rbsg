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
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    Army army = loadArmyConfig();
    private ArrayList<Army> armies = new ArrayList<>();
    private ArmyManagerController armyManagerController = new ArmyManagerController();
    ArrayList<Integer> counts = new ArrayList<>();


    public void initialize() {
        UserInterfaceUtils.makeFadeInTransition(gameLobby);
        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);


        Theming.setTheme(Arrays.asList(new Pane[]{gameLobby, gameLobby1}));
        Theming.hamburgerMenuTransition(hamburgerMenu, btnBack);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnLogout);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnFullscreen);

        showArmyConfig();
        for (Unit unit : units) {
            configControllers.get(0).showNumber(unit);
            configControllers.get(1).showNumber(unit);
        }


    }


    private void showArmyConfig() {
        for (Unit unit : units) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/unitConfig.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                UnitConfigController configController = fxmlLoader.getController();
                configController.loadConfig(unit, this);
                configControllers.add(configController);
                armyList.getItems().add(parent);
                armyList.setOrientation(Orientation.HORIZONTAL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

/*
    ArrayList<Integer> numberOfUnits(Army army) {
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        int count5 = 0;
        int count6 = 0;

        for (int i = 0; i < army.getUnits().size(); i++) {

            if (bazookaTrooper.getId().equals(army.getUnits().get(i))) {
                count1++;
            }


            if (chopper.getId().equals(army.getUnits().get(i))) {
                count2++;
            }

            if (heavyTank.getId().equals(army.getUnits().get(i))) {
                count3++;

            }

            if (infantry.getId().equals(army.getUnits().get(i))) {
                count4++;

            }

            if (jeep.getId().equals(army.getUnits().get(i))) {
                count5++;

            }

            if (lightTank.getId().equals(army.getUnits().get(i))) {
                count6++;

            }


        }

        counts.add(count1);
        counts.add(count2);
        counts.add(count3);
        counts.add(count4);
        counts.add(count5);
        counts.add(count6);

        return counts;
    }

 */


    private Army loadArmyConfig() {
        return SerializeUtils.deserialize(new File(String.format(armysavePath, 1)), Army.class);
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

