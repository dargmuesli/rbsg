package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class TurnUiController {
    @FXML
    private Label labelOne;
    @FXML
    private Label labelTwo;
    @FXML
    private Label labelThree;
    @FXML
    private Label labelFour;
    @FXML
    private JFXButton phaseBtn;
    @FXML
    private Label turnLabel;
    public ArrayList<InGamePlayer> inGamePlayerList = new ArrayList<>();
    public ArrayList<Label> lblList = new ArrayList<>();

    public static TurnUiController instance;

    public static TurnUiController getInstance() {
        return instance;
    }

    public void initialize() {
        instance = this;
        phaseBtn.setTranslateY(-4);
        Platform.runLater(() -> {
            updatePlayers();
            //setTurn("movePhase");
        });
    }

    public void updatePlayers() {
        // add players from InGameController.ingameObjects to a playerList
        InGameController.inGameObjects.entrySet().stream()
            .filter(stringInGameObjectEntry -> stringInGameObjectEntry.getValue() instanceof InGamePlayer)
            .forEachOrdered(inGameObjectEntry -> inGamePlayerList.add(((InGamePlayer)inGameObjectEntry.getValue())));
        System.out.println(inGamePlayerList.get(0).getName() + "" + inGamePlayerList.get(1).getName());
        // check size of list and add lbls according to size, set labels visible
        if (inGamePlayerList.size() == 2) {
            lblList.add(0, labelOne);
            lblList.add(1, labelTwo);
            for (Label label: lblList) {
                label.setVisible(true);
            }
            players();
        } else if (inGamePlayerList.size() == 4) {
            lblList.add(0, labelOne);
            lblList.add(1, labelTwo);
            lblList.add(2, labelThree);
            lblList.add(3, labelFour);
            for (Label label: lblList) {
                label.setVisible(true);
            }
            players();
        }
    }

    @FXML
    private void setOnAction(ActionEvent event) {
        if (event.getSource().equals(phaseBtn)) {
            GameSocket.instance.nextPhase();
        }
    }

    public void players() {
        // initalize labels with names
        if (inGamePlayerList.size() == 2) {
            for (int i = 0; i < lblList.size(); i++) {
                lblList.get(i).setText(inGamePlayerList.get(i).getName());
            }
        } else if (inGamePlayerList.size() == 4) {
            for (int i = 0; i < lblList.size(); i++) {
                lblList.get(i).setText(inGamePlayerList.get(i).getName());
            }
        }
    }

    public void setTurn(String phase) {
        turnLabel.setText(phase);
    }

    public void showTurn(String currentPlayer) {
        // iterate over all players in the playerList
            for (InGamePlayer player: inGamePlayerList) {
                // check if playID from gamesocket equals to any of those players in list
                if (player.getId().equals(currentPlayer)) {
                    // check which label has that name and color it red
                    for (Label label: lblList) {
                        if (label.getText().equals(player.getName())) {
                            label.setStyle("-fx-text-fill: Red");
                            // color the rest in the default text color of the client
                        } else {
                            label.setStyle("-fx-text-fill: #FFFF8d");
                        }
                    }
                }
            }
    }
}
