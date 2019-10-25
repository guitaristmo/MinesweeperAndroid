package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    Button smallGameButton;
    Button mediumGameButton;
    Button largeGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupButtons();
    }

    private void setupButtons()
    {
        smallGameButton = findViewById(R.id.small_board_button);
        mediumGameButton = findViewById(R.id.medium_board_button);
        largeGameButton = findViewById(R.id.large_board_button);
        smallGameButton.setOnClickListener(new ButtonClickListener(8, 8, 10));
        mediumGameButton.setOnClickListener(new ButtonClickListener(16, 16, 20));
        largeGameButton.setOnClickListener(new ButtonClickListener(20, 20, 30));
    }

    private class ButtonClickListener implements View.OnClickListener
    {
        int rows, cols, bombs;
        ButtonClickListener(int rows, int cols, int bombs)
        {
            this.rows = rows;
            this.cols = cols;
            this.bombs = bombs;
        }
        @Override
        public void onClick(View view)
        {
            Intent game  = new Intent(getApplicationContext(), GameActivity.class);
            game.putExtra("ROWS", rows);
            game.putExtra("COLS", cols);
            game.putExtra("BOMBS", bombs);
            startActivity(game);
        }
    }
}
