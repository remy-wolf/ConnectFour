package connectFour;

import java.util.*;

public class RemyGameBoard {

    final int NUM_ROWS = 6;
    final int NUM_COLUMNS = 7;
    final char EMPTY_SPACE = '-';
    final char P1_PIECE = 'X';
    final char P2_PIECE = 'O';

    private char[][] board = new char[NUM_ROWS][NUM_COLUMNS];
    // to store location of pieces

    public RemyGameBoard() {
        reset();
    } // constructor, set all values to EMPTY_SPACE

    public void reset() { // fill board with EMPTY_SPACE characters
        for(int i = 0; i < NUM_ROWS; i++) {
            Arrays.fill(board[i], EMPTY_SPACE);
        }
    }

    public void copy(RemyGameBoard boardToCopy) {
        this.board = boardToCopy.getBoard();
    }

    public char[][] getBoard() {
        // copy board[][] array to a brand new array and return it to prevent
        // problems with identical references
        char[][] newBoard = new char[NUM_ROWS][NUM_COLUMNS];
        for(int i = 0; i < NUM_ROWS; i++) {
            for(int j = 0; j < NUM_COLUMNS; j++) {
                newBoard[i][j] = this.board[i][j];
            }
        }
        return newBoard;
    }

    private char getPlayerPiece(int player) { // convert player (integer) to playerPiece (char)
        if(player > 2 || player < 1) {
            throw new IllegalArgumentException();
        }
        if(player == 1) {
            return P1_PIECE;
        } else {
            return P2_PIECE;
        }
    }

    private char getOpposingPlayerPiece(int player) {
        return getPlayerPiece(player % 2 + 1);
    }

    public void dropPiece(int column, int player) { // place a piece in the specified location
        if(column >= NUM_COLUMNS || column < 0 || heightOfColumn(column) >= NUM_ROWS) {
            throw new IllegalArgumentException();
        }
        char playerPiece = getPlayerPiece(player);
        board[heightOfColumn(column)][column] = playerPiece;
    }

    public int heightOfColumn(int column) {
        // returns how many pieces have been placed in a certain column
        if(column >= NUM_COLUMNS || column < 0) {
            throw new IllegalArgumentException();
        }
        int height = 0;
        while(height < NUM_ROWS && board[height][column] != EMPTY_SPACE) {
            height++;
        }
        return height;
    }

    private int numHorConnected(int row, int column, int player) {
        // returns how many pieces a player has connected or separated only by empty space
        // horizontally, right of the column specified
        if(column >= NUM_COLUMNS - 3) { // non winnable position
            return 0;
        }
        char playerPiece = getPlayerPiece(player);
        char opponentPiece = getOpposingPlayerPiece(player);
        int numConnected = 0;
        int i = 0;
        while(column + i < NUM_COLUMNS && i < 4) {
            if(board[row][column + i] == playerPiece) {
                numConnected++;
            } else if(board[row][column + i] == opponentPiece) {
                return 0; // if player is blocked, return 0 as there is no chance to win
            }
            i++;
        }
        return numConnected;
    }
    
    public int[] totalNumHorConnected(int player) {
        // returns how many 2-in-a-rows, 3-in-a-rows, 4-in-a-rows, etc. the player has
        // in the entire board that are in a winnable position
        int[] numConnected = new int[5];
        // stores how many 3s have been connected horizontally, 2s, 1s, etc.
        // number at index 3 represents how many horizontal 3-in-a-rows the player has
        for(int row = 0; row < NUM_ROWS; row++) {
            for(int column = 0; column < NUM_COLUMNS - 3; column++) {
                numConnected[(numHorConnected(row, column, player))]++;
            }
        }
        return numConnected;
    }

    private int numVertConnected(int row, int column, int player) {
        if(row >= NUM_ROWS - 3) {
            return 0;
        }
        char playerPiece = getPlayerPiece(player);
        char opponentPiece = getOpposingPlayerPiece(player);
        int numConnected = 0;
        int i = 0;
        while(row + i < NUM_ROWS && i < 4) {
            if(board[row + i][column] == playerPiece) {
                numConnected++;
            } else if(board[row + i][column] == opponentPiece) {
                return 0;
            }
            i++;
        }
        return numConnected;
    }
    
    public int[] totalNumVertConnected(int player) {
        int[] numConnected = new int[5];
        for(int row = 0; row < NUM_ROWS - 3; row++) {
            for(int column = 0; column < NUM_COLUMNS; column++) {
                numConnected[(numVertConnected(row, column, player))]++;
            }
        }
        return numConnected;
    }

    private int numLeftDiagConnected(int row, int column, int player) {
        if(row >= NUM_ROWS - 3 || column <= 2) {
            return 0;
        }
        char playerPiece = getPlayerPiece(player);
        char opponentPiece = getOpposingPlayerPiece(player);
        int numConnected = 0;
        int i = 0;
        while(i <= column && row + i < NUM_ROWS && i < 4) {
            if(board[row + i][column - i] == playerPiece) {
                numConnected++;
            } else if(board[row + i][column - i] == opponentPiece) {
                return 0;
            }
            i++;
        }
        return numConnected;
    }
    
    public int[] totalNumLeftDiagConnected(int player) {
        int[] numConnected = new int[5];
        for(int row = 0; row < NUM_ROWS - 3; row++) {
            for(int column = 2; column < NUM_COLUMNS; column++) {
                numConnected[(numLeftDiagConnected(row, column, player))]++;
            }
        }
        return numConnected;
    }

    private int numRightDiagConnected(int row, int column, int player) {
        if(row >= NUM_ROWS - 3 || column >= NUM_COLUMNS - 3) {
            return 0;
        }
        char playerPiece = getPlayerPiece(player);
        char opponentPiece = getOpposingPlayerPiece(player);
        int numConnected = 0;
        int i = 0;
        while(row + i < NUM_ROWS && column + i < NUM_COLUMNS && i < 4) {
            if(board[row + i][column + i] == playerPiece) {
                numConnected++;
            } else if(board[row + i][column + i] == opponentPiece) {
                return 0;
            }
            i++;
        }
        return numConnected;
    }
    
    public int[] totalNumRightDiagConnected(int player) {
        int[] numConnected = new int[5];
        for(int row = 0; row < NUM_ROWS - 3; row++) {
            for(int column = 0; column < NUM_COLUMNS - 3; column++) {
                numConnected[(numRightDiagConnected(row, column, player))]++;
            }
        }
        return numConnected;
    }

    public boolean checkForWin(int player) {
        // checks if the specified player won by connecting four pieces
        // if there are any 4-in-a-rows in the board, return true
        return totalNumHorConnected(player)[4] > 0
                || totalNumVertConnected(player)[4] > 0
                || totalNumLeftDiagConnected(player)[4] > 0
                || totalNumRightDiagConnected(player)[4] > 0;
    }

    public boolean checkForTie() {
        // check if all spaces have been filled with no winner
        if(!checkForWin(1) && !checkForWin(2)) {
            for(char[] row : board) {
                for(char n : row) {
                    if(n == EMPTY_SPACE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean gameOver() {
        return checkForWin(1) || checkForWin(2) || checkForTie();
    }

    public boolean isValidMove(int column) {
        return column >= 0 && column < NUM_COLUMNS && heightOfColumn(column) < NUM_ROWS;
    }
    
    public int numValidMoves() {
        int validMoves = 0;
        for(int i = 0; i < NUM_COLUMNS; i++) {
            if(isValidMove(i)) {
                validMoves++;
            }
        }
        return validMoves;
    }

    public String toString() {
        String graphicBoard = "";
        for(int i = 0; i < NUM_COLUMNS * 2 + 1; i++) {
            graphicBoard += "_";
        }
        graphicBoard += "\n";
        for(int i = NUM_ROWS - 1; i >= 0; i--) {
            graphicBoard += "|";
            for(int j = 0; j < NUM_COLUMNS; j++) {
                graphicBoard += board[i][j];
                if(j < NUM_COLUMNS - 1) {
                    graphicBoard += " ";
                }
            }
            graphicBoard += "|\n";
        }
        for(int i = 0; i < NUM_COLUMNS; i++) {
            graphicBoard += " " + i;
        }
        return graphicBoard;
    }
}
