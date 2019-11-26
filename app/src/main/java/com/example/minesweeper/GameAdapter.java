package com.example.minesweeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder>
{
    private Context gameContext;
    private MinesweeperModel mGameModel;
    CellButton[][] cells;

    GameAdapter(Context gameContext, MinesweeperModel gameModel)
    {
        this.gameContext = gameContext;
        mGameModel = gameModel;
        cells = new CellButton[mGameModel.rows][mGameModel.cols];
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        System.out.println("onBindViewHolder");
        //good for squares only
        int cellRow = position/mGameModel.rows;
        int cellCol = position % mGameModel.rows;

        cells[cellRow][cellCol] = new CellButton(cellRow, cellCol, holder.cell);

        holder.cell.setOnClickListener(new CellClickListener(cellRow, cellCol));
        holder.cell.setOnLongClickListener(new CellLongClickListener(cellRow, cellCol));
        if(mGameModel.board[cellRow][cellCol].state == MinesweeperModel.CellStatus.Open)
            holder.cell.setText(getText(mGameModel.board[cellRow][cellCol].value));
        if(mGameModel.board[cellRow][cellCol].isFlagged())
            cells[cellRow][cellCol].button.setText("F");
    }

    String getText(int value)
    {
        if (value < 0)
            return "B";
        else
            return String.valueOf(value);
    }

    @Override
    public int getItemCount() { return (mGameModel.rows * mGameModel.cols); }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private final Button cell;
        ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            cell = itemView.findViewById(R.id.cell);
        }
    }


    public class CellLongClickListener implements View.OnLongClickListener
    {
        int row;
        int col;

        CellLongClickListener(int row, int col)
        {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean onLongClick(View view)
        {
            if (mGameModel.board[row][col].state == MinesweeperModel.CellStatus.Closed)
                toggleCellFlag(row, col);
            else
                Toast.makeText(gameContext, "Cannot place flag on an open tile", Toast.LENGTH_SHORT).show();

            return true;
        }
    }

    private void toggleCellFlag(int row, int col)
    {
        if (mGameModel.board[row][col].isFlagged())
            mGameModel.unflagCell(row, col);
        else
            mGameModel.flagCell(row, col);
    }

    public class CellClickListener implements View.OnClickListener
    {
        int row;
        int col;

        CellClickListener(int row, int col)
        {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View view)
        {
            mGameModel.cellClicked(row, col);

            if(mGameModel.board[row][col].isFlagged())
                Toast.makeText(gameContext, "Cannot press flagged tile", Toast.LENGTH_SHORT).show();
        }
    }

    public class CellButton
    {
        final int row;
        final int col;
        final Button button;

        CellButton(int row, int col, Button button)
        {
            this.row = row;
            this.col = col;
            this.button = button;
        }
    }
}

