

package stratego2.model;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents a game state. As with all game model data-structures, the Board
 * class is and should remain immutable. All getters return deep copies 
 * of the current board. Immutability is an important property here. It allows
 * us to more clearly and concisely express AI algorithms by decoupling 
 * planning and game-state duplication logic.
 * @author roussew
 */
public class Board implements Iterable<Square> {

    /**
     * represents the current configuration of the Stratego board
     */
    protected final Square[][] squares;
    
    private boolean isFlagCaptured;
    /**
     * default constructor.
     */
    public Board () {
        squares = new Square[Game.NUM_ROWS][Game.NUM_COLUMNS];
        for (int i=0; i<Game.NUM_ROWS; i++) {
            for (int j = 0; j<Game.NUM_COLUMNS; j++) {
                boolean isActive;
                isActive = !( (i > 3 && i < 6) &&
                        ((j > 1 && j < 4) || (j > 5 && j < 8)) );
                        
                squares[i][j] = new Square(isActive);
            }
        }
    }
    /**
     * This is the constructor that should typically be used. 
     * @param redArmy
     * @param blueArmy 
     */
    public Board(List<? extends Piece> redArmy, List<? extends Piece> blueArmy) {
        this();
        for(Piece p: redArmy) {
            squares[p.getRow()][p.getColumn()].setOccupier(p);
        }
        for (Piece p: blueArmy) {
            squares[p.getRow()][p.getColumn()].setOccupier(p);
        }
    }

    /**
     * Creates and returns a deep copy of the given Board Object. 
     * @param board the Board to be copied
     */
    public Board (Board board) {
        this();
        for (int i=0; i<Game.NUM_ROWS; i++) {
            for (int j=0; j<Game.NUM_COLUMNS; j++) {
                squares[i][j] = board.getSquare(i, j);
            }
        }
    }

    /**
     * just your typical getter method.  
     * @param row 
     * @param column
     * @return returns a deep copy of the square
     */
    public Square getSquare(int row, int column) {
        if (row < 0 || column < 0 ||
            row > squares.length || column > squares[row].length) {
            throw new NoSuchElementException();
        }
        return squares[row][column].copy();
    }
    /**
     * makes an integer array representation of the board. where the integer
     * at each position of the array is the numerical value of the piece which
     * occupies that square. Empty squares are represented with 0. Inactive
     * squares are represented with -1. 
     * @return the array representation of the board
     */
//    public int[][] getIntBoard() {
//        int[][] simpleBoard = new int[Game.NUM_ROWS][Game.NUM_COLUMNS];
//        for (int i=0; i<squares.length; i++) {
//            for (int j=0; j<squares.length; j++) {
//                Square square = squares[i][j];
//                int val = 0;
//                if (!square.isActive()) val = -1;
//                else {
//                    if (square.isOccupied()) {
//                        val = square.getOccupier().getValue();
//                    }
//                }                
//                simpleBoard[i][j] = val;
//            }
//        }
//        return simpleBoard;
//    }
//    
    
    /**
     * sets the occupier of the square at the given coordinates. Returns a deep
     * copy of the itself with the given move. 
     * Replaces the current occupier if any. Should checks should be made to insure
     * that the move is legal prior to call. Has no rule enforcing logic.
     * 
     * Does not set
     * the values of the piece (this should happen in the calling class).
     * @param row the row of the square
     * @param column the column of the square
     * @param piece the piece to be placed.
     * @return a reference to the resulting game-state. In the future this will
     * likely be a completely new game object. 
     */
    public Board placePiece(int row, int column, Piece piece) {
        Board newBoard = new Board(this);
        Square square = newBoard.squares[row][column];
        if(square.isActive())
            square.setOccupier(piece);
        return newBoard;
    }
    /**
     * Creates and returns a new board that is identical to the this, except
     * that the given move has been made.
     * @param move a data-structure specifying the indices of the piece to be
     * moved both before and after the move is made.
     * @return the new board.
     */
//    public Board makeMove(Move move) {
//        Board board = new Board(this);
//        int startRow = move.getStartRow();
//        int startColumn = move.getStartColumn();
//        int destinationRow = move.getDestinationRow();
//        int destinationColumn = move.getDestinationColumn();
//        Square startSquare = board.squares[startRow][startColumn];
//        Square destination = board.squares[destinationRow][destinationColumn];
//        Piece piece = startSquare.getOccupier();
//        startSquare.setOccupier(null);
//        piece = piece.setLocation(destinationRow, destinationColumn);
//        destination.setOccupier(piece); 
//        return board;
//    }
    
    /**
     * returns a copy of this board with the occupying piece from the square
     * specified by the given coordinates removed.
     * @param row the row index of the square to be cleared.
     * @param column the column index of the square to be cleared.
     * @return a reference to the resulting game-state. In the future this will
     * likely be a completely new game object. 
     */
//    public Board clearSquare(int row, int column) {
//        Board newBoard = new Board(this);
//        if (newBoard.squares[row][column].getOccupier().getValue() == 12) {
//            isFlagCaptured = true;
//        }
//        newBoard.squares[row][column].setOccupier(null);
//        
//        return newBoard;
//    }
    
    public boolean isFlagCaptured() {
        return isFlagCaptured;
    }

    /**
     * Returns an iterator which iterates through the squares in row major
     * order. Iterator returns deep copies of the squares, not references to the
     * squares themselves.
     * @return 
     */
    @Override
    public Iterator<Square> iterator() {
        return new Iterator(){
            private int i =0,  j = 0;
            @Override
            public boolean hasNext() {
                return (i < squares.length);
            }
            @Override
            public Object next() {
                Square nextSquare;
                if (!hasNext()) throw new NoSuchElementException();
                nextSquare = getSquare(i, j);
                if(j < squares[i].length - 1) j++;
                else {
                    i++;
                    j = 0;
                }
                
                return nextSquare;
            }           
        };
    }
    
    
}
