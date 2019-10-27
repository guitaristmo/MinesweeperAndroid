package com.example.minesweeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class GameActivity extends AppCompatActivity {

    GameAdapter mGameboardAdapter;
    MinesweeperModel mGameModel;
    CellHandler mCellHandler;
    View gameView;
    boolean autoSave;
    int rows, cols, bombs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        unpackBundle();

        setupGame();
        setupViews();
    }

    private void setupGame()
    {
        mCellHandler = new CellHandler();
        mGameModel = new MinesweeperModel(mCellHandler, rows, cols, bombs);
    }

    private void unpackBundle()
    {
        Bundle bundle = getIntent().getExtras();
        rows = bundle.getInt("ROWS");
        cols = bundle.getInt("COLS");
        bombs = bundle.getInt("BOMBS");
    }

    private class CellHandler implements CellEventHandlerInterface
    {
        @Override
        public void OpenCell(MinesweeperModel.CellEventArgs args)
        {
            int value = mGameModel.board[args.row][args.col].value;

            mGameboardAdapter.cells[args.row][args.col].button.setText(value + "");
        }

        @Override
        public void GameLost(MinesweeperModel.BombHitEventArgs args)
        {
            for (MinesweeperModel.Bomb bomb : args.bombList)
            {
                mGameboardAdapter.cells[bomb.rowIndex][bomb.colIndex].button.setText("B");
                Snackbar.make(gameView, "You Lost", Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void GameWon()
        {
            Snackbar.make(gameView, "You Won!", Snackbar.LENGTH_LONG).show();
        }

    }

    private void setupViews()
    {
        gameView = findViewById(R.id.activity_game);
        RecyclerView GameBoardView = findViewById(R.id.recycler_view);
        GameBoardView.setHasFixedSize(true);

        RecyclerView.LayoutManager gameboardLayoutManager = new GridLayoutManager(this, 8);
        mGameboardAdapter = new GameAdapter(this, mGameModel);

        GameBoardView.setLayoutManager(gameboardLayoutManager);
        GameBoardView.setAdapter(mGameboardAdapter);
    }

    @Override
    protected  void onSaveInstanceState(@NonNull Bundle outBundle)
    {
        super.onSaveInstanceState(outBundle);
        outBundle.putSerializable("MODEL", mGameModel);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle inBundle)
    {
        mGameModel = (MinesweeperModel) inBundle.getSerializable("MODEL");
    }
}
