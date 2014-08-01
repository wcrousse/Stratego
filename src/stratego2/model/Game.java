package stratego2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import stratego2.model.Player.HumanPlayer;
import stratego2.model.Player.Player;

/**
 *
 * @author roussew
 */
public class Game {

    public static final int NUM_ROWS = 10;
    public static final int NUM_COLUMNS = 10;

    public static final int RED = 0;
    public final int BLUE = 1;
    public static final int ARMY_SIZE = 40;
    /**
     * a tuple to hold the player objects.  
     **/
    Player[] players;
    /**
     * intended as to index the players array. Should be incremented and then
     * modded by 2 following each move. Thus, should only equal 0 or 1.
     */
    private int toMove;
    /**
     * a data structure representing the current game layout
     */
    private GameState state;
    private List<FriendlyPiece> redArmy, blueArmy, redCaptured, blueCaptured;
    private boolean isFlagCaptured;
    private final StrategoRules rules;

    public Game() {
        players = new Player[2];
        state = new GameState();
        redCaptured = new ArrayList<>();
        blueCaptured = new ArrayList<>();
        rules = new StrategoRules();
    }

    /**
     * begins the game.
     * @throws java.lang.Exception
     */
    public void startGame() throws Exception {
        setupBoard();
        //players[1].setDisplay(new GameFrame(players[1].getColor()));
        toMove = RED;
        Color winner = play();
        for (Player p: players) p.reportResult(winner);
    }
    protected Color play() throws Exception {
        do {
            for (Player p: players) p.displayBoard();//probably should change leave it for now
            Move move = players[toMove].getMove();
            while (!rules.isLegal(state, move)) {               
                try {
                    players[toMove].reportIllegalMove();
                } catch (Exception ex) {
                    System.err.println("AAAAHHH!!!! AI has gone CRAZY!!!!" + 
                            move.toString());
                    throw ex;
                }
                move = players[toMove].getMove();
            }
            processMove(move);
            for (Player p: players) p.reportMove(move);
            
            toMove = (toMove + 1) % 2;
        } while (!isGameOver());
        for (Player p: players) p.displayBoard();
        return declareWinner();
    }

    public void setPlayers(Player bluePlayer, Player redPlayer) {
        players[BLUE] = bluePlayer;
        players[RED] = redPlayer;
    }

    /**
     * performs the setup stage of the game. Here the players choose the
     * starting configuration of their pieces.
     */
    private void setupBoard() {

        try {
            Thread t1 = new Thread() {
                @Override
                public void run() {
                    blueArmy = players[0].getSetup();
                }
            };
            
            Thread t2 = new Thread() {
                @Override
                public void run() {
                    redArmy = players[1].getSetup();
                }
            };
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        state = new GameState(Color.RED, redArmy, blueArmy);
    }

    /**
     * examines the position of the player to move. to be called after each move
     * is made.
     *
     * @return returns true if the player's flag has been captured or if the
     * player has no available moves and false otherwise.
     */
    protected boolean isGameOver() {
        boolean result = true;
        if (!isFlagCaptured) {  
            List<FriendlyPiece> army = (toMove != BLUE) ? blueArmy : redArmy;
            for (FriendlyPiece piece : army) {
                if (hasMove(piece)) {
                    System.out.println(true);
                    return false;
                }
            }
        }
        return result;

    }

    /**
     * tests a piece to determine whether or not it has any legal moves.
     * intended to be called from isGameOver method
     *
     * @param piece the piece to be tested
     * @return true if the piece can move, false otherwise
     */
    protected boolean hasMove(FriendlyPiece piece) {
        boolean result = false;
        if (rules.isLegal(state, piece, piece.getRow() - 1, piece.getColumn())) {
            result = true;
        } else if (rules.isLegal(state, piece, piece.getRow() + 1, piece.getColumn())) {
            result = true;
        } else if (rules.isLegal(state, piece, piece.getRow(), piece.getColumn() - 1)) {
            result = true;
        } else if (rules.isLegal(state, piece, piece.getRow(), piece.getColumn() + 1)) {
            result = true;
        }
        return result;
    }

    protected void printMove(boolean result, Piece piece, int row, int column) {
        System.out.println(result + piece.toString() + row + column);
    }

    /**
     * processes a move.
     *
     * @param move
     */
    private void processMove(Move move) {
        int destRow = move.getDestinationRow();
        int destCol = move.getDestinationColumn();
        Square destSquare = state.getSquare(destRow, destCol);
        int row = move.getStartRow();
        int column = move.getStartColumn();
        FriendlyPiece piece = (FriendlyPiece)state.getSquare(row, column).getOccupier();
        piece = (FriendlyPiece)piece.setLocation(destRow, destCol);
        if (destSquare.isOccupied()) {
            FriendlyPiece defender = 
                    (FriendlyPiece)state.getSquare(destRow, destCol).getOccupier();
            state = state.makeMove(move);
            piece = (FriendlyPiece) piece.setLocation(destRow, destCol);
            Piece winner;
            winner = rules.resolveAttack(state, defender, piece);
            
            state = state.placePiece(destRow, destCol, winner);
            destSquare = state.getSquare(destRow, destCol);
            if (defender.getRank() == Rank.FLAG) isFlagCaptured = true;
            for(Player p: players) p.revealSquare(destSquare);
        }
        else {
            state = state.makeMove(move);
        }
        
    }

    protected Color declareWinner() {
        Player winner = (toMove == BLUE)? players[BLUE]: players[RED];
        //for(Player p: players) p.reportResult(winner.getColor());
        return winner.getColor();
    }
    
}
