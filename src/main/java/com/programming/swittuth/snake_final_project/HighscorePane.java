package com.programming.swittuth.snake_final_project;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class HighscorePane extends StackPane
{
    private final ScoreDatabase scoreDatabase = new ScoreDatabase();
    VBox highScoreContainer;

    public HighscorePane()
    {
        displayHighScore();
    }


    public void displayHighScore()
    {
        setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0),
                Insets.EMPTY)));

        ArrayList<String> names = scoreDatabase.getTop10Names();
        ArrayList<Integer> scores = scoreDatabase.getTop10Scores();

        Label highScoreText = new Label("High Score");
        highScoreContainer = new VBox();
        highScoreContainer.getChildren().add(highScoreText);
        highScoreContainer.setSpacing(20);
        highScoreContainer.setAlignment(Pos.CENTER);

        HBox highScoreList = new HBox();
        highScoreList.setSpacing(50);
        highScoreList.setAlignment(Pos.CENTER);

        VBox playerName = new VBox();
        playerName.setAlignment(Pos.CENTER_LEFT);
        VBox playerScore = new VBox();
        playerScore.setAlignment(Pos.CENTER_RIGHT);

        new Thread(() -> {
            Platform.runLater(() ->
            {
                for (int i = 0; i < names.size() / 2; i++) {
                    playerName.getChildren().add(new Label(names.get(i)));
                    playerScore.getChildren().add(new Label(scores.get(i) + ""));
                }
            });
        }).start();

        new Thread(() ->
        {
            Platform.runLater(() ->
            {
                for (int i = (names.size() / 2) - 1; i < names.size(); i++)
                {
                    playerName.getChildren().add(new Label(names.get(i)));
                    playerScore.getChildren().add(new Label(scores.get(i) + ""));
                }
            });
        }).start();

        highScoreList.getChildren().addAll(playerName, playerScore);
        highScoreContainer.getChildren().add(highScoreList);
        getChildren().add(highScoreContainer);
    }
}