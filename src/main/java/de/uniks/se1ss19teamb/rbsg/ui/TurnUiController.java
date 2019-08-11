package de.uniks.se1ss19teamb.rbsg.ui;


import animatefx.animation.Wobble;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocketDistributor;
import java.util.ArrayList;
import java.util.Objects;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;


public class TurnUiController {

    public static String startShowTurn;
    public static String startTurnLabel;

    public ArrayList<InGamePlayer> inGamePlayerList = new ArrayList<>();

    @FXML
    public Label turnLabel;
    @FXML
    private Label labelOne;
    @FXML
    private Label labelTwo;
    @FXML
    private Label labelThree;
    @FXML
    private Label labelFour;
    @FXML
    private HBox lblBox;

    private ArrayList<Label> lblList = new ArrayList<>();

    private static TurnUiController instance;

    //    private GameSocket gameSocket = GameSocketDistributor.getGameSocket(0);
    //
    //    public void setGameSocket(int number) {
    //        gameSocket = GameSocketDistributor.getGameSocket(number);
    //    }

    public static TurnUiController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        instance = this;

        if (startShowTurn != null) {
            showTurn(startShowTurn);
            startShowTurn = null;
        }

        if (startTurnLabel != null) {
            setTurnLabel(startTurnLabel);
            startTurnLabel = null;
        }

        updatePlayers();
    }

    /**
     * Add all players to the player label list.
     */
    public void updatePlayers() {
        InGameController.inGameObjects.entrySet().stream()
            .filter(stringInGameObjectEntry -> stringInGameObjectEntry.getValue() instanceof InGamePlayer)
            .forEachOrdered(inGameObjectEntry -> inGamePlayerList.add(((InGamePlayer)inGameObjectEntry.getValue())));
        setLabelsVisible();
    }

    /**
     * Add labels according to the player list's size.
     */
    private void setLabelsVisible() {
        if (inGamePlayerList.size() != 2 && inGamePlayerList.size() != 4) {
            LogManager.getLogger().error("Did not expect player list size to be neither 2 nor 4!");
            return;
        }

        lblList.add(0, labelOne);
        lblList.add(1, labelTwo);

        if (inGamePlayerList.size() == 4) {
            lblList.add(2, labelThree);
            lblList.add(3, labelFour);
        } else if (inGamePlayerList.size() == 2) {
            lblBox.getChildren().remove(labelThree);
            lblBox.getChildren().remove(labelFour);
        }

        for (Label label : lblList) {
            label.setVisible(true);
        }

        for (int i = 0; i < lblList.size(); i++) {
            lblList.get(i).setText(inGamePlayerList.get(i).getName());
        }
    }

    @FXML
    private void nextPhase() {
        Objects.requireNonNull(GameSocketDistributor.getGameSocket(0)).nextPhase();
    }

    public void setTurnLabel(String turn) {
        Platform.runLater(() -> turnLabel.setText(turn));
    }

    public void showTurn(String currentPlayer) {
        // iterate over all players
        for (InGamePlayer player : inGamePlayerList) {
            // filter the current player
            if (player.getId().equals(currentPlayer)) {
                for (Label label : lblList) {
                    // color the player's label
                    if (label.getText().equals(player.getName())) {
                        new Wobble(label).play();
                        label.setStyle("-fx-text-fill: red");
                    } else {
                        label.setStyle("-fx-text-fill: #FFFF8d");
                    }
                }
            }
        }
    }
}
