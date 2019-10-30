package com.example.minesweeper;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class MinesweeperModel implements Serializable
{
    static final long serialVersionUID = 42L;

    public enum CellStatus {Closed, Open}

    private CellEventHandlerInterface cellListener;
    public final int bombCount;
    public final int rows;
    public final int cols;
    private ArrayList<Bomb> bombs;
    public Cell[][] board;
    private boolean initiated;
    private int openCellMethodCounter;

    public MinesweeperModel(int rows, int cols, int bombCount)
    {
        this.rows = rows;
        this.cols = cols;
        this.bombCount = bombCount;
        initiated = false;
        openCellMethodCounter = 0;
        bombs = new ArrayList<>();
        board = new Cell[this.rows][this.cols];

        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                board[row][col] = new Cell(row, col, 0);
            }
        }
    }

    public void placeBombs(int originalRow, int originalCol) {
        boolean valid = true;
        int bombRow;
        int bombCol;
        Random placeGetter = new Random();
        ArrayList<Cell> originalsNeighbors = getNeighbors(originalRow, originalCol);

        for (int counter = 0; counter < bombCount; counter++) {
            do {
                valid = true;
                bombRow = placeGetter.nextInt(rows);
                bombCol = placeGetter.nextInt(cols);

                if (originalRow == bombRow && originalCol == bombCol)
                    valid = false;
                if (board[bombRow][bombCol].value == -1)
                    valid = false;
                for (Cell neighbor : originalsNeighbors) {
                    if (neighbor.row == bombRow && neighbor.col == bombCol)
                        valid = false;
                }
            }
            while (!valid);
            board[bombRow][bombCol].value = -1;
            bombs.add(new Bomb(bombRow, bombCol));
        }
        fillBoard();
    }

    public void fillBoard() {
        for (Bomb bomb : bombs) {
            for (Cell neighbor : getNeighbors(bomb.rowIndex, bomb.colIndex)) {
                if (neighbor.value != -1)
                    neighbor.value += 1;
            }
        }
        initiated = true;
    }

    public void cellClicked(int row, int col) {
        //to prevent infinite recursive loops by adjacent empty cells
        if (board[row][col].state == CellStatus.Open)
            return;
        board[row][col].state = CellStatus.Open;

        if (!initiated)
            placeBombs(row, col);

        openCellMethodCounter++;

        if (board[row][col].value == -1)
        {
            cellListener.GameLost(new BombHitEventArgs(bombs));
            openCellMethodCounter--;
            cellListener.OpenCell(new CellEventArgs(row, col, openCellMethodCounter));
        }
            else if (board[row][col].value == 0)
                emptyCellClicked(row, col);
            else
        {
            openCellMethodCounter--;
            cellListener.OpenCell(new CellEventArgs(row, col, openCellMethodCounter));
            checkGameWon();
        }
    }

    private void checkGameWon()
    {
        for(int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                if (board[row][col].state == CellStatus.Closed)
                    return;
            }
        }
        cellListener.GameWon();
    }

    public void emptyCellClicked(int row, int col) {
        board[row][col].state = CellStatus.Open;
        for (Cell cell : getNeighbors(row, col))
        {
            cellClicked(cell.row, cell.col);
        }
        openCellMethodCounter--;
        cellListener.OpenCell(new CellEventArgs(row, col, openCellMethodCounter));
    }

    public ArrayList<Cell> getNeighbors(int row, int col) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>();
        for (int rowCounter = row - 1; rowCounter <= row + 1; rowCounter++) {
            for (int colCounter = col - 1; colCounter <= col + 1; colCounter++) {
                if (!(rowCounter == row && colCounter == col) && rowCounter >= 0 && rowCounter < rows && colCounter >= 0 && colCounter < cols)
                    neighbors.add(board[rowCounter][colCounter]);
            }
        }
        return neighbors;
    }

    public void resetCellHandler(CellEventHandlerInterface handlerInterface)
    {
        this.cellListener = handlerInterface;
    }




    //Note: row and col start at 0
    public int inspect(int row, int col) {
        return board[row][col].value;
    }

    public String toString() {
        StringBuilder returnString = new StringBuilder();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                returnString.append(board[row][col].value);
            }
            returnString.append("\n");
        }
        return returnString.toString();
    }

    public class Cell implements Serializable
    {
        public int row;
        public int col;
        public int value;
        public MinesweeperModel.CellStatus state;

        public Cell(int row, int col, int value)
        {
            this.row = row;
            this.col = col;
            this.value = value;
            state = CellStatus.Closed;
        }
    }

    public class Bomb implements Serializable
    {
        public int rowIndex, colIndex;
        public Bomb(int rowIndex, int colIndex)
        {
            this.rowIndex = rowIndex;
            this.colIndex = colIndex;
        }
    }

    public class CellEventArgs
    {
        public final int row;
        public final int col;
        public final int openCellCounter;

        public CellEventArgs(int row, int col, int openCellCounter)
        {
            this.row = row;
            this.col = col;
            this.openCellCounter = openCellCounter;
        }
    }

    public class BombHitEventArgs
    {
        public final ArrayList<Bomb> bombList;

        public BombHitEventArgs(ArrayList<Bomb> bombList)
        {
            this.bombList = bombList;
        }
    }

}









