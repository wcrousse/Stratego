package stratego2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import stratego2.model.Player.AI.AIPlayer;
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
     *
     */
    Player[] players;
    /**
     * intended as to index the players array. Should be incremented and then
     * modded by 2 following each move. Thus, should only equal 0 or 1.
     */
    private int toMove;
    public int depth;
    /**
     * a data structure representing the current game layout
     */
    private GameState state;
    List<FriendlyPiece> redArmy, blueArmy, redCaptured, blueCaptured;
    private boolean isFlagCaptured;
    private final StrategoRules rules;

    public Game() {
        players = new Player[2];

        redCaptured = new ArrayList<>();
        blueCaptured = new ArrayList<>();
        rules = new StrategoRules();
    }

    public Game(GameState startState, Player redPlayer, Player bluePlayer) {
        rules = new StrategoRules();
        this.state = startState;
        this.players = new Player[2];
        players[BLUE] = bluePlayer;
        players[RED] = redPlayer;
        System.out.println("red= " + redPlayer + ", blue= " + bluePlayer);
    }

    /**
     * begins the game.
     *
     * @throws java.lang.Exception
     */
    public void startGame() throws Exception {
        setupBoard();
        //players[1].setDisplay(new GameFrame(players[1].getColor()));
        toMove = BLUE;
        state = new GameState(Color.BLUE, blueArmy, redArmy);
        Color winner = play();
        for (Player p : players) {
            p.reportResult(winner);
        }
    }

    protected Color play() throws Exception {
        depth = 0;
        do {
            depth++;
            for (Player p : players) {
                p.displayBoard();//probably should change leave it for now
            }
            Move move = players[toMove].getMove();
            if (move == null) {
                System.out.println(state);
                isGameOver();
                System.out.println("game over " + isGameOver());
            }
            while (!rules.isLegal(state, move)) {
                try {
                    players[toMove].reportIllegalMove();
                } catch (Exception ex) {
                    System.err.println("AAAAHHH!!!! AI has gone CRAZY!!!!"
                            + move.toString() + "\n" + state);
                    //                   System.out.println(state);
                    throw ex;
                }
                move = players[toMove].getMove();
            }

            for (Player p : players) {
                p.reportMove(move);
            }
            processMove(move);
            toMove = (toMove + 1) % 2;
        } while (!isGameOver());
        for (Player p : players) {
            p.displayBoard();
        }
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
                    blueArmy = players[BLUE].getSetup();
                }
            };

            Thread t2 = new Thread() {
                @Override
                public void run() {
                    redArmy = players[RED].getSetup();
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
            return (AIPlayer.generateSucessors(state).isEmpty());
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
        FriendlyPiece piece = (FriendlyPiece) state.getSquare(row, column).getOccupier();
        piece = (FriendlyPiece) piece.setLocation(destRow, destCol);
        if (destSquare.isOccupied()) {
            FriendlyPiece defender
                    = (FriendlyPiece) state.getSquare(destRow, destCol).getOccupier();
            state = state.makeMove(move);
            Piece winner;
            winner = rules.resolveAttack(defender, piece);

            state = state.placePiece(destRow, destCol, winner);
            destSquare = state.getSquare(destRow, destCol);
            if (defender.getRank() == Rank.FLAG) {
                isFlagCaptured = true;
                System.out.println("game \nWinner= " + winner);
            }

//            System.out.println(state);
            for (Player p : players) {
                p.revealSquare(destSquare, move);
            }
        } else {
            state = state.makeMove(move);
        }

    }

    protected Color declareWinner() {
        Player winner = (toMove == BLUE) ? players[RED] : players[BLUE];
        //for(Player p: players) p.reportResult(winner.getColor());
        return winner.getColor();
    }

    public GameState getState() {
        return state;
    }

}
