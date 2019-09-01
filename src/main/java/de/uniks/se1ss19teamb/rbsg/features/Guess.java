package de.uniks.se1ss19teamb.rbsg.features;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Guess extends Application {
    private VBox vBox = new VBox();
    private HBox hBox = new HBox();
    private JFXTextField numberField = new JFXTextField();
    private int counter = 0;
    int number = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        numberField.setPromptText("Press Button");
        numberField.setAlignment(Pos.CENTER);
        numberField.setId("numberField");

        Button btnNewGame = new Button("Start New Game");
        btnNewGame.setOnAction(event -> {
            initalGame();
        });
        btnNewGame.setId("btnNewGame");

        Button btnStart = new Button("Start Guessing");
        btnStart.setOnAction(event -> {
            gameStart();
        });
        btnStart.setId("btnStart");

        vBox.getChildren().add(numberField);
        hBox.getChildren().add(btnNewGame);
        hBox.getChildren().add(btnStart);
        hBox.setSpacing(50);
        vBox.getChildren().add(hBox);

        primaryStage.setScene(new Scene(vBox, 300, 300));
        primaryStage.setResizable(false);
        primaryStage.setTitle("G U E S S   T H E   N U M B E R");
        primaryStage.show();

        btnNewGame.requestFocus();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void gameStart() {
        int yourNumber = Integer.parseInt(numberField.getText());

        if (counter == 0) {
            System.out.println("Game Over ! You lose!!!");
            numberField.setPromptText("Game Over ! You lose!!!");
            numberField.setText("");
        } else {
            if (yourNumber == number) {
                System.out.println("You win !");
                numberField.setPromptText("Your Number is correct , You win!");
                numberField.setText("");
            } else if (yourNumber > number) {
                System.out.println("Your number is to high");
                numberField.setPromptText("Try again you have, " + counter + "tries left");
                numberField.setText("");
                counter--;
            } else if (yourNumber < number) {
                System.out.println("Your number is to low");
                numberField.setPromptText("Try again you have, " + counter + "tries left");
                numberField.setText("");
                counter--;
            } else {
                System.out.println("Try again");
                numberField.setPromptText("Try again you have, " + counter + "tries left");
                numberField.setText("");
                counter--;
            }
        }
    }

    private void initalGame() {
        numberField.setPromptText("Enter your Number");
        counter = 3;

        number = getRandomNumber();
        System.out.println("The Number is : " + number);
    }

    private int getRandomNumber() {
        return (int) (Math.random() * 100) + 1;
    }
}
