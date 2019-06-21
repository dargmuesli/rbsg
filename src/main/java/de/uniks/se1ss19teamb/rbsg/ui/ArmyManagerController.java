package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.units.*;
import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
    private ListView<Parent> unitList;
    @FXML
    private Button btnChg;
    @FXML
    private Button btnSave1;
    @FXML
    private Button btnSave2;
    @FXML
    private Button btnSave3;
    @FXML
    private JFXTextField txtfldArmyName;
    @FXML
    private Label labelArmyName;
    @FXML
    private AnchorPane mainPane1;
    private String whiteMode = "-fx-control-inner-background: white;" + "-fx-background-insets: 0;"
        + "-fx-padding: 0px;";
    private String darkMode = "-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0;"
        + "-fx-padding: 0px;";
    private String mode;
    private String cssDark = "/de/uniks/se1ss19teamb/rbsg/css/dark-design2.css";
    private String cssWhite = "/de/uniks/se1ss19teamb/rbsg/css/white-design2.css";
    private String path = "./src/main/resources/de/uniks/se1ss19teamb/rbsg/cssMode.json";
    LoginController loginController = new LoginController();

    private static final Logger logger = LogManager.getLogger();

    private BazookaTrooper bazookaTrooper = new BazookaTrooper();
    private Chopper chopper = new Chopper();
    private HeavyTank heavyTank = new HeavyTank();
    private Infantry infantry = new Infantry();
    private Jeep jeep = new Jeep();
    private LightTank lightTank = new LightTank();
    ArrayList<Unit> units = new ArrayList<>(Arrays.asList(bazookaTrooper, chopper,
        heavyTank, infantry, jeep, lightTank));

    private int bazookaTrooperCount = 0;
    private int chopperCounter = 0;
    private int heavyTankCounter = 0;
    private int infantryCounter = 0;
    private int jeepCounter = 0;
    private int lightTankCounter = 0;

    private int leftUnits = 10;

    // saveMode = true -> Buttons save configuration.
    // saveMode = false -> Buttons laod configuration
    private boolean saveMode = true;
    private ArrayList<UnitObjectController> unitObjectControllers = new ArrayList<>();
    /*
        0 -> Bazooka Trooper
        1 -> Chopper
        2 -> Heavy Tank
        3 -> Infantry
        4 -> Jeep
        5 -> Light Tank
     */

    private ArrayList<Army> savedArmies = new ArrayList<>();
    private Army currentArmy = new Army();
    private Army armySave1 = null;
    private Army armySave2 = null;
    private Army armySave3 = null;

    public void initialize() {

        loginController.changeTheme(mainPane, mainPane1, path, cssDark, cssWhite);
        if (SerializeUtils.deserialize(new File(path), boolean.class)) {
            mode = darkMode;
        } else {
            mode = whiteMode;
        }

        hamTran(ham, btnBack);
        hamTran(ham, btnLogout);
        hamTran(ham, btnFullScreen);
        UserInterfaceUtils.makeFadeInTransition(mainPane);
        setLabelLeftUnits(10);
        setUpUnitObjects();
    }

    private void setUpUnitObjects() {
        unitList.setStyle("-fx-background-color:transparent;");
        unitList.setStyle(mode);
        ObservableList items = unitList.getItems();
        for (Unit unit : units) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/unitObject.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                UnitObjectController controller = fxmlLoader.getController();
                controller.setUpUnitObject(unit, this);
                unitObjectControllers.add(controller);
                unitList.getItems().add(parent);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    int getLeftUnits() {
        return leftUnits;
    }

    void setLeftUnits(int leftUnits) {
        this.leftUnits = leftUnits;
        setLabelLeftUnits(leftUnits);
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

    private void setLabelLeftUnits(int count) {
        labelLeftUnits.setText(Integer.toString(count));
    }

    public void changeSaveMode() {
        saveMode = !saveMode;
        if (saveMode) {
            btnSave1.setText("Save 1");
            btnSave2.setText("Save 2");
            btnSave3.setText("Save 3");
        } else {
            btnSave1.setText("Load 1");
            btnSave2.setText("Load 2");
            btnSave3.setText("Load 3");
        }
    }

    public void loadFromServer() {
        QueryArmiesRequest req = new QueryArmiesRequest(LoginController.getUserKey());
        req.sendRequest();
        ArrayList<Army> serverArmies = req.getArmies();
        if (serverArmies.size() == 0) {
            NotificationHandler.getNotificationHandler()
                .sendInfo("Keine Armeen auf dem Server gespeichert.", logger);
        } else {
            Army firstArmy = serverArmies.get(0);
            currentArmy = firstArmy;
            labelArmyName.setText(currentArmy.getName());
            updateConfigurationView(firstArmy);

        }
    }

    private void updateConfigurationView(Army army) {

        for (String unitId : army.getUnits()) {
            if (unitId.equals("5cc051bd62083600017db3b7")) {
                unitObjectControllers.get(0).increaseCount();
            } else if (unitId.equals("5cc051bd62083600017db3bb")) {
                unitObjectControllers.get(1).increaseCount();
            } else if (unitId.equals("5cc051bd62083600017db3ba")) {
                unitObjectControllers.get(2).increaseCount();
            } else if (unitId.equals("5cc051bd62083600017db3b6")) {
                unitObjectControllers.get(3).increaseCount();
            } else if (unitId.equals("5cc051bd62083600017db3b8")) {
                unitObjectControllers.get(4).increaseCount();
            } else if (unitId.equals("5cc051bd62083600017db3b9")) {
                unitObjectControllers.get(5).increaseCount();
            }
        }

    }

    public void saveToServer() {
        setArmyConfiguration();
        String currentArmyName = currentArmy.getName();
        String currentArmyId = currentArmy.getId();
        ArrayList<String> currentArmyUnits = currentArmy.getUnits();

        if (currentArmyName == null) {
            NotificationHandler.getNotificationHandler().sendError("You have to give the army a name!",
                logger);
            return;
        }

        if (currentArmyUnits.size() < 10) {
            NotificationHandler.getNotificationHandler().sendError("You need at least ten units!", logger);
            return;
        }

        if (currentArmyId == null) {
            CreateArmyRequest req = new CreateArmyRequest(currentArmyName, currentArmyUnits,
                LoginController.getUserKey());
            req.sendRequest();
            currentArmy.setId(req.getArmyID());

        } else {
            UpdateArmyRequest req = new UpdateArmyRequest(currentArmyId, currentArmyName, currentArmyUnits,
                LoginController.getUserKey());
            req.sendRequest();
        }

        NotificationHandler.getNotificationHandler().sendSuccess("The Army was saved.", logger);
    }

    private void setArmyConfiguration() {
        currentArmy.setUnits(getCurrentConfiguration().getUnits());
    }

    private Army getCurrentConfiguration() {
        int bazTroopCount = unitObjectControllers.get(0).getCount();
        ArrayList<String> allIds = new ArrayList<>();
        for (int i = 0; i < bazTroopCount; i++) {
            allIds.add("5cc051bd62083600017db3b7");
        }

        int chopperCount = unitObjectControllers.get(1).getCount();
        for (int i = 0; i < chopperCount; i++) {
            allIds.add("5cc051bd62083600017db3bb");
        }

        int heavyTankCount = unitObjectControllers.get(2).getCount();
        for (int i = 0; i < heavyTankCount; i++) {
            allIds.add("5cc051bd62083600017db3ba");
        }

        int infantryCount = unitObjectControllers.get(3).getCount();
        for (int i = 0; i < infantryCount; i++) {
            allIds.add("5cc051bd62083600017db3b6");
        }

        int jeepCount = unitObjectControllers.get(4).getCount();
        for (int i = 0; i < jeepCount; i++) {
            allIds.add("5cc051bd62083600017db3b8");
        }

        int lightTankCount = unitObjectControllers.get(5).getCount();
        for (int i = 0; i < lightTankCount; i++) {
            allIds.add("5cc051bd62083600017db3b9");
        }
        Army army = new Army();
        army.setUnits(allIds);
        return army;
    }

    public void setArmyName() {
        if (txtfldArmyName.getText().equals("")) {
            NotificationHandler.getNotificationHandler().sendError("You have to type in a name!", logger);
            return;
        }
        currentArmy.setName(txtfldArmyName.getText());
        labelArmyName.setText(txtfldArmyName.getText());
        txtfldArmyName.setText("");
    }

    public void saveLoadCurrent1() {
        if (saveMode) {
            saveCurrentConfig(1);
        } else {
            //TODO: load function
        }
    }

    public void saveLoadCurrent2() {
        if (saveMode) {
            saveCurrentConfig(2);
        }
    }

    public void saveLoadCurrent3() {
        if (saveMode) {
            saveCurrentConfig(3);
        }
    }

    private void saveCurrentConfig(int configNum) {
        String armysavePath1 = "./src/main/resources/de/uniks/se1ss19teamb/rbsg/armySaves/armySave1.json";
        String armysavePath2 = "./src/main/resources/de/uniks/se1ss19teamb/rbsg/armySaves/armySave2.json";
        String armysavePath3 = "./src/main/resources/de/uniks/se1ss19teamb/rbsg/armySaves/armySave3.json";

        switch (configNum) {
            case 1:
                armySave1 = currentArmy;
                SerializeUtils.serialize(armysavePath1, armySave1);
                NotificationHandler.getNotificationHandler()
                    .sendSuccess("Configuration saved to Save 1.", logger);
                break;

            case 2:
                armySave2 = currentArmy;
                SerializeUtils.serialize(armysavePath2, armySave2);
                NotificationHandler.getNotificationHandler()
                    .sendSuccess("Configuration saved to Save 2.", logger);
                break;

            case 3:
                armySave3 = currentArmy;
                SerializeUtils.serialize(armysavePath3, armySave3);
                NotificationHandler.getNotificationHandler()
                    .sendSuccess("Configuration saved to Save 3.", logger);
                break;

            default:
                NotificationHandler.getNotificationHandler()
                    .sendError("Wrong configNumber", logger);

        }
    }
}

