package com.company.herosbattle;

import java.util.ArrayList;
import java.util.List;

public class GamePieces {

    public static class Pawn extends Character{
        public Pawn(String playerName, int x, int y) {
            super(playerName, x, y);
        }

        @Override
        public boolean isValidMove(int newX, int newY,Character[][] board) {
            if((Math.abs(newX-x)<=1) && (Math.abs(newY-y)<=1)  ){
                // Checking for opponent in path
                if(board[newX][newY]==null || !board[newX][newY].playerName.equals(playerName)){
                    return true;
                }

            }
            return false;
        }
    }

    public static class Hero1 extends Character{
        public Hero1(String playerName, int x, int y) {
            super(playerName, x, y);
        }

        @Override
        public boolean isValidMove(int newX, int newY, Character[][] board) {
            // Check if the move is either in the same row or the same column
            if (newX == x || newY == y) {
                int minX = Math.min(x, newX);
                int maxX = Math.max(x, newX);
                int minY = Math.min(y, newY);
                int maxY = Math.max(y, newY);

                // Check for clear path (no friendly characters in the way)
                if (newX == x) {  // Moving vertically
                    for (int i = minY + 1; i < maxY; i++) {
                        if (board[x][i] != null) {
                            return false;  // Blocked by another character
                        }
                    }
                } else if (newY == y) {  // Moving horizontally
                    for (int i = minX + 1; i < maxX; i++) {
                        if (board[i][y] != null) {
                            return false;  // Blocked by another character
                        }
                    }
                }

                // Check the destination cell
                if (board[newX][newY] != null) {
                    if (board[newX][newY].playerName.equals(this.playerName)) {
                        return false;  // Cannot capture own piece
                    }
                }

                return true;  // Valid move
            }

            return false;  // Invalid move (not in the same row or column)
        }

    }

    public static class Hero2 extends Character {

        public Hero2(String playerName, int x, int y) {
            super(playerName, x, y);
        }

        @Override
        public boolean isValidMove(int targetRow, int targetCol, Character[][] board) {
            int rowDiff = Math.abs(targetRow - this.getX());
            int colDiff = Math.abs(targetCol - this.getY());
            // Valid if diagonal movement by 1 or 2 cells
            return rowDiff == colDiff && (rowDiff == 1 || rowDiff == 2);
        }

        @Override
        public List<int[]> getPathToDestination(int targetRow, int targetCol) {
            List<int[]> path = new ArrayList<>();

            int currentRow = this.getX();
            int currentCol = this.getY();

            if (Math.abs(targetRow - currentRow) == Math.abs(targetCol - currentCol)) { // Diagonal movement
                int rowStep = targetRow > currentRow ? 1 : -1;
                int colStep = targetCol > currentCol ? 1 : -1;

                for (int i = 1; i < Math.abs(targetRow - currentRow); i++) {
                    path.add(new int[]{currentRow + i * rowStep, currentCol + i * colStep});
                }
            }

            return path;
        }
    }

}
