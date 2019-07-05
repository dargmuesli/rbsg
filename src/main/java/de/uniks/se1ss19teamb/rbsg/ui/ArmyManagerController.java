package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Troop;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.units.*;
import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArmyManagerController {
    private static final Logger logger = LogManager.getLogger();
    static Army currentArmy = new Army();
    static boolean joiningGame;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnFullscreen;
    @FXML
    private JFXButton btnBack;
    @FXML
    private Label labelLeftUnits;
    @FXML
    private ListView<Parent> unitList;
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
    @FXML
    private HBox hboxLowerButtons;
    private BazookaTrooper bazookaTrooper = new BazookaTrooper();
    private Chopper chopper = new Chopper();
    private HeavyTank heavyTank = new HeavyTank();
    private Infantry infantry = new Infantry();
    private Jeep jeep = new Jeep();
    private LightTank lightTank = new LightTank();
    private ArrayList<Unit> units = new ArrayList<>(Arrays.asList(bazookaTrooper, chopper,
        heavyTank, infantry, jeep, lightTank));
    private int leftUnits = 10;
    private boolean saveMode = true;
    private ArrayList<UnitObjectController> unitObjectControllers = new ArrayList<>();
    private Army[] armySaves = new Army[3];
    private String armysavePath = "./src/main/resources/de/uniks/se1ss19teamb/rbsg/armySaves/armySave%d.json";
    private JFXButton btnJoinGame = new JFXButton("Join Game");

    public void initialize() {
        Theming.setTheme(Arrays.asList(new Pane[]{mainPane, mainPane1}));

        UserInterfaceUtils.updateBtnFullscreen(btnFullscreen);

        if (joiningGame) {
            btnJoinGame.setOnAction(this::setOnAction);
            hboxLowerButtons.getChildren().add(btnJoinGame);
        }

        UserInterfaceUtils.makeFadeInTransition(mainPane);
        setLabelLeftUnits(10);
        setUpUnitObjects();
    }

    private void setUpUnitObjects() {
        // TODO: Tipp für @Chris: CSS Properties setzt man über Dateien, nicht über einzelne flags :)
        String whiteMode = "-fx-control-inner-background: white;" + "-fx-background-insets: 0;"
            + "-fx-padding: 0px;";
        String darkMode = "-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0;"
            + "-fx-padding: 0px;";

        unitList.setStyle("-fx-background-color:transparent;");
        unitList.setStyle(Theming.darkModeActive() ? darkMode : whiteMode);

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
        if (event.getSource().equals(btnBack)) {
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", mainPane);
        }
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
        } else if (event.getSource().equals(btnFullscreen)) {
            UserInterfaceUtils.toggleFullscreen(btnFullscreen);
        } else if (event.getSource().equals(btnJoinGame)) {
            if (leftUnits > 0) {
                NotificationHandler.getInstance().sendInfo("You need ten units. Add some.", logger);
                return;
            }
            saveToServer();
            QueryArmiesRequest req = new QueryArmiesRequest(LoginController.getUserKey());
            req.sendRequest();
            ArrayList<Army> serverArmies = req.getArmies();
            loadFromServer();

            if (serverArmies.size() != 0) {
                UserInterfaceUtils.makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/fxmls/inGame.fxml", mainPane);
            }
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
            NotificationHandler.getInstance()
                .sendInfo("Keine Armeen auf dem Server gespeichert.", logger);
        } else {
            Army firstArmy = serverArmies.get(0);
            currentArmy = firstArmy;
            labelArmyName.setText(currentArmy.getName());
            updateConfigurationView(firstArmy);
        }
    }

    private void updateConfigurationView(Army army) {
        for (UnitObjectController controller : unitObjectControllers) {
            controller.setCount(0);
            leftUnits = 10;
        }

        for (String unitId : army.getUnits()) {
            switch (Troop.keyOf(unitId)) {
                case BAZOOKA_TROOPER:
                    unitObjectControllers.get(0).increaseCount();
                    break;
                case CHOPPER:
                    unitObjectControllers.get(1).increaseCount();
                    break;
                case HEAVY_TANK:
                    unitObjectControllers.get(2).increaseCount();
                    break;
                case INFANTRY:
                    unitObjectControllers.get(3).increaseCount();
                    break;
                case JEEP:
                    unitObjectControllers.get(4).increaseCount();
                    break;
                case LIGHT_TANK:
                    unitObjectControllers.get(5).increaseCount();
                    break;
                default:
                    NotificationHandler.getInstance().sendWarning("Unknown unit id!", logger);
            }
        }

        labelArmyName.setText(army.getName());
    }

    public void saveToServer() {
        setArmyConfiguration();
        String currentArmyName = currentArmy.getName();
        String currentArmyId = currentArmy.getId();
        ArrayList<String> currentArmyUnits = currentArmy.getUnits();

        if (currentArmyName == null) {
            NotificationHandler.getInstance().sendError("You have to give the army a name!",
                logger);
            return;
        }

        if (currentArmyUnits.size() < 10) {
            NotificationHandler.getInstance().sendError("You need at least ten units!", logger);
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

        NotificationHandler.getInstance().sendSuccess("The Army was saved.", logger);
    }

    private void setArmyConfiguration() {
        currentArmy.setUnits(getCurrentConfiguration().getUnits());
    }

    private Army getCurrentConfiguration() {
        ArrayList<String> allIds = new ArrayList<>();

        for (int i = 0; i < unitObjectControllers.get(0).getCount(); i++) {
            allIds.add(Troop.BAZOOKA_TROOPER.id);
        }

        for (int i = 0; i < unitObjectControllers.get(1).getCount(); i++) {
            allIds.add(Troop.CHOPPER.id);
        }

        for (int i = 0; i < unitObjectControllers.get(2).getCount(); i++) {
            allIds.add(Troop.HEAVY_TANK.id);
        }

        for (int i = 0; i < unitObjectControllers.get(3).getCount(); i++) {
            allIds.add(Troop.INFANTRY.id);
        }

        for (int i = 0; i < unitObjectControllers.get(4).getCount(); i++) {
            allIds.add(Troop.JEEP.id);
        }

        for (int i = 0; i < unitObjectControllers.get(5).getCount(); i++) {
            allIds.add(Troop.LIGHT_TANK.id);
        }

        currentArmy.setUnits(allIds);

        return currentArmy;
    }

    public void setArmyName() {
        if (txtfldArmyName.getText().equals("")) {
            NotificationHandler.getInstance().sendError("You have to type in a name!", logger);
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
            currentArmy = loadConfig(1);

            if (currentArmy == null) {
                NotificationHandler.getInstance().sendInfo("The save is empty", logger);
            }

            updateConfigurationView(currentArmy);
        }
    }

    public void saveLoadCurrent2() {
        if (saveMode) {
            saveCurrentConfig(2);
        } else {
            currentArmy = loadConfig(2);

            if (currentArmy == null) {
                NotificationHandler.getInstance().sendInfo("The save is empty", logger);
            }

            updateConfigurationView(currentArmy);
        }
    }

    public void saveLoadCurrent3() {
        if (saveMode) {
            saveCurrentConfig(3);
        } else {
            currentArmy = loadConfig(3);

            if (currentArmy == null) {
                NotificationHandler.getInstance().sendInfo("The save is empty", logger);
            }

            updateConfigurationView(currentArmy);
        }
    }

    private Army loadConfig(int number) {
        return SerializeUtils.deserialize(new File(String.format(armysavePath, number)), Army.class);
    }

    private void saveCurrentConfig(int configNum) {
        armySaves[configNum - 1] = getCurrentConfiguration();
        SerializeUtils.serialize(String.format(armysavePath, configNum), armySaves[configNum - 1]);
        NotificationHandler.getInstance()
            .sendSuccess("Configuration saved to slot " + configNum + ".", logger);
    }
}

