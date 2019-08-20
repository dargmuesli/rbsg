package de.uniks.se1ss19teamb.rbsg.features;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
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
    public static Stage classStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("P_O_N_G");
        Canvas canvas = new Canvas(WIDTH, HEIGTH);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        /*
        canvas.setOnKeyTyped(e -> {
            KeyCode keyCode = e.getCode();
            if (keyCode.equals(KeyCode.UP)) {
                System.out.println("Ja");
                setPlayerOneYPos(getPlayerOneYpos() + 100);
            }
            if (keyCode.equals(KeyCode.DOWN)) {
                System.out.println("Ja");
                setPlayerOneYPos(getPlayerOneYpos() - 100);
            }

        });

         */

        canvas.setOnMouseMoved(e -> playerOneYPos = e.getY() >= 500 ? e.getY() - PLAYER_HEIGTH : e.getY());
        canvas.setOnMouseClicked(e -> gameStarted = true);
        classStage = primaryStage;
        classStage.setScene(new Scene(new StackPane(canvas)));
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
            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("Click", WIDTH / 2, HEIGTH / 2);

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

    private double getPlayerOneYpos() {
        return playerOneYPos;
    }

    private void setPlayerOneYpos(double position) {
        this.playerOneYPos = position;
    }

}
