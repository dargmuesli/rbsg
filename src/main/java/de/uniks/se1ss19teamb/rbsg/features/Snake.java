package de.uniks.se1ss19teamb.rbsg.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * A simple snake game.
 */
public class Snake extends Application {

    /**
     * The {@link Stage} to start the game in.
     */
    public static Stage classStage = new Stage();

    private static int speed = 0;
    private static int foodcolor = 0;
    private static int width = 20;
    private static int height = 20;
    private static int foodX = 0;
    private static int foodY = 0;
    private static int cornersize = 25;
    private static int score = 0;
    private static List<Coordinates> cornerCoordinateList = new ArrayList<>();
    private static Dir direction = Dir.left;
    private static boolean gameOver = false;
    private static Random rand = new Random();
    private long lastTick = 0;

    /**
     * The four directions left, right, up and down.
     */
    public enum Dir {
        left, right, up, down
    }

    /**
     * A simple x-y mapping.
     */
    public static class Coordinates {
        int x;
        int y;

        private Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * The game initializer.
     * Creates the {@link Canvas}, {@link AnimationTimer} and adds eventlisteners for keypresses.
     *
     * @param stage The stage to start the snake game in.
     */
    public void start(Stage stage) {
        try {
            lastTick = 0;
            VBox root = new VBox();
            Canvas c = new Canvas(width * cornersize, height * cornersize);
            final GraphicsContext gc = c.getGraphicsContext2D();
            root.getChildren().add(c);
            classStage = stage;
            classStage.setResizable(false);

            AnimationTimer animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        tick(gc);
                        return;
                    }

                    if (now - lastTick > 1000000000 / speed) {
                        lastTick = now;
                        tick(gc);
                    }
                }
            };

            if (gameOver) {
                animationTimer.stop();
            }

            animationTimer.start();
            newFood();

            Scene scene = new Scene(root, width * cornersize, height * cornersize);
            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (key.getCode() == KeyCode.W || key.getCode() == KeyCode.UP) {
                    direction = Dir.up;
                    if (speed == 1) {
                        speed = 5;
                    }
                } else if (key.getCode() == KeyCode.A || key.getCode() == KeyCode.LEFT) {
                    direction = Dir.left;
                    if (speed == 1) {
                        speed = 5;
                    }
                } else if (key.getCode() == KeyCode.S || key.getCode() == KeyCode.DOWN) {
                    direction = Dir.down;
                    if (speed == 1) {
                        speed = 5;
                    }
                } else if (key.getCode() == KeyCode.D || key.getCode() == KeyCode.RIGHT) {
                    direction = Dir.right;
                    if (speed == 1) {
                        speed = 5;
                    }
                }
            });

            cornerCoordinateList.add(new Coordinates(width / 2, height / 2));
            cornerCoordinateList.add(new Coordinates(width / 2, height / 2));
            cornerCoordinateList.add(new Coordinates(width / 2, height / 2));

            classStage.setScene(scene);
            classStage.setTitle("SNAKE");
            classStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void tick(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("", 50));
            gc.fillText("GAME OVER", 100, 250);
            return;
        }

        for (int i = cornerCoordinateList.size() - 1; i >= 1; i--) {
            cornerCoordinateList.get(i).x = cornerCoordinateList.get(i - 1).x;
            cornerCoordinateList.get(i).y = cornerCoordinateList.get(i - 1).y;
        }

        switch (direction) {
            case up:
                cornerCoordinateList.get(0).y--;
                if (cornerCoordinateList.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case down:
                cornerCoordinateList.get(0).y++;
                if (cornerCoordinateList.get(0).y >= height) {
                    gameOver = true;
                }
                break;
            case left:
                cornerCoordinateList.get(0).x--;
                if (cornerCoordinateList.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case right:
                cornerCoordinateList.get(0).x++;
                if (cornerCoordinateList.get(0).x >= width) {
                    gameOver = true;
                }
                break;
            default:
        }

        if (foodX == cornerCoordinateList.get(0).x && foodY == cornerCoordinateList.get(0).y) {
            cornerCoordinateList.add(new Coordinates(-1, -1));
            newFood();
        }

        for (int i = 1; i < cornerCoordinateList.size(); i++) {
            if (cornerCoordinateList.get(0).x == cornerCoordinateList.get(i).x
                && cornerCoordinateList.get(0).y == cornerCoordinateList.get(i).y) {
                gameOver = true;
            }
        }

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width * cornersize, height * cornersize);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("", 30));
        gc.fillText("Score: " + (score - 1), 10, 30);

        Color cc = Color.WHITE;

        switch (foodcolor) {
            case 0:
                cc = Color.PURPLE;
                break;
            case 1:
                cc = Color.LIGHTBLUE;
                break;
            case 2:
                cc = Color.YELLOW;
                break;
            case 3:
                cc = Color.PINK;
                break;
            case 4:
                cc = Color.ORANGE;
                break;
            default:
        }
        gc.setFill(cc);
        gc.fillOval(foodX * cornersize, foodY * cornersize, cornersize, cornersize);

        for (Coordinates c : cornerCoordinateList) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
            gc.setFill(Color.GREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 2, cornersize - 2);
        }

    }

    private static void newFood() {
        start: while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            for (Coordinates c : cornerCoordinateList) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            if (gameOver) {
                score = 0;
                speed = 0;
                cornerCoordinateList.clear();
                gameOver = false;
            }
            foodcolor = rand.nextInt(5);
            speed++;
            score++;
            break;

        }
    }
}