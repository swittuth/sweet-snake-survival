package com.programming.swittuth.snake_final_project;

import javafx.animation.KeyFrame;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
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

public class GamePane extends Pane
{
    // imagine the pane to be 20 x 20
    private Snake snake;
    private Circle snack;
    private final Random randomizer = new Random();
    private boolean up = true;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private final Timeline tm;
    private final Label gameOverStatus = new Label("GAME OVER");
    private int gameScore = 0;
    private final Label scoreStatus = new Label(gameScore + "");


    public GamePane()
    {

        // to aid visualization of the board game
        /*
        for (int i = 20; i <= 400; i += 20 )
        {
            getChildren().add(new Line(i, 0, i, 400));
        }

        for (int i = 20; i <= 400; i+= 20)
        {
            getChildren().add(new Line(0, i, 400, i));
        }
         */

        double SNAKE_SIZE = 20;
        spawnSnake(400, 400, SNAKE_SIZE);
        spawnSnack();
        spawnScore();
        setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0),
                Insets.EMPTY)));

        tm = new Timeline(new KeyFrame(Duration.seconds(0.2), e -> moveSnake()));
        tm.setCycleCount(Timeline.INDEFINITE);

        setOnKeyPressed(e -> {
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

        });

        tm.play();

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
            double INCREASE_RATE_GAME = 0.05;
            tm.setRate(tm.getRate() + INCREASE_RATE_GAME);
            increaseScore();
        }
    }

    public void increaseScore()
    {
        gameScore += 5;
        scoreStatus.setText(gameScore + "");
    }

    public void moveSnake()
    {
        if (up)
        {
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
    }

    public void gameOver()
    {
        tm.stop();
        gameOverStatus.setTextFill(Color.DARKRED);
        gameOverStatus.setFont(Font.font("Alice",
                FontWeight.EXTRA_BOLD, FontPosture.ITALIC,30));
        gameOverStatus.setLayoutX(getWidth() / 4);
        gameOverStatus.setLayoutY(getHeight() / 2);
        getChildren().add(gameOverStatus);
    }

    public void snakeOnBorder()
    {
        Rectangle head = snake.getShapePositions().get(0);
        double size = head.getWidth();


        if (head.getY() <= -size)
        {
            head.setY(getHeight() - size);
        }
        else if (head.getY() >= getHeight())
        {
            head.setY(-size);
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

        double xPosition = randomizer.nextInt(0, 40) * 10;
        double yPosition = randomizer.nextInt(0, 40) * 10;

        // to avoid being on line
        xPosition = ((int)(xPosition / 10) % 2 == 0 ? xPosition + 10 : xPosition);
        yPosition = ((int)(yPosition / 10) % 2 == 0 ? yPosition + 10 : yPosition);

        double SNACK_SIZE = 5;
        snack = new Circle(SNACK_SIZE);
        snack.setFill(snackColor);
        snack.setStroke(snackOutline);

        getChildren().add(snack);

        snack.setCenterX(xPosition);
        snack.setCenterY(yPosition);
    }

    public void clearSnack()
    {
        this.getChildren().remove(snack);
    }

    public void spawnScore()
    {
        Font font = Font.font("Alice", FontWeight.BLACK, FontPosture.REGULAR, 20);
        scoreStatus.setFont(font);
        scoreStatus.setTextFill(Color.LIGHTGOLDENRODYELLOW);
        getChildren().add(scoreStatus);
    }

}
