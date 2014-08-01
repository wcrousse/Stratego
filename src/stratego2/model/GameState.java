package stratego2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private List<? extends Piece> blueArmy;
    private List<? extends Piece> redArmy;
    private double utility;
    private int numVisited;
    /**
     * 0 represents red 1 represents blue. should use constants Game.RED and
     * Game.BLUE
     */
    private final Color toMove;

    public GameState( Color toMove, List<? extends Piece> redArmy,
            List<? extends Piece> blueArmy ) {
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

    public GameState clearSquare( int row, int column ) {
        GameState newBoard = new GameState( this, this.toMove );
        Square square = newBoard.squares[row][column];
        square.setOccupier( null );
        return newBoard;
    }

//    @Override
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
    public ArrayList<FriendlyPiece> getMovablePieces() {
        ArrayList<FriendlyPiece> movablePieces = new ArrayList<>();
        for ( int i = 0; i < squares.length; i++ ) {
            for ( int j = 0; j < squares[i].length; j++ ) {
                Square square = squares[i][j];
                if ( square.isOccupied() && 
                        square.getOccupier() instanceof FriendlyPiece) {
                    FriendlyPiece p = (FriendlyPiece)square.getOccupier();
                    if (p.getColor() == toMove && p instanceof FriendlyPiece
                            && ((FriendlyPiece) p).getRank() != Rank.BOMB
                            && ((FriendlyPiece) p).getRank() != Rank.FLAG) {

                        movablePieces.add(p);
                    }
                }
            }
        }
        return movablePieces;
    }

    /**
     * returns the color of pieces controlled by the player who moves next
     *
     * @return the color of the pieces controlled by the player who moves next
     */
    public Color getToMove() {
        return toMove;
    }



    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.toMove);
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

    public double getUtility() {
        return utility;
    }

    public void setUtility(double utility) {
        this.utility = utility;
    }

}
