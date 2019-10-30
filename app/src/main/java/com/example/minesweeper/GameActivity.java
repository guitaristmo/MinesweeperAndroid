package com.example.minesweeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class GameActivity extends AppCompatActivity
{
    GameAdapter mGameboardAdapter;
    MinesweeperModel mGameModel;
    View gameView;
    boolean autoSave = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");

        setContentView(R.layout.activity_game);
        if(savedInstanceState == null)
        {
            newGameFromBundle();
            setupViews();
        }
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
    }

    @Override
    protected  void onSaveInstanceState(@NonNull Bundle outBundle)
    {
        System.out.println("onSaveInstanceState");
        super.onSaveInstanceState(outBundle);
        outBundle.putSerializable("MODEL", mGameModel);
        System.out.println("Serialized");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mGameModel.resetCellHandler(new CellHandler());
    }

    @Override
    protected void onStop()
    {
        mGameModel.resetCellHandler(null);
        super.onStop();
        System.out.println("OnStop");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle inBundle)
    {
        mGameModel = (MinesweeperModel) inBundle.getSerializable("MODEL");
        setupViews();
    }

    private void newGameFromBundle()
    {
        Bundle bundle = getIntent().getExtras();
        int rows = bundle.getInt("ROWS");
        int cols = bundle.getInt("COLS");
        int bombs = bundle.getInt("BOMBS");
        mGameModel = new MinesweeperModel(rows, cols, bombs);
    }

    private class CellHandler implements CellEventHandlerInterface
    {
        @Override
        public void OpenCell(MinesweeperModel.CellEventArgs args)
        {
            int value = mGameModel.board[args.row][args.col].value;
            mGameboardAdapter.cells[args.row][args.col].button.setText(value+"");
        }

        @Override
        public void GameLost(MinesweeperModel.BombHitEventArgs args)
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
        public void GameWon()
        {
            Snackbar.make(gameView, "You Won!", Snackbar.LENGTH_LONG).setAction("New Game", new NewGameListener()).show();
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
