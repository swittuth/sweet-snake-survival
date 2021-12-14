package com.programming.swittuth.snake_final_project;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.animation.Timeline;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.util.Random;
import java.util.ArrayList;

public class GamePanel extends Pane
{
    private boolean on = true;

    private Snake snake;

    private Circle snack;
    private ImageView fruitViewer;
    private boolean strawberry = false;
    private boolean lemon = false;
    private boolean orange = false;


    private ImageView heartViewer;
    private Label heartStatus;
    private HBox heartContainers;
    private ImageView poisonViewer;
    private final Random randomizer = new Random();
    private boolean up = true;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private final Label gameOverStatus = new Label("GAME OVER");

    HBox scoreBox;
    private int gameScore = 0;
    private final Label scoreStatus = new Label("Points: " + gameScore);
    private final ScoreDatabase scoreDatabase = new ScoreDatabase();


    private boolean changedDirection = true;

    // animation
    private final Timeline tm;
    private final Timeline poisonTimeline;
    private final Timeline fruitTimeline;

    public GamePanel(double boardWidth, double boardHeight)
    {
        setWidth(boardWidth);
        setHeight(boardHeight);

        // to aid visualization of the board game
        Line borderLine = new Line(0, 20, getWidth(), 20);
        getChildren().add(borderLine);

        final double SNAKE_SIZE = 20;
        spawnSnake(boardWidth, boardHeight, SNAKE_SIZE);
        spawnSnack();
        spawnFruits();
        spawnScore();
        spawnHeart();
        spawnPoison();
        setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0),
                Insets.EMPTY)));

        tm = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> moveSnake()));
        tm.setCycleCount(Timeline.INDEFINITE);

        fruitTimeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> spawnFruits()));
        fruitTimeline.setCycleCount(Timeline.INDEFINITE);

        poisonTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> spawnPoison()));
        poisonTimeline.setCycleCount(Timeline.INDEFINITE);

        setOnKeyPressed(e -> {
            if (changedDirection)
            {
                changedDirection = false;
                if (e.getCode() == KeyCode.UP)
                {
                    if (!down)
                    {
                        up = true;
                        left = false;
                        right = false;
                        down = false;
                    }
                }
                else if (e.getCode() == KeyCode.LEFT)
                {
                    if (!right)
                    {
                        up = false;
                        left = true;
                        right = false;
                        down = false;
                    }
                }
                else if (e.getCode() == KeyCode.RIGHT)
                {
                    if (!left)
                    {
                        up = false;
                        left = false;
                        right = true;
                        down = false;
                    }
                }
                else if (e.getCode() == KeyCode.DOWN)
                {
                    if (!up)
                    {
                        up = false;
                        left = false;
                        right = false;
                        down = true;
                    }
                }
            }
        });

        fruitTimeline.play();
        poisonTimeline.play();
        tm.play();
    }

    public boolean isOn()
    {
        return on;
    }

    public void eat()
    {
        if (snake.getShapePositions().get(0).intersects(snack.getLayoutBounds()))
        {
            snake.increaseLength();
            int length = snake.getShapePositions().size();
            getChildren().add(snake.getShapePositions().get(length - 1));
            clearSnack();
            spawnSnack();
            digestSnack();
        }
        else if (snake.getShapePositions().get(0).intersects(fruitViewer.getLayoutBounds().getMaxX(),
                fruitViewer.getLayoutBounds().getMaxY(),
                -10, -10))
        {
            snake.increaseLength();
            int length = snake.getShapePositions().size();
            getChildren().add(snake.getShapePositions().get(length - 1));
            clearFruit();
            spawnFruits();
            digestFruit();
        }
        else if (snake.getShapePositions().get(0).intersects(poisonViewer.getLayoutBounds().getMaxX(),
                poisonViewer.getLayoutBounds().getMaxY(),
                -10, -10))
        {
            clearPoison();
            snake.decreaseLife();
            updateLifeCounter();
            tm.setRate(tm.getRate() - 0.08);
        }

    }

    public void digestSnack()
    {
        gameScore += 1;
        tm.setRate(tm.getRate() + 0.01);
        updateScore();
    }

    public void digestFruit()
    {
        if (strawberry)
        {
            gameScore += 10;
            tm.setRate(tm.getRate() + 0.1);
        }
        else if (orange)
        {
            gameScore += 5;
            tm.setRate(tm.getRate() + 0.05);
        }
        else if (lemon)
        {
            gameScore += 2;
            tm.setRate(tm.getRate() + 0.02);
        }

        updateScore();
    }

    public void updateScore()
    {
        scoreStatus.setText("Points: " + gameScore);
    }

    public void moveSnake()
    {
        if (up)
        {
            gameOver();
            snake.moveUp();
            snakeOnBorder();
            eat();
        }
        else if (left)
        {
            snake.moveLeft();
            snakeOnBorder();
            eat();
        }
        else if (right)
        {
            snake.moveRight();
            snakeOnBorder();
            eat();
        }
        else if (down)
        {
            snake.moveDown();
            snakeOnBorder();
            eat();
        }

        if (!snake.isAlive())
        {
            gameOver();
        }

        if (!changedDirection)
        {
            changedDirection = true;
        }
    }

    public void gameOver()
    {
        tm.stop();
        poisonTimeline.stop();
        fruitTimeline.stop();
        gameOverStatus.setTextFill(Color.DARKRED);
        gameOverStatus.setFont(Font.font("Alice",
                FontWeight.EXTRA_BOLD, FontPosture.ITALIC,30));
        gameOverStatus.setLayoutX(getWidth() / 4);
        gameOverStatus.setLayoutY(getHeight() / 2);
        getChildren().add(gameOverStatus);
        displayInputName();
        displayNewHighScore();

    }

    public void displayInputName()
    {
        VBox nameInputContainer = new VBox();
        Button submitButton = new Button("Submit");
        TextField nameInputField = new TextField();
        nameInputContainer.getChildren().add(new Label("Please enter your name"));
        nameInputContainer.getChildren().add(nameInputField);
        nameInputContainer.getChildren().add(submitButton);
        nameInputContainer.setLayoutX(getWidth() / 3);
        nameInputContainer.setLayoutY(getHeight() / 2);
        getChildren().add(nameInputContainer);
        nameInputContainer.setAlignment(Pos.CENTER);

        submitButton.setOnAction(e -> {
            scoreDatabase.enterScore(nameInputField.getText(), gameScore);
            on = false;
        });

    }

    public void displayNewHighScore()
    {
        ArrayList<Integer> scores = scoreDatabase.getTop10Scores();
        if (gameScore > scores.get(0))
        {
            Label labelHighScore = new Label("NEW HIGHSCORE: " + gameScore);
            labelHighScore.setLayoutX(getWidth() / 2);
            labelHighScore.setLayoutY(20);
            getChildren().add(labelHighScore);

            new Thread(() -> {
                try
                {
                    String text = "";
                    while (true)
                    {
                        if (labelHighScore.getText().trim().length() == 0)
                        {
                            text = "NEW HIGHSCORE " + gameScore;
                        }
                        else
                        {
                            text = "";
                        }

                        String finalText = text;
                        Platform.runLater(() ->
                        {
                            labelHighScore.setText(finalText);
                        });

                        Thread.sleep(200);
                    }
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }).start();
        }
    }

    public void snakeOnBorder()
    {
        Rectangle head = snake.getShapePositions().get(0);
        double size = head.getWidth();


        if (head.getY() <= (size / 2))
        {
            head.setY(getHeight() - size);
        }
        else if (head.getY() >= getHeight())
        {
            head.setY(size);
        }
        else if (head.getX() <= -size)
        {
            head.setX(getWidth() - size);
        }
        else if (head.getX() >= getWidth())
        {
            head.setX(0);
        }
    }

    public void spawnSnake(double boardWidth, double boardHeight, double snakeSize)
    {
        snake = new Snake(boardWidth / 2, boardHeight / 2, snakeSize);
        ArrayList<Rectangle> shapePositions = snake.getShapePositions();

        for (Rectangle shapePosition : shapePositions)
        {
            getChildren().add(shapePosition);
        }
    }

    public void spawnSnack()
    {
        Color snackColor = Color.color(Math.random(), Math.random(), Math.random());
        Color snackOutline = snackColor.brighter();

        double xPosition = randomizer.nextInt(0, (int)(getWidth() / 10)) * 10;
        double yPosition = randomizer.nextInt(0, (int)(getHeight() / 10)) * 10;

        // to avoid being on line
        xPosition = ((int)(xPosition / 10) % 2 == 0 ? xPosition + 10 : xPosition);
        yPosition = ((int)(yPosition / 10) % 2 == 0 ? yPosition + 10 : yPosition);

        // to avoid being in the status section of the game
        if (yPosition < 20)
        {
            yPosition += 20;
        }

        double SNACK_SIZE = 5;
        snack = new Circle(SNACK_SIZE);
        snack.setFill(snackColor);
        snack.setStroke(snackOutline);

        getChildren().add(snack);

        snack.setCenterX(xPosition);
        snack.setCenterY(yPosition);
    }

    public void spawnPoison()
    {
        clearPoison();
        poisonViewer = new ImageView(new Image(getClass().getResource("poison.png").toExternalForm()));

        double POISON_SIZE = 20;
        poisonViewer.setFitWidth(POISON_SIZE);
        poisonViewer.setFitHeight(POISON_SIZE);
        poisonViewer.setSmooth(true);

        double xPosition = randomizer.nextInt(0, (int)(getWidth() / 10)) * 10;
        double yPosition = randomizer.nextInt(0, (int)(getHeight() / 10)) * 10;

        // to avoid being on line
        xPosition = ((int)(xPosition / 10) % 2 != 0 ? xPosition + 10 : xPosition);
        yPosition = (((int)(yPosition / 10) % 2 != 0) || (yPosition < 20) ? yPosition + 10 : yPosition);

        // to avoid being outside of the line
        if (xPosition >= (getWidth()-1))
        {
            xPosition -= 20;
        }
        if (yPosition >= (getHeight()-1))
        {
            yPosition -= 20;
        }

        poisonViewer.setX(xPosition);
        poisonViewer.setY(yPosition);

        getChildren().add(poisonViewer);
    }

    public void spawnFruits()
    {
        clearFruit();
        Image fruit;
        Double randomNumber = randomizer.nextDouble(0, 1);

        if (randomNumber >= 0.80)
        {
            fruit =  new Image(getClass().getResource("strawberry.png").toExternalForm());
            strawberry = true;
            lemon = false;
            orange = false;
        }
        else if (randomNumber >= 0.40)
        {
            fruit = new Image(getClass().getResource("orange.png").toExternalForm());
            strawberry = false;
            lemon = false;
            orange = true;
        }
        else
        {
            fruit = new Image(getClass().getResource("lemon.png").toExternalForm());
            strawberry = false;
            lemon = true;
            orange = false;
        }


        fruitViewer = new ImageView(fruit);

        double FRUIT_SIZE = 20;
        fruitViewer.setFitWidth(FRUIT_SIZE);
        fruitViewer.setFitHeight(FRUIT_SIZE);
        fruitViewer.setSmooth(true);

        double xPosition = randomizer.nextInt(0, (int)(getWidth() / 10)) * 10;
        double yPosition = randomizer.nextInt(0, (int)(getHeight() / 10)) * 10;

        // to avoid being on line
        xPosition = ((int)(xPosition / 10) % 2 != 0 ? xPosition + 10 : xPosition);
        yPosition = (((int)(yPosition / 10) % 2 != 0) || (yPosition < 20) ? yPosition + 10 : yPosition);

        // to avoid being outside of the line
        if (xPosition >= (getWidth()-1))
        {
            xPosition -= 20;
        }
        if (yPosition >= (getHeight()-1))
        {
            yPosition -= 20;
        }

        fruitViewer.setX(xPosition);
        fruitViewer.setY(yPosition);

        getChildren().add(fruitViewer);
    }

    public void spawnScore()
    {
        scoreBox = new HBox();
        Font font = Font.font("Alice", FontWeight.BLACK, FontPosture.REGULAR, 15);
        scoreStatus.setFont(font);
        scoreStatus.setTextFill(Color.BLACK);
        scoreBox.getChildren().add(scoreStatus);
        scoreBox.setLayoutX(0);
        scoreBox.setLayoutY(0);
        scoreBox.setAlignment(Pos.CENTER_LEFT);
        getChildren().add(scoreBox);
    }

    public void spawnHeart()
    {
        heartContainers = new HBox();
        heartViewer = new ImageView(new Image(getClass().getResource("heart.png").toExternalForm()));
        heartViewer.setFitHeight(20);
        heartViewer.setFitWidth(20);
        heartStatus = new Label("X" + snake.getLifeCounter());
        heartContainers.getChildren().addAll(heartViewer, heartStatus);
        heartContainers.setAlignment(Pos.CENTER_RIGHT);
        heartContainers.setLayoutY(0);
        heartContainers.setLayoutX(getWidth()- 40);
        getChildren().add(heartContainers);
    }

    public void updateLifeCounter()
    {
        heartStatus.setText("X" + snake.getLifeCounter());
    }

    public void clearSnack()
    {
        this.getChildren().remove(snack);
    }

    public void clearFruit()
    {
        this.getChildren().remove(fruitViewer);
    }

    public void clearPoison()
    {
        getChildren().remove(poisonViewer);
    }

}