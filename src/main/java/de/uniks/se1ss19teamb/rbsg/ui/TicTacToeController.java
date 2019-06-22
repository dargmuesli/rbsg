package de.uniks.se1ss19teamb.rbsg.ui;

import com.jfoenix.controls.JFXButton;

import de.uniks.se1ss19teamb.rbsg.util.SerializeUtils;

import java.io.File;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;



public class TicTacToeController {
    @FXML
    private AnchorPane tictactoeScreen;
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
    private String signX = "X";
    private String signO = "O";
    private static boolean turn = true;
    @FXML
    private Label label;
    private static boolean playable = true;
    private String path = "./src/main/resources/de/uniks/se1ss19teamb/rbsg/cssMode.json";
    ArrayList<JFXButton> buttons = new ArrayList<>();
    public int number;


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

        if (SerializeUtils.deserialize(new File(path), boolean.class)) {
            tictactoeScreen.getStylesheets().clear();
            tictactoeScreen.getStylesheets().add("/de/uniks/se1ss19teamb/rbsg/css/tictactoedark.css");
        } else {
            tictactoeScreen.getStylesheets().clear();
            tictactoeScreen.getStylesheets().add("/de/uniks/se1ss19teamb/rbsg/css/tictactoewhite.css");
        }
        one.setStyle("-fx-font-size: 72.0");
        two.setStyle("-fx-font-size: 72.0");
        three.setStyle("-fx-font-size: 72.0");
        four.setStyle("-fx-font-size: 72.0");
        five.setStyle("-fx-font-size: 72.0");
        six.setStyle("-fx-font-size: 72.0");
        seven.setStyle("-fx-font-size: 72.0");
        eight.setStyle("-fx-font-size: 72.0");
        nine.setStyle("-fx-font-size: 72.0");
        btnReplay.setStyle("-fx-font-size: 20.0");
        label.setStyle("-fx-font-size: 20.0");

    }

    public void getTurn() {
        if (playable) {
            one.setDisable(false);
            two.setDisable(false);
            three.setDisable(false);
            four.setDisable(false);
            five.setDisable(false);
            six.setDisable(false);
            seven.setDisable(false);
            eight.setDisable(false);
            nine.setDisable(false);
        } else {
            one.setDisable(true);
            two.setDisable(true);
            three.setDisable(true);
            four.setDisable(true);
            five.setDisable(true);
            six.setDisable(true);
            seven.setDisable(true);
            eight.setDisable(true);
            nine.setDisable(true);
        }
    }

    public void setOnAction(ActionEvent event) {
        if (event.getSource().equals(one)) {
            one.setText(signX);
            setNextTurn();
            calculateWinner(signX, "Player wins!");
            one.setDisable(true);
        } else if (event.getSource().equals(two)) {
            two.setText(signX);
            setNextTurn();
            calculateWinner(signX, "Player wins!");
            two.setDisable(true);
        } else if (event.getSource().equals(three)) {
            three.setText(signX);
            setNextTurn();
            calculateWinner(signX, "Player wins!");
            three.setDisable(true);
        } else if (event.getSource().equals(four)) {
            four.setText(signX);
            setNextTurn();
            calculateWinner(signX, "Player wins!");
            four.setDisable(true);
        } else if (event.getSource().equals(five)) {
            five.setText(signX);
            setNextTurn();
            calculateWinner(signX, "Player wins!");
            five.setDisable(true);
        } else if (event.getSource().equals(six)) {
            six.setText(signX);
            setNextTurn();
            calculateWinner(signX, "Player wins!");
            six.setDisable(true);
        } else if (event.getSource().equals(seven)) {
            seven.setText(signX);
            setNextTurn();
            calculateWinner(signX, "Player wins!");
            seven.setDisable(true);
        } else if (event.getSource().equals(eight)) {
            eight.setText(signX);
            setNextTurn();
            calculateWinner(signX, "Player wins!");
        } else if (event.getSource().equals(nine)) {
            nine.setText(signX);
            setNextTurn();
            calculateWinner(signX, "Player wins!");
            nine.setDisable(true);
        } else if (event.getSource().equals(btnReplay)) {
            playable = true;
            turn = true;
            getTurn();
            one.setText("");
            two.setText("");
            three.setText("");
            four.setText("");
            five.setText("");
            six.setText("");
            seven.setText("");
            eight.setText("");
            nine.setText("");
            label.setText("");
        }
    }

    public void calculateWinner(String x, String winner) {
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

    public void setNextTurn() {
        turn = false;
        number = (int) (Math.random() * 8) + 0;
        if (one.getText().equals("") || two.getText().equals("") || three.getText().equals("")
            || four.getText().equals("") || five.getText().equals("") || six.getText().equals("")
            || seven.getText().equals("") || eight.getText().equals("") || nine.getText().equals("")) {
            while (!buttons.get(number).getText().equals("")) {
                number = (int) (Math.random() * 8) + 0;
            }
            if (label.getText().equals("")) {
                buttons.get(number).setText(signO);
                buttons.get(number).setDisable(true);
            }
            calculateWinner(signO, "Computer Wins!");
            turn = true;
        }
    }
}
