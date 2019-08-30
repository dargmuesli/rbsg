package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocketDistributor;
import de.uniks.se1ss19teamb.rbsg.util.UserInterfaceUtils;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;



public class WinScreenController {
    @FXML
    private Label winnerLabel;
    @FXML
    private JFXButton backBtn;

    public ArrayList<InGamePlayer> inGamePlayerList = new ArrayList<>();

    private GameSocket gameSocket = GameSocketDistributor.getGameSocket(0);

    private static WinScreenController instance;

    public static WinScreenController getInstance() {
        return instance;
    }

    public void initialize() {
        instance = this;
        updatePlayers();
    }

    public void setGameSocket(int number) {
        gameSocket = GameSocketDistributor.getGameSocket(number);
    }

    /**
     * Fills the list of ingame players by filtering the list of ingame objects for players.
     */
    public void updatePlayers() {
        // fills inGamePlayerList with InGamePlayers
        InGameController.inGameObjects.entrySet().stream()
            .filter(stringInGameObjectEntry -> stringInGameObjectEntry.getValue() instanceof InGamePlayer)
            .forEachOrdered(inGameObjectEntry -> inGamePlayerList.add((InGamePlayer)inGameObjectEntry.getValue()));
    }

    /**
     * Selects the winner from all ingame players.
     *
     * @param winner The winner's id.
     */
    public void calculateWinner(String winner) {
        for (InGamePlayer player: inGamePlayerList) {
            if (player.getId().equals(winner)) {
                winnerLabel.setText(player.getName());
            }
        }
    }

    /**
     * Displays the winscreen.
     *
     * @param winner The winner's id.
     */
    public void setWinningScreen(String winner) {
        Platform.runLater(() -> {
            InGameController.instance.vBoxWinScreen.setVisible(true);
            calculateWinner(winner);
        });
    }

    @FXML
    private void setOnAction(ActionEvent event) {
        if (event.getSource().equals(backBtn)) {
            gameSocket.leaveGame();
            gameSocket.disconnect();
            UserInterfaceUtils.makeFadeOutTransition(
                "/de/uniks/se1ss19teamb/rbsg/fxmls/main.fxml", InGameController.getInstance().winScreenPane);
        }
    }

}
