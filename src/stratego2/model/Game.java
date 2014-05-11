package stratego2.model;

import java.util.ArrayList;
import java.util.List;
import stratego2.view.Display;

/**
 *
 * @author roussew
 */
public class Game {

    public static final int NUM_ROWS = 10;
    public static final int NUM_COLUMNS = 10;

    private static final int RED = 0;
    private static final int BLUE = 1;
    public static final int ARMY_SIZE = 40;
    /**
     * a tuple to hold the player objects.  
     **/
    private Player[] players;
    /**
     * intended as to index the players array. Should be incremented and then
     * modded by 2 following each move. Thus, should only equal 0 or 1.
     */
    private int toMove;
    /**
     * a data structure representing the current game layout
     */
    private Board board;
    private List<Piece> redArmy, blueArmy, redCaptured, blueCaptured;
    private boolean isFlagCaptured;

    public Game() {
        players = new Player[2];
        board = new Board();
        redCaptured = new ArrayList<>();
        blueCaptured = new ArrayList<>();
    }

    /**
     * begins the game.
     */
    public void play() {
        setupBoard();
        toMove = RED;
        do {
            Move move = players[toMove].getMove(board);
            while (!isLegal(move)) {
                players[toMove].reportIllegalMove();
                move = players[toMove].getMove(board);
            }
            processMove(move);
            toMove = (toMove + 1) % 2;
        } while (!isGameOver());
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
        board.initialize();
        blueArmy = players[0].getSetup();
        redArmy = players[1].getSetup();

        setPieces(blueArmy);
        setPieces(redArmy);
    }

    /**
     * places the pieces on the board.
     *
     * @param army a list containing all of a single players pieces.
     */
    private void setPieces(List<Piece> army) {
        for (Piece p : army) {
            int i, j;
            i = p.getRow();
            j = p.getColumn();
            board.placePiece(i, j, p);
        }

    }

    private void getMove(Player player) {
    }

    /**
     * examines the position of the player to move. to be called after each move
     * is made.
     *
     * @return returns true if the player's flag has been captured or if the
     * player has no available moves and false otherwise.
     */
    private boolean isGameOver() {
        boolean result = true;
        if (!isFlagCaptured) {
            List<Piece> army = (toMove != BLUE) ? blueArmy : redArmy;
            for (Piece piece : army) {
                System.out.println(piece);
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
    private boolean hasMove(Piece piece) {
        boolean result = false;
        if (isLegal(piece, piece.getRow() - 1, piece.getColumn())) {
            result = true;
        } else if (isLegal(piece, piece.getRow() + 1, piece.getColumn())) {
            result = true;
        } else if (isLegal(piece, piece.getRow(), piece.getColumn() - 1)) {
            result = true;
        } else if (isLegal(piece, piece.getRow(), piece.getColumn() + 1)) {
            result = true;
        }
        System.out.println(piece + ", " + result);
        return result;
    }

    /**
     * checks the legality of a proposed move. This is where the move rules are
     * specified.
     *
     * @param piece the piece to be moved
     * @param column the column index of the destination square
     * @param row the row index of the destination square
     * @return true if the move is legal, false otherwise.
     */
    private boolean isLegal(Piece piece, int row, int column) {
        boolean result = true;
        if (row >= Game.NUM_ROWS
                || column >= Game.NUM_COLUMNS
                || row < 0
                || column < 0) {
            return false;
        }
        Square square = board.getSquare(row, column);
        //check if destination square is active
        if (!square.isActive()) {
            result = false;
            
        //check that piece is of a moveable type    
        }else if(piece.getRank() == Rank.BOMB || piece.getRank() == Rank.FLAG) {
            result = false;
        
        //check that the piece belongs to the correct player 
            //this statement works but obviously that means that there is a prior
            //error. find and fix it
        } else if (piece.getColor() != players[toMove].getColor()) {
            result =  false;
            
         /* check if destination is already occupied by a piece belonging to the
         same player */ 
        } else if (square.isOccupied()
                && square.getOccupier().getColor() == piece.getColor()) {
            result = false;
            
        //check for diagonal moves
        } else if (column != piece.getColumn() && row != piece.getRow()) {
            result = false;

         //check if the piece is a scout
        } else if (piece.getValue() == 2) {
            result = checkScoutMove(piece, row, column);

         //check if destination is contiguous with start square
        } else if (column > piece.getColumn() + 1
                || column < piece.getColumn() - 1
                || row > piece.getRow() + 1
                || row < piece.getRow() - 1) {
            result = false;
        }
        printMove(result, piece, row, column);

        return result;
    }

    /**
     * A convenience method unpackages the data required by is legal(Piece, row, column) 
     * @param move a data structure holding the coordinates of the piece to be 
     * moved, prior to and after the move. 
     * @return true if the proposed move is legal, false otherwise
     */
    private boolean isLegal(Move move) {
        Square startSquare = board.getSquare(move.getStartRow(), move.getStartColumn());
        Piece piece = startSquare.getOccupier();
        return isLegal(piece, move.getDestinationRow(), move.getDestinationColumn());
    }

    private void printMove(boolean result, Piece piece, int row, int column) {
        System.out.println(result + piece.toString() + row + column);
    }

    /**
     * tests the legality of a proposed scout move. A scout is allowed to move
     * across any number of active, unoccupied square in any one of the four
     * cardinal directions. Does not check that destination square is not occupied
     * by another piece of the same color. This should have already taken place
     * in the calling method.
     * @param piece the scout to be moved
     * @param row the row index of the destination square
     * @param column the column index of the destination square.
     * @return 
     */
    private boolean checkScoutMove(Piece piece, int row, int column) {
        boolean result = true;
        Square square = board.getSquare(row, column);
        if (column != piece.getColumn()) {
            int start = Math.min(column, piece.getColumn());
            int end = Math.max(column, piece.getColumn());
            for (int i = start + 1; i < end; i++) {
                square = board.getSquare(row, i);
                if (!square.isActive() || square.getOccupier() != null) {
                    result = false;
                }
            }
        } else {
            int start = Math.min(row, piece.getRow());
            int end = Math.max(row, piece.getRow());
            for (int i = start + 1; i < end; i++) {
                square = board.getSquare(i, column);
                if (!square.isActive() || square.getOccupier() != null) {
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * to be called when one piece attacks another. Removes the appropriate
     * piece(s)
     *
     * @param square the square upon which the battle takes place
     * @param defender the piece occupying the square immediately prior to the
     * battle
     * @param attacker the piece that has just been moved to the already
     * occupied square
     */
    private void resolveAttack(Square square, Piece defender, Piece attacker) {
        //check if defender is a bomb
        if (defender.getRank() == Rank.BOMB) {
            if (attacker.getRank() == Rank.MINER) {
                cleanUp(square, attacker, defender);
            } else {
                cleanUp(square, defender, attacker);
            }
        } else if (attacker.getRank() == Rank.SPY
                && defender.getRank() == Rank.MARSHAL) {
            this.cleanUp(square, attacker, defender);
        } else if (defender.getRank() == Rank.FLAG) {
            cleanUp(square, attacker, defender);
            isFlagCaptured = true;
        } else {
            if (defender.getValue() == attacker.getValue()) {
                cleanUpTie(square, attacker, defender);
            } else {
                Piece winner = null;
                Piece loser = null;
                if (defender.getValue() > attacker.getValue()) {
                    winner = defender;
                    loser = attacker;
                } else if (attacker.getValue() > defender.getValue()) {
                    winner = attacker;
                    loser = defender;
                }
                cleanUp(square, winner, loser);
            }
        }
        for(Player player: players) player.revealSquare(square);
    }

    /**
     * removes the loser from the board, sets square.occupier to winner, adds
     * loser to appropriate captured piece list. In case of a tie call
     * cleanupTie()
     *
     * @param square the square
     * @param winner the new occupier of the square
     * @param loser the captured piece
     */
    private void cleanUp(Square square, Piece winner, Piece loser) {
        if (loser.getColor() == Color.BLUE) {
            blueCaptured.add(loser);
        } else {
            redCaptured.add(loser);
        }
        board.placePiece(loser.getRow(), loser.getColumn(), winner);
    }

    /**
     *
     * @param square
     * @param p1
     * @param p2
     */
    private void cleanUpTie(Square square, Piece p1, Piece p2) {
        square.setOccupier(null);
        if (p1.getColor() == Color.BLUE) {
            blueCaptured.add(p1);
            redCaptured.add(p2);
        } else {
            blueCaptured.add(p2);
            redCaptured.add(p1);
        }
    }

    /**
     * processes a move.
     *
     * @param move
     */
    private void processMove(Move move) {
        int startRow = move.getStartRow();
        int startColumn = move.getStartColumn();
        Square startSquare = board.getSquare(startRow, startColumn);
        Piece piece = startSquare.getOccupier();
        int destRow = move.getDestinationRow();
        int destCol = move.getDestinationColumn();
        piece.setRow(destRow);
        piece.setColumn(destCol);
        Square destSquare = board.getSquare(destRow, destCol);
        board.placePiece(startRow, startColumn, null);
        if (destSquare.isOccupied()) {
            resolveAttack(destSquare, destSquare.getOccupier(), piece);
        } else {           
            board.placePiece(destRow, destCol, piece);
        }
    }

}
