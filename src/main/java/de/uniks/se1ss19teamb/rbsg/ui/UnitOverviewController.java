package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.units.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class UnitOverviewController {
    @FXML
    Label lblCount;
    @FXML
    JFXButton btnMore;
    @FXML
    JFXButton btnLess;
    @FXML
    Button btnLoad;
    @FXML
    Button btnSave;
    @FXML
    private ListView<Parent> unitList;

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

    private int count = 0;
    private ArmyManagerController armyManagerController;
    private int leftUnits = 10;

    public void initialize() {
        setUpUnitObjects();
    }

    private void setUpUnitObjects() {
        unitList.setStyle("-fx-background-color:transparent;");
        unitList.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;"
            + "-fx-padding: 0px;");
        ObservableList items = unitList.getItems();
        for (Unit unit : units) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/unitObject.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                UnitObjectController controller = fxmlLoader.getController();
                controller.setUpUnitObject(unit, this);
                unitList.getItems().add(parent);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void setArmyManagerController(ArmyManagerController armyManagerController) {
        this.armyManagerController = armyManagerController;
    }

    int getLeftUnits() {
        return leftUnits;
    }

    void setLeftUnits(int leftUnits) {
        this.leftUnits = leftUnits;
        armyManagerController.setLabelLeftUnits(leftUnits);
    }
}
