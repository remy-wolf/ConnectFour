package connectFour;

import java.util.*;

public class RemyMain {

    public static Scanner console = new Scanner(System.in);

    public static int getNextInt(String prompt) {
        System.out.print(prompt);
        while(!console.hasNextInt()) {
            console.next(); // to discard the input
            System.out.print("Invalid input format. Try again: ");
        }
        return console.nextInt();
    }

    public static int getPlayers() {
        int numPlayers = getNextInt("How many players? (0 for simulation, 1 to play against\n"
                + "the computer, or 2 to play against a friend): ");
        while(numPlayers < 0 || numPlayers > 2) {
            numPlayers = getNextInt("Invalid value: please type 0 for simulation, 1 to\n"
                    + "play against the computer, or 2 to play against a friend: ");
        }
        return numPlayers;
    }

    public static RemyAI createNewAI(int player, RemyGameBoard board) {
        int difficulty = getNextInt("Difficulty of AI #" + player + "?\n"
                + "(0 for Eugene Baldonado, 1 for easy, 2 for medium, or 3 for hard): ");
        while(difficulty < 0 || difficulty > 3) {
            difficulty = getNextInt("Invalid value: please type 0 for Eugene\n"
                    + "Baldonado, 1 for easy, 2 for medium, or 3 for hard: ");
        }
        return new RemyAI(difficulty, player, board);
    }

    public static RemyAI createNewAI(RemyGameBoard board) {
        int difficulty = getNextInt("Difficulty of AI?\n"
                + "(0 for Eugene Baldonado, 1 for easy, 2 for medium, or 3 for hard): ");
        while(difficulty < 0 || difficulty > 3) {
            difficulty = getNextInt("Invalid value: please type 0 for Eugene\n"
                    + "Baldonado, 1 for easy, 2 for medium, or 3 for hard: ");
        }
        return new RemyAI(difficulty, 2, board);
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Connect 4, a game by Remy Wolf!\n");
        
        int numPlayers;
        int player = 1;
        boolean keepPlaying = true;
        RemyGameBoard board = new RemyGameBoard();
        RemyAI ai1 = new RemyAI(board); // dummy AIs
        RemyAI ai2 = new RemyAI(board);
        
        while(keepPlaying) {
            numPlayers = getPlayers();
            if(numPlayers == 0) {
                ai1 = createNewAI(1, board);
                ai2 = createNewAI(2, board);
            } else if(numPlayers == 1) {
                ai1 = createNewAI(board);
            }
            board.reset();
            System.out.println(board);
            while(!board.gameOver()) {
                int move;
                if(numPlayers == 0) {
                    if(player == 1) {
                        move = ai1.getMove();
                    } else {
                        move = ai2.getMove();
                    }
                } else if(numPlayers == 1 && player == 2) {
                    move = ai1.getMove();
                } else {
                    move = getNextInt("Player " + player + ", type the column of your move: ");
                    while(!board.isValidMove(move)) {
                        move = getNextInt("Invalid move. Try again: ");
                    }
                }
                board.dropPiece(move, player);
                System.out.println(board);
                player = player % 2 + 1; // switch between player 1 and 2
            }

            if(board.checkForWin(1)) {
                System.out.println("Player 1 wins!");
            } else if(board.checkForWin(2)) {
                System.out.println("Player 2 wins!");
            } else {
                System.out.println("It's a tie.");
            }
            System.out.print("Play again? (Y/N): ");
            keepPlaying = Character.toLowerCase(console.next().charAt(0)) == 'y';
        }
        System.out.println("Goodbye!");
    }
}
