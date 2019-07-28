package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.util.ArmyUtil;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;
import de.uniks.se1ss19teamb.rbsg.util.Theming;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArmyManagerController {

    static Army currentArmy = new Army(null, null, new ArrayList<>());
    static final int MAXIMUM_UNIT_COUNT = 10;
    static boolean spectator = false;
    static boolean joiningGame;
    public static Map<String, Unit> availableUnits = new HashMap<>();

    private static final Logger logger = LogManager.getLogger();
    private static final Path ARMY_SAVE_PATH =
        Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "rbsg_army-save-%d.json");
    private static ArrayList<UnitObjectController> unitObjectControllers = new ArrayList<>();

    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane errorContainer;
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

    private boolean saveMode = true;
    private Army[] armySaves = new Army[3];
    private JFXButton btnJoinGame = new JFXButton("Join Game");
    private static ArmyManagerController instance;

    public static ArmyManagerController getInstance() {
        return instance;
    }

    public void initialize() {
        UserInterfaceUtils.initialize(mainPane, mainPane1, ArmyManagerController.class, btnFullscreen, errorContainer);

        ArmyManagerController.instance = this;

        QueryUnitsRequest queryUnitsRequest = new QueryUnitsRequest(LoginController.getUserKey());
        queryUnitsRequest.sendRequest();

        if (!queryUnitsRequest.getSuccessful()) {
            LogManager.getLogger().error("Could not query units!");
            return;
        }

        for (Unit unit : queryUnitsRequest.getUnits()) {
            availableUnits.put(unit.getId(), unit);
        }

        if (joiningGame) {
            btnJoinGame.setOnAction(this::setOnAction);
            hboxLowerButtons.getChildren().add(btnJoinGame);
        }
        setLabelLeftUnits(10);
        setUpUnitObjects();
    }

    private void setUpUnitObjects() {
        unitList.getStyleClass().add(Theming.darkModeActive() ? "darkMode_UnitObject.css" : "whiteMode_UnitObject.css");

        availableUnits.forEach((s, unit) -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/unitObject.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                UnitObjectController controller = fxmlLoader.getController();
                controller.setUpUnitObject(unit);
                unitObjectControllers.add(controller);
                unitList.getItems().add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
            if (currentArmy.getUnits().size() < 10) {
                NotificationHandler.getInstance().sendInfo("You need ten units. Add some.", logger);
                return;
            }
            ArmyUtil.saveToServer(currentArmy);
            QueryArmiesRequest req = new QueryArmiesRequest(LoginController.getUserKey());
            req.sendRequest();
            ArrayList<Army> serverArmies = req.getArmies();
            loadFromServer();
            VBox chatWindow = (VBox) mainPane.getScene().lookup("#chatWindow");
            JFXButton btnMinimize = (JFXButton) chatWindow.lookup("#btnMinimize");
            btnMinimize.setDisable(false);

            if (serverArmies.size() != 0) {
                UserInterfaceUtils.makeFadeOutTransition("/de/uniks/se1ss19teamb/rbsg/fxmls/inGame.fxml", mainPane,
                    mainPane.getScene().lookup("#chatWindow"));
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
            currentArmy = serverArmies.get(0);
            updateConfigurationView();
        }
    }

    void updateConfigurationView() {
        labelLeftUnits.setText("" + (MAXIMUM_UNIT_COUNT - currentArmy.getUnits().size()));
        unitObjectControllers.forEach(
            unitObjectController -> unitObjectController.update(
                (int) currentArmy.getUnits().stream().filter(
                    unit -> unit.getType()
                        .equals(unitObjectController.getUnit().getType()))
                    .count()));

        labelArmyName.setText(currentArmy.getName());
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
        saveLoadCurrent(1);
    }

    public void saveLoadCurrent2() {
        saveLoadCurrent(2);
    }

    public void saveLoadCurrent3() {
        saveLoadCurrent(3);
    }

    private void saveLoadCurrent(int number) {
        if (saveMode) {
            saveCurrentConfig(number);
        } else {
            currentArmy = loadConfig(number);

            if (currentArmy == null) {
                NotificationHandler.getInstance().sendInfo("The save is empty", logger);
            }

            updateConfigurationView();
        }
    }

    private Army loadConfig(int number) {
        return SerializeUtils.deserialize(new File(String.format(ARMY_SAVE_PATH.toString(), number)), Army.class);
    }

    private void saveCurrentConfig(int configNum) {
        armySaves[configNum - 1] = currentArmy;
        SerializeUtils.serialize(String.format(ARMY_SAVE_PATH.toString(), configNum), armySaves[configNum - 1]);
        NotificationHandler.getInstance()
            .sendSuccess("Configuration saved to slot " + configNum + ".", logger);
    }

    public void saveToServer() {
        ArmyUtil.saveToServer(currentArmy);
    }

}

