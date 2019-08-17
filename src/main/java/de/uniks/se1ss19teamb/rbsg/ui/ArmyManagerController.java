package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.uniks.se1ss19teamb.rbsg.model.Army;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocketDistributor;
import de.uniks.se1ss19teamb.rbsg.util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArmyManagerController {

    public static Map<String, Unit> availableUnits = new HashMap<>();

    static boolean spectator = false;
    static Army army = null;

    boolean discardConfirmation = true;

    private static final int MAXIMUM_UNIT_COUNT = 10;
    private static final Logger logger = LogManager.getLogger();
    private static Army armyBeforeEdit = null;
    private static ArmyManagerController instance;
    private static ArrayList<UnitObjectController> unitObjectControllers = new ArrayList<>();

    @FXML
    private FontAwesomeIconView btnEditIcon;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXButton btnExport;
    @FXML
    private JFXButton btnImport;
    @FXML
    private JFXButton btnRemove;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXComboBox<Army> cmbArmies;
    @FXML
    private JFXTextField txtfldArmyName;
    @FXML
    private Label labelLeftUnits;
    @FXML
    private VBox unitList;
    @FXML
    private VBox vboxData;

    private Tooltip tooltipEdit = new Tooltip("edit");
    private Tooltip tooltipDiscard = new Tooltip("discard");

    public static ArmyManagerController getInstance() {
        return instance;
    }

    @FXML
    private void initialize() {
        ArmyManagerController.instance = this;

        RequestUtil.request(new QueryUnitsRequest(LoginController.getUserToken())).ifPresent(units -> {
            for (Unit unit : units) {
                availableUnits.put(unit.getId(), unit);
            }
        });

        btnAdd.setTooltip(new Tooltip("add"));
        btnRemove.setTooltip(new Tooltip("remove"));
        btnEdit.setTooltip(tooltipEdit);
        btnSave.setTooltip(new Tooltip("save"));
        btnImport.setTooltip(new Tooltip("import"));
        btnExport.setTooltip(new Tooltip("export"));

        cmbArmies.setOnAction((event) -> {
            army = cmbArmies.getSelectionModel().getSelectedItem();

            updateUnits();

            btnSave.setDisable(true);

            if (army == null) {
                btnRemove.setDisable(true);
                btnExport.setDisable(true);
                return;
            }

            btnRemove.setDisable(false);
            btnExport.setDisable(false);

            txtfldArmyName.setText(army.getName());

            if (GameLobbyController.instance != null) {
                Objects.requireNonNull(GameSocketDistributor.getGameSocket(0)).changeArmy(army.getId());
            }
        });

        txtfldArmyName.setOnKeyReleased(event -> {
            discardConfirmation = false;

            army.setName(txtfldArmyName.getText());

            if (army.getName() == null || army.getName().equals("")
                || army.getUnits() == null || army.getUnits().size() < 10) {
                btnSave.setDisable(true);
                btnExport.setDisable(true);
            } else {
                btnSave.setDisable(false);
                btnExport.setDisable(false);
            }
        });

        setUpUnitObjects();
        loadFromServer();
    }

    private void setUpUnitObjects() {
        availableUnits.forEach((s, unit) -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/modules/unitObject.fxml"));

            try {
                Parent parent = fxmlLoader.load();
                UnitObjectController controller = fxmlLoader.getController();
                controller.setUpUnitObject(unit);
                unitObjectControllers.add(controller);
                unitList.getChildren().add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    void updateUnits() {
        if (army == null || army.getUnits() == null) {
            labelLeftUnits.setText(String.valueOf(MAXIMUM_UNIT_COUNT));
        } else {
            int unitsLeft = MAXIMUM_UNIT_COUNT - army.getUnits().size();

            if (unitsLeft == 0 && !army.getName().equals("")) {
                btnSave.setDisable(false);
                btnExport.setDisable(false);
            } else {
                btnSave.setDisable(true);
                btnExport.setDisable(true);
            }

            txtfldArmyName.setText(army.getName());
            labelLeftUnits.setText(String.valueOf(unitsLeft));
            unitObjectControllers.forEach(
                unitObjectController -> unitObjectController.update(
                    (int) army.getUnits().stream().filter(
                        unit -> unit.getType()
                            .equals(unitObjectController.getUnit().getType()))
                        .count()));
        }
    }

    private void loadFromServer() {
        RequestUtil.request(new QueryArmiesRequest(LoginController.getUserToken())).ifPresent(armies -> {
            cmbArmies.getItems().clear();

            if (armies.size() != 0) {
                cmbArmies.getItems().addAll(armies);
            }
        });
    }

    @FXML
    private void addArmy() {
        cmbArmies.getSelectionModel().clearSelection();

        discardConfirmation = false;
        army = new Army();

        btnRemove.setDisable(false);
        btnEdit.setDisable(false);
        btnExport.setDisable(true);

        updateUnits();
        editArmy();
    }

    @FXML
    private void removeArmy() {
        if (army.getId() == null || army.getId().equals("")) {
            army = null;
        } else {
            RequestUtil.request(new DeleteArmyRequest(army.getId(), LoginController.getUserToken()));
            loadFromServer();
        }

        btnAdd.setDisable(false);
        btnRemove.setDisable(true);
        btnEdit.setDisable(true);
        btnSave.setDisable(true);
        btnImport.setDisable(false);
        btnExport.setDisable(true);

        updateUnits();
    }

    @FXML
    private void editArmy() {
        if (btnEditIcon.getGlyphName().equals("PENCIL")) {
            armyBeforeEdit = new Army(army);

            cmbArmies.setDisable(true);
            btnAdd.setDisable(true);
            btnRemove.setDisable(true);
            btnEditIcon.setGlyphName("TIMES");
            btnImport.setDisable(true);
            vboxData.setDisable(false);

            btnEdit.setTooltip(tooltipDiscard);

            if (army.getName() != null && !army.getName().equals("") && army.getUnits() != null) {
                btnSave.setDisable(false);
            }
        } else if (btnEditIcon.getGlyphName().equals("TIMES")) {
            army = armyBeforeEdit;
            armyBeforeEdit = null;

            updateUnits();

            txtfldArmyName.setText(army.getName());
            cmbArmies.setDisable(false);
            btnRemove.setDisable(false);
            btnEditIcon.setGlyphName("PENCIL");
            btnSave.setDisable(true);
            btnImport.setDisable(false);
            vboxData.setDisable(true);

            btnEdit.setTooltip(tooltipEdit);
        }
    }

    @FXML
    protected void saveArmy() {
        discardConfirmation = true;

        btnSave.setDisable(true);
        btnEditIcon.setGlyphName("PENCIL");
        vboxData.setDisable(true);

        saveToServer();
        loadFromServer();

        for (Army army : cmbArmies.getItems()) {
            if (army.getId().equals(ArmyManagerController.army.getId())) {
                cmbArmies.getSelectionModel().select(army);
                break;
            }
        }

        cmbArmies.setDisable(false);
    }

    @FXML
    private void exportArmy() {
        SerializeUtil.chooseFile().ifPresent(file -> {
            SerializeUtil.serialize(file.getAbsolutePath(), army);
            NotificationHandler.sendSuccess("Exported successfully.", logger);
        });
    }

    @FXML
    private void importArmy() {
        SerializeUtil.chooseFile().ifPresent(file -> {
            army = SerializeUtil.deserialize(file, Army.class);

            NotificationHandler.sendSuccess("Imported successfully.", logger);

            btnEdit.setDisable(false);
            txtfldArmyName.setText(army.getName());
            updateUnits();

            if (army.getId() != null && !army.getId().equals("")) {
                saveToServer();
            } else {
                discardConfirmation = false;
            }
        });
    }

    @FXML
    private void saveToServer() {
        if (army.getName() == null || army.getName().equals("")) {
            NotificationHandler.sendError("You have to give the army a name!",
                logger);
            return;
        }

        if (army.getUnits() == null || army.getUnits().size() < 10) {
            NotificationHandler.sendError("You need at least ten units!", logger);
            return;
        }

        if (army.getId() == null || army.getId().equals("")) {
            RequestUtil.request(new CreateArmyRequest(army.getName(), army.getUnits(), LoginController.getUserToken()))
                .ifPresent(s -> {
                    NotificationHandler.sendSuccess("The Army was saved.", logger);
                    army.setId(s);
                });
        } else {
            if (!RequestUtil.request(new UpdateArmyRequest(army, LoginController.getUserToken()))) {
                return;
            }

            NotificationHandler.sendSuccess("The Army was updated.", logger);
        }
    }
}

