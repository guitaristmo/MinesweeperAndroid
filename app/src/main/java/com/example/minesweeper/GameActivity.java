package com.example.minesweeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class GameActivity extends AppCompatActivity
{
    private static final String TAG = "GAME_ACTIVITY";
    GameAdapter mGameboardAdapter;
    MinesweeperModel mGameModel;
    View gameView;
    TextView flagsView;
    TextView bombsView;
    boolean showStats = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        newGameFromBundle();
    }



    @Override
    protected void onRestoreInstanceState(@NonNull Bundle inBundle)
    {
        Log.d(TAG, "onRestoreInstanceState: ");
        mGameModel = (MinesweeperModel) inBundle.getSerializable("MODEL");
    }

    @Override
    protected void onResume()
    {
        Log.d(TAG, "onResume: ");
        super.onResume();
        mGameModel.resetCellHandler(new CellHandler());
        setupViews();
    }

    @Override
    protected  void onSaveInstanceState(@NonNull Bundle outBundle)
    {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outBundle);
        mGameModel.resetCellHandler(null);
        outBundle.putSerializable("MODEL", mGameModel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }





    private void setupViews()
    {
        gameView = findViewById(R.id.activity_game);
        RecyclerView gameBoardView = findViewById(R.id.recycler_view);
        gameBoardView.setHasFixedSize(true);

        RecyclerView.LayoutManager gameboardLayoutManager = new GridLayoutManager(this, mGameModel.cols);
        mGameboardAdapter = new GameAdapter(this, mGameModel);

        gameBoardView.setLayoutManager(gameboardLayoutManager);
        gameBoardView.setAdapter(mGameboardAdapter);

        flagsView = findViewById(R.id.tv_flags);
        bombsView = findViewById(R.id.tv_bombs);


        updateStats();
    }

    private void updateStats()
    {
        if(!showStats)
        {
            flagsView.setText("");
            bombsView.setText("");
            return;
        }
        flagsView.setText("Flags: " + mGameModel.flags);
        bombsView.setText("Bombs: " + mGameModel.bombCount);
    }

    private void newGameFromBundle()
    {
        Bundle bundle = getIntent().getExtras();
        int rows = bundle.getInt("ROWS");
        int cols = bundle.getInt("COLS");
        int bombs = bundle.getInt("BOMBS");
        mGameModel = new MinesweeperModel(rows, cols, bombs);
    }

    public void stats_listener(MenuItem item)
    {
        item.setChecked(!item.isChecked());
        showStats = item.isChecked();
        updateStats();
    }

    public void AboutListener(MenuItem item)
    {
        Intent aboutScreen = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(aboutScreen);
    }


    private class CellHandler implements CellEventHandlerInterface
    {
        @Override
        public void openCell(MinesweeperModel.CellEventArgs args)
        {
            int value = mGameModel.board[args.row][args.col].value;
            mGameboardAdapter.cells[args.row][args.col].button.setText(mGameboardAdapter.getText(value));
        }

        @Override
        public void gameLost(MinesweeperModel.BombHitEventArgs args)
        {
            for (MinesweeperModel.Bomb bomb : args.bombList)
            {
                mGameboardAdapter.cells[bomb.rowIndex][bomb.colIndex].button.setText("B");
                Snackbar.make(gameView, "You Lost", Snackbar.LENGTH_LONG).setAction("New Game", new NewGameListener()).show();
            }

            for (int row = 0; row < mGameboardAdapter.cells.length; row++)
            {
                for (int col = 0; col < mGameboardAdapter.cells[0].length; col++)
                {
                    mGameboardAdapter.cells[row][col].button.setOnClickListener(null);
                }
            }
        }
        @Override
        public void gameWon()
        {
            Snackbar.make(gameView, "You Won!", Snackbar.LENGTH_LONG).setAction("New Game", new NewGameListener()).show();
        }

        @Override
        public void cellFlagged(int row, int col)
        {
            mGameboardAdapter.cells[row][col].button.setText("F");
            updateStats();
        }

        @Override
        public void cellUnFlagged(int row, int col)
        {
            mGameboardAdapter.cells[row][col].button.setText("");
            updateStats();
        }

    }
    private class NewGameListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            finish();
        }
    }
}
