package com.example.minesweeper;

public interface CellEventHandlerInterface
{
    void OpenCell(MinesweeperModel.CellEventArgs args);
    void GameLost(MinesweeperModel.BombHitEventArgs args);
    void GameWon();

}
