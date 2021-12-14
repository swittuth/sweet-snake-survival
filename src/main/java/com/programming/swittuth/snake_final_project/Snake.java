package com.programming.swittuth.snake_final_project;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

// Define property of the snake
public class Snake
{
    private final ArrayList<Rectangle> shapePositions = new ArrayList<>();
    private boolean alive = true;
    private int lifeCounter = 3;

    public Snake()
    {
        // body left empty
    }

    // takes in starting position
    public Snake(double xPosition, double yPosition, double size)
    {
        Rectangle bodyPart;

        Rectangle head = new Rectangle(size, size);
        head.setFill(Color.GREEN);
        //head.setStroke(Color.DARKRED);
        head.setX(xPosition);
        head.setY(yPosition);
        shapePositions.add(head);

        int INITIAL_LENGTH = 3;
        for (int i = 1; i < INITIAL_LENGTH; i++)
        {
            bodyPart = new Rectangle(size, size);
            bodyPart.setFill(Color.LEMONCHIFFON);
            //bodyPart.setStroke(Color.RED);
            bodyPart.setX(xPosition);
            bodyPart.setY(shapePositions.get(i - 1).getY() + size);
            shapePositions.add(bodyPart);
        }

    }

    // functions
    public void moveLeft() // move left and then the move forward function is to register all the other movements
    {
        Rectangle head = shapePositions.get(0);

        double size = head.getWidth();
        registerMoves();
        head.setX(head.getX() - size);
        bitesItself();
    }

    public void moveRight()
    {
        Rectangle head = shapePositions.get(0);

        double size = head.getWidth();
        registerMoves();
        head.setX(head.getX() + size);
        bitesItself();

    }

    public void moveUp()
    {
        Rectangle head = shapePositions.get(0);

        double size = head.getWidth();
        registerMoves();
        head.setY(head.getY() - size);
        bitesItself();
    }

    public void moveDown()
    {
        Rectangle head = shapePositions.get(0);

        double size = head.getWidth();
        registerMoves();
        head.setY(head.getY() + size);
        bitesItself();

    }

    public void registerMoves()
    {
        for (int i = shapePositions.size() - 1; i > 0 ; i--) // start from second to last
        {
            shapePositions.get(i).setX(shapePositions.get(i - 1).getX());
            shapePositions.get(i).setY(shapePositions.get(i - 1).getY());
        }
    }

    public void bitesItself()
    {
        Rectangle head = shapePositions.get(0);

        for (int i = 1; i < shapePositions.size() - 1; i++)
        {
            if (head.getLayoutBounds().equals(shapePositions.get(i).getLayoutBounds()))
            {
                alive = false;
            }
        }
    }

    public void decreaseLife()
    {
        lifeCounter -= 1;

        if (lifeCounter <= 0)
        {
            alive = false;
        }
    }

    public int getLifeCounter()
    {
        return lifeCounter;
    }

    public void increaseLength()
    {
        Rectangle newBodyPart;
        Rectangle lastPart = shapePositions.get(shapePositions.size() - 1);
        Rectangle secondLastPart = shapePositions.get(shapePositions.size() - 2);

        double xDiff = lastPart.getX() - secondLastPart.getX();
        double yDiff = lastPart.getY() - secondLastPart.getY();

        // because width = height with square
        double size = lastPart.getWidth();
        Paint bodyColor = lastPart.getFill();
        //Paint skinColor = lastPart.getStroke();

        // means to add part to the bottom because snake tail is south
        if (yDiff > 0)
        {
            newBodyPart = new Rectangle(size, size);
            newBodyPart.setX(lastPart.getX());
            newBodyPart.setY(lastPart.getY() + size);
            newBodyPart.setFill(bodyColor);
            //newBodyPart.setStroke(skinColor);
            shapePositions.add(newBodyPart);
        }
        else if (yDiff < 0)
        {
            newBodyPart = new Rectangle(size, size);
            newBodyPart.setX(lastPart.getX());
            newBodyPart.setY(lastPart.getY() - size);
            newBodyPart.setFill(bodyColor);
            //newBodyPart.setStroke(skinColor);
            shapePositions.add(newBodyPart);
        }
        else if (xDiff < 0) // meaning that lake is facing towards the right and tail is on the left
        {
            newBodyPart = new Rectangle(size, size);
            newBodyPart.setX(lastPart.getX() - size);
            newBodyPart.setY(lastPart.getY());
            newBodyPart.setFill(bodyColor);
            //newBodyPart.setStroke(skinColor);
            shapePositions.add(newBodyPart);
        }
        else if (xDiff > 0)
        {
            newBodyPart = new Rectangle(size, size);
            newBodyPart.setX(lastPart.getX() + size);
            newBodyPart.setY(lastPart.getY());
            newBodyPart.setFill(bodyColor);
            //newBodyPart.setStroke(skinColor);
            shapePositions.add(newBodyPart);
        }
    }

    // getters
    public ArrayList<Rectangle> getShapePositions()
    {
        return shapePositions;
    }

    public boolean isAlive()
    {
        return alive;
    }

}
