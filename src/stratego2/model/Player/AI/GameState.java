package stratego2.model.Player.AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import stratego2.model.Board;
import stratego2.model.Color;
import stratego2.model.Move;
import stratego2.model.Piece;
import stratego2.model.Rank;
import stratego2.model.Square;

/**
 * data-structure represents the game state at any given moment. Game state
 * includes the arrangement of pieces on the board, who is to move. Game state
 * is nigh immutable; only the utility can be modified. successors are not
 * modifications of the current state. They are new states.
 *
 * @author roussew
 */
public class GameState extends Board {
    private Color playerColor;
    private List<Piece> blueArmy;
    private List<Piece> redArmy;
    /**
     * 0 represents red 1 represents blue. should use constants Game.RED and
     * Game.BLUE
     */
    private final Color toMove;
    private double utility;

    public GameState( Color toMove, List<Piece> redArmy, List<Piece> blueArmy ) {
        super(redArmy, blueArmy);
        this.toMove = toMove;
        this.redArmy = redArmy;
        this.blueArmy = blueArmy;
    }

    public GameState( Board board, Color playerColor ) {
        super( board );
        this.playerColor = playerColor;
        toMove = Color.RED;
    }

    public GameState() {
        blueArmy = new ArrayList<>();
        redArmy = new ArrayList<>();
        toMove = Color.RED;
    }

    @Override
    public GameState placePiece( int row, int column, Piece piece ) {
        GameState newBoard = new GameState( this, this.toMove );
        Square square = newBoard.squares[row][column];
        if ( square.isActive() ) {
            square.setOccupier( piece );
        }
        return newBoard;
    }

    @Override
    public GameState clearSquare( int row, int column ) {
        GameState newBoard = new GameState( this, this.toMove );
        Square square = newBoard.squares[row][column];
        square.setOccupier( null );
        return newBoard;
    }

    @Override
    public GameState makeMove( Move move ) {
        Color nextColor = ( toMove == Color.RED ) ? Color.BLUE : Color.RED;
        GameState board = new GameState( this, nextColor );
        int startRow = move.getStartRow();
        int startColumn = move.getStartColumn();
        int destinationRow = move.getDestinationRow();
        int destinationColumn = move.getDestinationColumn();
        Square startSquare = board.squares[startRow][startColumn];
        Square destination = board.squares[destinationRow][destinationColumn];
        Piece piece = startSquare.getOccupier();
        startSquare.setOccupier( null );
        piece = piece.setLocation(destinationRow, destinationColumn);
        destination.setOccupier( piece );
        return board;
    }

    /**
     * returns a list of the movable pieces that belong to the player whose turn
     * it is to move. Here, movable, means not a flag or a bomb. Pieces may can
     * both be movable and have no available moves.
     *
     * @return
     */
    public List<Piece> getMovablePieces() {
        ArrayList<Piece> movablePieces = new ArrayList<>();
        for ( int i = 0; i < squares.length; i++ ) {
            for ( int j = 0; j < squares[i].length; j++ ) {
                Square square = squares[i][j];
                if ( square.isOccupied() ) {
                    Piece p = square.getOccupier();
                    if (p.getColor() == toMove
                            && p.getRank() != Rank.BOMB
                            && p.getRank() != Rank.FLAG) {

                        movablePieces.add(p);
                    }
                }
            }
        }
        return movablePieces;
    }

    /**
     * returns the utility of the state
     *
     * @return the utility
     */
    public double getUtility() {
        return utility;
    }

    /**
     * returns the color of pieces controlled by the player who moves next
     *
     * @return the color of the pieces controlled by the player who moves next
     */
    public Color getToMove() {
        return toMove;
    }

    /**
     * sets the utility of the state
     *
     * @param utility a double representation of the state's utility
     */
    public void setUtility(double utility) {
        this.utility = utility;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.toMove);
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.utility) ^ (Double.doubleToLongBits(this.utility) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj ) {
        if (obj == null) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final GameState other = ( GameState ) obj;
        if ( this.toMove != other.toMove ) {
            return false;
        }
        if (Double.doubleToLongBits( this.utility )
                != Double.doubleToLongBits( other.utility) ) {
            return false;
        }

        return areBoardsEqual( other.squares );
    }

    private boolean areBoardsEqual( Square[][] others ) {
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                if (!squares[i][j].equals(others[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

}
