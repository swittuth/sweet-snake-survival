package com.programming.swittuth.snake_final_project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SnakeGame extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        GamePane gamePane = new GamePane();
        StackPane stackPane = new StackPane();

        Scene scene = new Scene(stackPane, 400, 400);

        stackPane.getChildren().add(gamePane);

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        gamePane.requestFocus();
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
