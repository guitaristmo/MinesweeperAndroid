package com.example.minesweeper;

import java.io.*;

public class ModelSerializationManager
{
    static String filename = "file.ser";

    public static void serializeBoard(MinesweeperModel model)
    {
        try
        {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(model);

            out.close();
            file.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static MinesweeperModel getBoard()
    {
        MinesweeperModel toReturn = null;
        try
        {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            toReturn = (MinesweeperModel) in.readObject();

            in.close();
            file.close();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
        return toReturn;
    }
}
