package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;

import de.uniks.se1ss19teamb.rbsg.util.Theming;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;



public class TicTacToeController {
    private static boolean playable = true;
    private ArrayList<JFXButton> buttons = new ArrayList<>();
    @FXML
    private AnchorPane apnRoot;
    @FXML
    private JFXButton one;
    @FXML
    private JFXButton two;
    @FXML
    private JFXButton three;
    @FXML
    private JFXButton four;
    @FXML
    private JFXButton five;
    @FXML
    private JFXButton six;
    @FXML
    private JFXButton seven;
    @FXML
    private JFXButton eight;
    @FXML
    private JFXButton nine;
    @FXML
    private JFXButton btnReplay;
    @FXML
    private Label label;

    public void initialize() {
        buttons.add(0, one);
        buttons.add(1, two);
        buttons.add(2, three);
        buttons.add(3, four);
        buttons.add(4, five);
        buttons.add(5, six);
        buttons.add(6, seven);
        buttons.add(7, eight);
        buttons.add(8, nine);

        Theming.setTheme(Arrays.asList(new Pane[]{apnRoot}));

        for (JFXButton button: buttons) {
            button.setStyle("-fx-font-size: 72.0;" + "-fx-border-radius: 0.0;" + "-fx-background-radius: 0.0;");
        }
        btnReplay.setStyle("-fx-font-size: 20.0");
        label.setStyle("-fx-font-size: 20.0");

    }

    private void getTurn() {
        if (playable) {
            for (JFXButton button: buttons) {
                button.setDisable(false);
            }
        } else {
            for (JFXButton button: buttons) {
                button.setDisable(true);
            }
        }
    }

    public void setOnAction(ActionEvent event) {
        String signX = "X";
        for (JFXButton button: buttons) {
            if (event.getSource().equals(button)) {
                button.setText(signX);
                setNextTurn();
                calculateWinner(signX, "Player wins!");
                button.setDisable(true);
            }
        }
        if (event.getSource().equals(btnReplay)) {
            playable = true;
            getTurn();
            for (JFXButton button: buttons) {
                button.setText("");
            }
            label.setText("");
        }
    }

    private void calculateWinner(String x, String winner) {
        if ((one.getText().equals(x) && two.getText().equals(x)) && (two.getText().equals(x)
            && three.getText().equals(x))) {
            label.setText(winner);
            playable = false;
            getTurn();
        } else if ((one.getText().equals(x) && five.getText().equals(x))
            && (five.getText().equals(x) && nine.getText().equals(x))) {
            label.setText(winner);
            playable = false;
            getTurn();
        } else if ((one.getText().equals(x) && four.getText().equals(x))
            && (four.getText().equals(x) && seven.getText().equals(x))) {
            label.setText(winner);
            playable = false;
            getTurn();
        } else if ((two.getText().equals(x) && five.getText().equals(x))
            && (five.getText().equals(x) && eight.getText().equals(x))) {
            label.setText(winner);
            playable = false;
            getTurn();
        } else if ((three.getText().equals(x) && six.getText().equals(x))
            && (six.getText().equals(x) && nine.getText().equals(x))) {
            label.setText(winner);
            playable = false;
            getTurn();
        } else if ((three.getText().equals(x) && five.getText().equals(x))
            && (five.getText().equals(x) && seven.getText().equals(x))) {
            label.setText(winner);
            playable = false;
            getTurn();
        } else if ((four.getText().equals(x) && five.getText().equals(x))
            && (five.getText().equals(x) && six.getText().equals(x))) {
            label.setText(winner);
            playable = false;
            getTurn();
        } else if ((seven.getText().equals(x) && eight.getText().equals(x))
            && (eight.getText().equals(x) && nine.getText().equals(x))) {
            label.setText(winner);
            playable = false;
            getTurn();
        }
    }

    private void setNextTurn() {
        int number;
        String signO = "O";
        number = (int) (Math.random() * 8);
        if (one.getText().equals("") || two.getText().equals("") || three.getText().equals("")
            || four.getText().equals("") || five.getText().equals("") || six.getText().equals("")
            || seven.getText().equals("") || eight.getText().equals("") || nine.getText().equals("")) {

            while (!buttons.get(number).getText().equals("")) {
                number = (int) (Math.random() * 8);
            }

            if (label.getText().equals("")) {
                buttons.get(number).setText(signO);
                buttons.get(number).setDisable(true);
            }

            calculateWinner(signO, "Computer Wins!");
        }
    }
}
