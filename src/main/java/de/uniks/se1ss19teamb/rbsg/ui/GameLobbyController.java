package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import generated.fulib.testmodel.Game;
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
    private Label army1;

    @FXML
    private Label army2;

    @FXML
    private Label army3;

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

    @FXML
    private JFXButton btnStart;

    private static final Path ARMY_SAVE_PATH =
        Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "rbsg_army-save-%d.json");
    private ArrayList<UnitConfigController> configControllers = new ArrayList<>();
    private Army currentArmy;
    private Army armyBuffer1 = new Army(null, null, new ArrayList<>());
    private Army armyBuffer2 = new Army(null, null, new ArrayList<>());
    private Army armyBuffer3 = new Army(null, null, new ArrayList<>());


    public void initialize() {
        UserInterfaceUtils.makeFadeInTransition(gameLobby);
        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);

        Theming.setTheme(Arrays.asList(new Pane[]{gameLobby, gameLobby1}));
        Theming.hamburgerMenuTransition(hamburgerMenu, btnBack);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnLogout);
        Theming.hamburgerMenuTransition(hamburgerMenu, btnFullscreen);

        QueryUnitsRequest queryUnitsRequest = new QueryUnitsRequest(LoginController.getUserKey());
        queryUnitsRequest.sendRequest();

        for (Unit unit : queryUnitsRequest.getUnits()) {
            ArmyManagerController.availableUnits.put(unit.getId(), unit);
        }

        setArmyName();
        showArmyConfig();

    }


    private void showArmyConfig() {

        ArmyManagerController.availableUnits.forEach((s, unit) -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/unitConfig.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                UnitConfigController configController = fxmlLoader.getController();
                configController.loadConfig(unit);
                configControllers.add(configController);
                armyList.getItems().add(parent);
                armyList.setOrientation(Orientation.HORIZONTAL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        for (int i = 1; i <= 3; i++) {
            currentArmy = loadArmyConfig(i);
            for (int j = 0; j < configControllers.size(); j++) {
                configControllers.get(j).loadNumberOfUnit(currentArmy, i);
            }
        }

    }

    private void setArmyName() {
        armyBuffer1 = loadArmyConfig(1);
        armyBuffer2 = loadArmyConfig(2);
        armyBuffer3 = loadArmyConfig(3);

        army1.setText(armyBuffer1.getName());
        army2.setText(armyBuffer2.getName());
        army3.setText(armyBuffer3.getName());
    }

    private Army loadArmyConfig(int number) {
        return SerializeUtils.deserialize(new File(String.format(ARMY_SAVE_PATH.toString(), number)), Army.class);
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
        } else if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        } else if (event.getSource().equals(select1)) {
            CreateArmyRequest req = new CreateArmyRequest(armyBuffer1.getName(), armyBuffer1.getUnits(),
                LoginController.getUserKey());
            req.sendRequest();
            select1.setDisable(true);
            select2.setDisable(false);
            select3.setDisable(false);
        } else if (event.getSource().equals(select2)) {
            CreateArmyRequest req = new CreateArmyRequest(armyBuffer2.getName(), armyBuffer2.getUnits(),
                LoginController.getUserKey());
            req.sendRequest();
            select2.setDisable(true);
            select1.setDisable(false);
            select3.setDisable(false);

        } else if (event.getSource().equals(select3)) {
            CreateArmyRequest req = new CreateArmyRequest(armyBuffer3.getName(), armyBuffer3.getUnits(),
                LoginController.getUserKey());
            req.sendRequest();
            select3.setDisable(true);
            select1.setDisable(false);
            select2.setDisable(false);

        } else if (event.getSource().equals(btnMyReady)) {
            QueryArmiesRequest req = new QueryArmiesRequest(LoginController.getUserKey());
            req.sendRequest();
            ArrayList<Army> serverArmies = req.getArmies();
            loadFromServer();
            GameSocket.instance.readyToPlay();
        } else if (event.getSource().equals(btnStart)) {
            VBox chatWindow = (VBox) gameLobby.getScene().lookup("#chatWindow");
            JFXButton btnMinimize = (JFXButton) chatWindow.lookup("#btnMinimize");
            btnMinimize.setDisable(false);

            UserInterfaceUtils.makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/fxmls/inGame.fxml", gameLobby,
                gameLobby.getScene().lookup("#chatWindow"));
            GameSocket.instance.startGame();

        }
    }

    private void loadFromServer() {
        QueryArmiesRequest req = new QueryArmiesRequest(LoginController.getUserKey());
        req.sendRequest();
        ArrayList<Army> serverArmies = req.getArmies();

        if (serverArmies.size() == 0) {
            NotificationHandler.getInstance()
                .sendInfo("Keine Armeen auf dem Server gespeichert.", logger);
        }
    }


}

