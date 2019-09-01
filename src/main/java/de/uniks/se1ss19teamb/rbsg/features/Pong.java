package de.uniks.se1ss19teamb.rbsg.features;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Pong extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGTH = 600;
    private static final int PLAYER_HEIGTH = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_R = 15;
    private int ballYSpeed = 1;
    private int ballXSpeed = 1;
    private double playerOneYPos = HEIGTH / 2;
    private int playerOneXPos = 0;
    private double playerTwoYPos = HEIGTH / 2;
    private double playerTwoXPos = WIDTH - PLAYER_WIDTH;
    private double ballXPos = WIDTH / 2;
    private double ballYPos = HEIGTH / 2;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private boolean gameStarted;
    private VBox mainScreen = new VBox();
    private Button onePlayer = new Button("1 - Player");

    public static Stage classStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("P_O_N_G");

        Canvas canvas = new Canvas(WIDTH, HEIGTH);
        canvas.setId("canvas");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);

        onePlayer.setStyle(style());
        onePlayer.setId("onePlayer");

        mainScreen.getChildren().add(onePlayer);
        mainScreen.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        mainScreen.setAlignment(Pos.CENTER);
        mainScreen.setSpacing(50);

        onePlayer.setOnMouseClicked(event ->  classStage.setScene(new Scene(new StackPane(canvas))));

        canvas.setOnMouseMoved(event -> playerOneYPos = event.getY() >= 500
            ? event.getY() - PLAYER_HEIGTH : event.getY());
        canvas.setOnMouseClicked(event -> gameStarted = true);

        classStage = primaryStage;
        classStage.setScene(new Scene(mainScreen, 800, 600));
        classStage.setResizable(false);
        classStage.show();
        timeline.play();
    }

    private void run(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, WIDTH, HEIGTH);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));

        if (gameStarted) {
            ballXPos += ballXSpeed;
            ballYPos += ballYSpeed;

            if (ballXPos < WIDTH - WIDTH / 4) {
                playerTwoYPos = ballYPos - PLAYER_HEIGTH / 2;
            } else {
                playerTwoYPos = ballYPos > playerTwoYPos + PLAYER_HEIGTH / 2 ? playerTwoYPos += 1 : playerTwoYPos - 1;
            }
            gc.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);
        } else {
            if (scoreP1 == 0 && scoreP2 == 0) {
                gc.setStroke(Color.WHITE);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.strokeText("Click to Play",WIDTH / 2, HEIGTH / 2);
            } else {
                gc.setStroke(Color.WHITE);
                gc.setTextAlign(TextAlignment.CENTER);
                String point1 = String.valueOf(scoreP1);
                String point2 = String.valueOf(scoreP2);

                gc.strokeText(point1 + " - " + point2,WIDTH / 2, HEIGTH / 2);
            }

            ballXPos = WIDTH / 2;
            ballYPos = HEIGTH / 2;

            ballXSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
            ballYSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
        }

        if (ballYPos > HEIGTH || ballYPos < 0) {
            ballYSpeed *= -1;
        }

        if (ballXPos < playerOneXPos - PLAYER_WIDTH) {
            scoreP2++;
            gameStarted = false;
        }

        if (ballXPos > playerTwoXPos + PLAYER_WIDTH) {
            scoreP1++;
            gameStarted = false;
        }

        if ((ballXPos + BALL_R > playerTwoXPos) && ballYPos >= playerTwoYPos && ballYPos
            <= playerTwoYPos + PLAYER_HEIGTH || ((ballXPos < playerOneXPos + PLAYER_WIDTH)
            && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + PLAYER_HEIGTH)) {
            ballYSpeed += 1 * Math.signum(ballYSpeed);
            ballXSpeed += 1 * Math.signum(ballXSpeed);
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }

        gc.fillText(scoreP1 + "\t\t\t\t\t\t\t\t\t\t\t" + scoreP2,WIDTH / 2,100);
        gc.fillRect(playerOneXPos, playerOneYPos, PLAYER_WIDTH, PLAYER_HEIGTH);
        gc.fillRect(playerTwoXPos, playerTwoYPos, PLAYER_WIDTH, PLAYER_HEIGTH);
    }


    private String style() {
        return "-fx-padding: 8 15 15 15;\n"
            + "    -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;\n"
            + "    -fx-background-radius: 8;\n"
            + "    -fx-background-color: \n"
            + "        linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%),\n"
            + "        #9d4024,\n"
            + "        #d86e3a,\n"
            + "        radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);\n"
            + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );\n"
            + "    -fx-font-weight: bold;\n"
            + "    -fx-font-size: 1.1em;";
    }

    private double getPlayerOneYpos() {
        return playerOneYPos;
    }

    private void setPlayerOneYpos(double position) {
        this.playerOneYPos = position;
    }
}
