

package stratego2.model;

import java.util.List;
import stratego2.model.Player.AI.GameState;

/**
 * contains the rules.
 * @author roussew
 */
public class StrategoRules {

   /**
     * examines the position of the player to move. to be called after each move
     * is made.
     *
     * @param state the current game state
     * @return returns true if the player's flag has been captured or if the
     * player has no available moves and false otherwise.
     */
    public boolean isGameOver(GameState state) {
        boolean result = true;
        if (!state.isFlagCaptured()) {
            List<Piece> army = state.getMovablePieces();
            for (Piece piece : army) {
                if (hasMove(piece, state)) {
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
    public boolean hasMove(Piece piece, GameState state) {
        boolean result = false;
        if (isLegal(state, piece, piece.getRow() - 1, piece.getColumn())) {
            result = true;
        } else if (isLegal(state, piece, piece.getRow() + 1, piece.getColumn())) {
            result = true;
        } else if (isLegal(state, piece, piece.getRow(), piece.getColumn() - 1)) {
            result = true;
        } else if (isLegal(state, piece, piece.getRow(), piece.getColumn() + 1)) {
            result = true;
        }
        return result;
    }

    /**
     * checks the legality of a proposed move. This is where the move rules are
     * specified.
     *
     * @param state the current game state
     * @param piece the piece to be moved
     * @param column the column index of the destination square
     * @param row the row index of the destination square
     * @return true if the move is legal, false otherwise.
     */
    public boolean isLegal(GameState state, Piece piece, int row, int column) {
        boolean result = true;
        if (row >= Game.NUM_ROWS
                || column >= Game.NUM_COLUMNS
                || row < 0
                || column < 0) {
            return false;
        }
        Square square = state.getSquare(row, column);
        //check if destination square is active
        if (!square.isActive()) {
            result = false;
            
        //check that piece is of a moveable type    
        }else if(piece.getRank() == Rank.BOMB || piece.getRank() == Rank.FLAG) {
            result = false;
        
        //check that the piece belongs to the correct player 
        } else if (piece.getColor() != state.getToMove()) {
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
            result = checkScoutMove(state, piece, row, column);

         //check if destination is contiguous with start square
        } else if (column > piece.getColumn() + 1
                || column < piece.getColumn() - 1
                || row > piece.getRow() + 1
                || row < piece.getRow() - 1) {
            result = false;
        }
       // printMove(result, piece, row, column);

        return result;
    }

    /**
     * A convenience method unpackages the data required by is legal(Piece, row, column) 
     * @param state the current game state
     * @param move a data structure holding the coordinates of the piece to be 
     * moved, prior to and after the move. 
     * @return true if the proposed move is legal, false otherwise
     */
    public boolean isLegal(GameState state, Move move) {
        Square startSquare;
        startSquare = state.getSquare(move.getStartRow(), move.getStartColumn());
        Piece piece = startSquare.getOccupier();
        return isLegal(state, piece, move.getDestinationRow(),
                move.getDestinationColumn());
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
    private boolean checkScoutMove(
            GameState state, Piece piece, int row, int column) {
        
        boolean result = true;
        Square square;
        if (column != piece.getColumn()) {
            int start = Math.min(column, piece.getColumn());
            int end = Math.max(column, piece.getColumn());
            for (int i = start + 1; i < end; i++) {
                square = state.getSquare(row, i);
                if (!square.isActive() || square.isOccupied()) {
                    result = false;
                }
            }
        } else {
            int start = Math.min(row, piece.getRow());
            int end = Math.max(row, piece.getRow());
            for (int i = start + 1; i < end; i++) {
                square = state.getSquare(i, column);
                if (!square.isActive() || square.isOccupied()) {
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * to be called when one piece attacks another. returns the winning piece.
     * in the event of a tie returns null
     * @param state the current game state
     * @param defender the piece occupying the square immediately prior to the
     * battle
     * @param attacker the piece that has just been moved to the already
     * occupied square
     * @return the winner 
     */
    public Piece resolveAttack(GameState state, Piece defender, Piece attacker) {
        Piece winner;
        //check if defender is a bomb
        if (defender.getRank() == Rank.BOMB) {
            if (attacker.getRank() == Rank.MINER) {
                winner = attacker;
            } else { 
                winner = defender;
            }
            //check if spy attacks marshal
        } else if (attacker.getRank() == Rank.SPY
                && defender.getRank() == Rank.MARSHAL) {
            winner = attacker;
            
            //check if flag was captured
        } else if (defender.getRank() == Rank.FLAG) {
            winner = attacker;
         
            //check for a tie
        } else {
            if (defender.getValue() == attacker.getValue()) {
                winner = null;
            } else {
                winner = (defender.getValue() > attacker.getValue())? defender: attacker;
            }
        }
        return winner;
    }

}
