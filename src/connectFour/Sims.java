package connectFour;

//for running simulations between AIs
public class Sims {

    public static void main(String[] args) {
        
        RemyGameBoard board = new RemyGameBoard();
        RemyAI ai1 = new RemyAI(3, 1, board);
        RemyAI ai2 = new RemyAI(3, 2, board);
        
        int p1Wins = 0;
        int p2Wins = 0;
        int ties = 0;
        int player = 1;
        
        for(int i = 0; i < 100; i++) {
            
            board.reset();
            //System.out.println(board);
            while(!board.gameOver()) {
                int move;
                    if(player == 1) {
                        move = ai1.getMove();
                    } else {
                        move = ai2.getMove();
                    }
                board.dropPiece(move, player);
                //System.out.println(board);
                player = player % 2 + 1; // switch between player 1 and 2
            }

            if(board.checkForWin(1)) {
                //System.out.println("Player 1 wins!");
                p1Wins++;
            } else if(board.checkForWin(2)) {
                //System.out.println("Player 2 wins!");
                p2Wins++;
            } else {
                //System.out.println("It's a tie.");
                ties++;
            }
            System.out.println(i);
        }
        System.out.println("p1: " + p1Wins);
        System.out.println("p2: " + p2Wins);
        System.out.println("ties: " + ties);
    }
}

