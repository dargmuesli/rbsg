package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.units.*;
import de.uniks.se1ss19teamb.rbsg.request.*;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
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
    private AnchorPane mainPane1;
    private String cssDark = "/de/uniks/se1ss19teamb/rbsg/css/dark-design2.css";
    private String cssWhite = "/de/uniks/se1ss19teamb/rbsg/css/white-design2.css";
    private String path = "./src/main/resources/de/uniks/se1ss19teamb/rbsg/cssMode.json";
    LoginController loginController = new LoginController();

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

    // saveMode = true -> Buttons save configuration.
    // saveMode = false -> Buttons laod configuration
    private boolean saveMode = true;

    public void initialize() {

        loginController.changeTheme(mainPane, mainPane1, path, cssDark, cssWhite);

        hamTran(ham, btnBack);
        hamTran(ham, btnLogout);
        hamTran(ham, btnFullScreen);
        UserInterfaceUtils.makeFadeInTransition(mainPane);
        setLabelLeftUnits(10);
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

    void setLabelLeftUnits(int count) {
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
}

