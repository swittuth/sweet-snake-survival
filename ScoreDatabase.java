package com.programming.swittuth.snake_final_project;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

public class ScoreDatabase
{
    private Statement stmt;

    public ScoreDatabase() // initialize the database
    {
        try
        {
            String username = "scott";
            String password = "tiger";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/snakehighscore",
                    username, password);
            stmt = connection.createStatement();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void enterScore(String name, int score)
    {
        String sqlInstructions = "insert into Highscore values ('" + name + "'," + score + ")";
        try
        {
            stmt.execute(sqlInstructions);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public ArrayList<String> getTop10Names()
    {
        String sqlInstruction = "select name from Highscore order by score desc limit 10";
        ResultSet topNames;
        ArrayList<String> names = new ArrayList<>();
        try
        {
            topNames = stmt.executeQuery(sqlInstruction);
            while (topNames.next())
            {
                names.add(topNames.getString(1));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return names;
    }

    public ArrayList<Integer> getTop10Scores()
    {
        ArrayList<Integer> scores = new ArrayList<>();
        String sqlInstruction = "select score from Highscore order by score desc limit 10";
        ResultSet topNames;
        try
        {
            topNames = stmt.executeQuery(sqlInstruction);
            while (topNames.next())
            {
                scores.add(Integer.parseInt(topNames.getString(1)));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return scores;
    }


}
