package com.company.herosbattle;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    protected int x,y; // Positioning of character on board
    protected String playerName; // Name of player

    public Character(String playerName,int x, int y) {
        this.x = x;
        this.y = y;
        this.playerName = playerName;
    }

    public  abstract boolean isValidMove(int newX, int newY, Character[][] board);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveTo(int newX, int newY){
        this.x = newX;
        this.y = newY;
    }


    public List<int[]> getPathToDestination(int targetRow, int targetCol) {
        List<int[]> path = new ArrayList<>();

        int currentRow = this.getX();
        int currentCol = this.getY();

        if (currentRow == targetRow) {
            // Horizontal movement
            int step = targetCol > currentCol ? 1 : -1;
            for (int col = currentCol + step; col != targetCol; col += step) {
                path.add(new int[]{currentRow, col});
            }
        } else if (currentCol == targetCol) {
            // Vertical movement
            int step = targetRow > currentRow ? 1 : -1;
            for (int row = currentRow + step; row != targetRow; row += step) {
                path.add(new int[]{row, currentCol});
            }
        }
        return path;
    }

}
