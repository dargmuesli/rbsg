package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.request.QueryArmiesRequest;
import de.uniks.se1ss19teamb.rbsg.request.QueryUnitsRequest;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;

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
    private ListView unitList;


    private int count = 0;

    public void initialize() {
        updateCount();
        setUpUnitObjects();
    }

    @FXML
    private void eventHandler(ActionEvent event) {
        switch (((JFXButton) event.getSource()).getId()) {
            case "btnMore":
                count++;
                updateCount();
                break;
            case "btnLess":
                if (count > 0) {
                    count--;
                    updateCount();
                }

                break;
            default:
        }
    }

    private void updateCount() {
        if (count == 0) {
            btnLess.setDisable(true);
        } else {
            btnLess.setDisable(false);
        }

        lblCount.setText("Anzahl: " + count);
    }

    public void loadConfiguration() {
        QueryArmiesRequest req = new QueryArmiesRequest(LoginController.getUserKey());
        req.sendRequest();
    }

    private void setUpUnitObjects() {
        unitList.setStyle("-fx-background-color:transparent;");
        unitList.setStyle("-fx-control-inner-background: #2A2E37;" + "-fx-background-insets: 0 ;"
            + "-fx-padding: 0px;");
        ObservableList items = unitList.getItems();
        QueryUnitsRequest unitsRequest = new QueryUnitsRequest(LoginController.getUserKey());
        unitsRequest.sendRequest();
        ArrayList<Unit> units = unitsRequest.getUnits();
        for (Unit unit : units) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/de/uniks/se1ss19teamb/rbsg/fxmls/unitObject.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                UnitObjectController controller = fxmlLoader.getController();
                controller.setUpUnitObject(unit, this);
                unitList.getItems().add(parent);

            } catch (IOException e) {
                // notificationHandler.sendError("Ein UnitObject konnte nicht erstellt werden.", logger, e);
                e.printStackTrace();
            }

        }
    }
}
