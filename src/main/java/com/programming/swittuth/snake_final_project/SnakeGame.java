package com.programming.swittuth.snake_final_project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SnakeGame extends Application
{

    @Override
    public void start(Stage primaryStage)
    {
        MainPane mainPane = new MainPane();
        Scene scene = new Scene(mainPane, 600, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Game");
        primaryStage.show();

        mainPane.gamePane.requestFocus();
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
