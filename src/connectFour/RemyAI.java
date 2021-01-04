package connectFour;

import java.util.*;

public class RemyAI {
    
    private final int DIAG_MULTIPLIER = 4;
    private final int HOR_MULTIPLIER = 2;
    private final int[] NUM_CONNECTED_SCORE = {0, 1, 25, 200, 3000};
    // represents how much a certain number of pieces connected are worth
    private final int[] NUM_ITERATIONS = {0, 1, 3, 5};
    // represents how many iterations of move checking the AI will perform before returning a move
    // based on difficulty
    private final int[] RNG_AMOUNT = {100, 50, 35, 30};
    // represents the range of the random number added to each move's potential, decreases with
    // difficulty to ensure more accurate moves are made

    private int difficulty;
    private int player;
    private RemyGameBoard board;

    public RemyAI(int difficulty, int player, RemyGameBoard board) {
        if(difficulty < 0 || difficulty > 3 || player < 1 || player > 2) {
            throw new IllegalArgumentException();
        }
        this.difficulty = difficulty;
        this.player = player;
        this.board = board;
    }
    
    public RemyAI(RemyGameBoard board) {
        //create a dummy AI
        this(0, 2, board);
    }

    public int getMove() { // calculates potential of all possible moves and picks the best one
        Random rng = new Random();
        int[] movePotential;
        if(difficulty > 0) { // if difficulty is 0, don't calculate potential, just add rng
            movePotential = createMovePotentialArray(board, 1, player);
        } else {
            movePotential = new int[board.NUM_COLUMNS];
        }
        for(int i = 0; i < movePotential.length; i++) {
            if(!board.isValidMove(i)) {
                movePotential[i] = -100000; // to ensure an invalid move doesn't get picked
            } else {
                movePotential[i] += rng.nextInt
                        (Math.abs(average(board, movePotential)) / 4 + RNG_AMOUNT[difficulty]);
            } // add random number based on average value plus a set value to reduce predictability
        }
        return getBestMove(movePotential);
    }

    private int getBestMove(int[] movePotential) {
        int max = getMaxPotential(movePotential);
        int move = 0;
        while(move < movePotential.length && movePotential[move] != max) {
            move++;
        }
        return move;
    }
    
    private int getMaxPotential(int[] movePotential) {
        int max = movePotential[0];
        for(int i = 1; i < movePotential.length; i++) {
            max = Math.max(max, movePotential[i]);
        }
        return max;
    }
    
    private int getMinPotential(int[] movePotential) {
        int min = movePotential[0];
        for(int i = 1; i < movePotential.length; i++) {
            min = Math.min(min, movePotential[i]);
        }
        return min;
    }
    
    private int[] createMovePotentialArray(RemyGameBoard currentBoard,
            int iteration, int currentPlayer) {
        // creates an array representing the best/worst possible moves given a certain state
        // using a minimax algorithm
        int[] movePotential = new int[currentBoard.NUM_COLUMNS];
        for(int i = 0; i < movePotential.length; i++) {
            if(currentBoard.isValidMove(i)) {
                RemyGameBoard nextMove = new RemyGameBoard();
                nextMove.copy(currentBoard);
                nextMove.dropPiece(i, currentPlayer);
                if(!nextMove.gameOver()) {
                    if(NUM_ITERATIONS[difficulty] == iteration) {
                        // if at terminating depth, calculate potential of game state
                        movePotential[i] = calculateGameStatePotential(nextMove, currentPlayer);
                    } else if(iteration < NUM_ITERATIONS[difficulty]){
                        if(currentPlayer == player) {
                            // if AI is to play, try to minimize losses
                            movePotential[i] = getMinPotential(
                                    createMovePotentialArray(
                                            nextMove, iteration + 1,
                                            currentPlayer % 2 + 1));
                        } else {
                            // assume player after them will pick best possible move
                            movePotential[i] = getMaxPotential(
                                    createMovePotentialArray(
                                            nextMove, iteration + 1,
                                            currentPlayer % 2 + 1));
                        }
                    } 
                } else if(nextMove.checkForWin(player)) {
                    // want this game state if possible
                    movePotential[i] = 90000;
                } else if(nextMove.checkForWin(player % 2 + 1)) {
                    // avoid this game state if possible
                    movePotential[i] = -90000;
                }
            }
        }
        return movePotential;
    }

    private int calculateGameStatePotential(RemyGameBoard currentBoard, int currentPlayer) {
        // returns how much a certain game state is worth
        // calculates strength of player's position minus strength of opponents position
        return getOffensivePotential(currentBoard, currentPlayer)
                - getOffensivePotential(currentBoard, currentPlayer % 2 + 1);
    }

    private int getOffensivePotential(RemyGameBoard currentBoard, int currentPlayer) {
        // calculates strength of game state for one player only
        int potential = 0;
        for(int i = 1; i < 5; i++) {
            // multiplies number connected of a certain type by score per number connected,
            // then by multiplier for that type
            potential += NUM_CONNECTED_SCORE[i]
                            * currentBoard.totalNumHorConnected(currentPlayer)[i]
                            * HOR_MULTIPLIER
                    + NUM_CONNECTED_SCORE[i]
                            * currentBoard.totalNumVertConnected(currentPlayer)[i]
                    + NUM_CONNECTED_SCORE[i]
                            * currentBoard.totalNumLeftDiagConnected(currentPlayer)[i]
                            * DIAG_MULTIPLIER
                    + NUM_CONNECTED_SCORE[i]
                            * currentBoard.totalNumRightDiagConnected(currentPlayer)[i]
                            * DIAG_MULTIPLIER;
        }
        return potential;
    }
    
    private int average(RemyGameBoard currentBoard, int[] movePotential) {
        // gets the average move potential of a given array
        if(currentBoard.numValidMoves() == 0) {
            return 0;
        }
        int sum = 0;
        for(int i = 0; i < movePotential.length; i++) {
            sum += movePotential[i];
        }
        return sum / currentBoard.numValidMoves();
    }
}
