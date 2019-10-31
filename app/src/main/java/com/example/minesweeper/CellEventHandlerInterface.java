package com.example.minesweeper;

public interface CellEventHandlerInterface
{
    void openCell(MinesweeperModel.CellEventArgs args);
    void gameLost(MinesweeperModel.BombHitEventArgs args);
    void gameWon();
    void cellFlagged(int row, int col);
    void cellUnFlagged(int row, int col);

}
