package com.example.totito;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //For buttons and text
    private Button[][] buttons = new Button[3][3];

    private Button buttonReset;
    private boolean player1Turn = true;
    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    //For sounds
    private MediaPlayer playerWins;
    private MediaPlayer draw_p1p2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restGame();
            }
        });
    }

    @Override
    public void onClick(View view) {
        playerWins = MediaPlayer.create(MainActivity.this, R.raw.p_win);
        draw_p1p2 = MediaPlayer.create(MainActivity.this, R.raw.draw);

        if(!((Button) view).getText().toString().equals("")) //When there is nothing on it
        {
            return;
        }
        if(player1Turn)
        {
            ((Button) view).setText("X");
            view.setBackgroundResource(R.drawable.player1); //Image for player 1
        }
        else
        {
            ((Button) view).setText("O");
            view.setBackgroundResource(R.drawable.player2); //Image for player 2
        }
        roundCount++; //Count the rounds

        if(checkForWin()) //Check if it is a win or draw
        {
            if(player1Turn)
            {
                playerWins.start();
                player1Wins();
            }
            else
            {
                playerWins.start();
                player2Wins();
            }
        }
        else if(roundCount == 9)
        {
            draw_p1p2.start();
            draw();
        }
        else
        {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin()
    {
        String[][] field = new String[3][3];

        for(int i = 0; i < 3; i++) //For the values of each player
        {
            for(int j = 0; j < 3; j++)
            {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for(int i = 0; i < 3; i++) //For the columns
        {
            if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals(""))
            {
                return true;
            }
        }

        for(int i = 0; i < 3; i++) //For the rows
        {
            if(field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals(""))
            {
                return true;
            }
        }

        if(field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals(""))
        { //For diagonal
            return true;
        }

        if(field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals(""))
        { //For diagonal
            return true;
        }

        return false; //Game continues since no one won yet
    }

    private void player1Wins()
    {
        player1Points++;
        Toast.makeText(this, "Ice cream wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
//        resetBoard();
        //For the image to stay a little bit longer
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //do something
                resetBoard();
            }
        }, 1000);
    }

    private void player2Wins()
    {
        player2Points++;
        Toast.makeText(this, "Donut wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
//        resetBoard();
        //For the image to stay a little bit longer
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //do something
                resetBoard();
            }
        }, 1000);
    }

    private void draw()
    {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
//        resetBoard();
        //For the image to stay a little bit longer
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //do something
                resetBoard();
            }
        }, 1000);
    }

    private void updatePointsText()
    {
        textViewPlayer1.setText("Ice cream: " + player1Points);
        textViewPlayer2.setText("Donut: " + player2Points);
    }

    private void resetBoard()
    {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(buttonReset.getBackground());
            }
        }
        roundCount = 0;
        player1Turn = true;
    }

    private void restGame()
    {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}