

package stratego2.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author roussew
 */
public class Board implements Iterable<Square>{
    private final Square[][] squares;
    
    /**
     * default constructor 
     */
    public Board () {
        squares = new Square[Game.NUM_ROWS][Game.NUM_COLUMNS];
    }
    
    /**
     * clears the board. replaces each Square in the square matrix with a 
     * new Square
     */
    public void initialize() {
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
    public int[][] getIntBoard() {
        int[][] simpleBoard = new int[Game.NUM_ROWS][Game.NUM_COLUMNS];
        for (int i=0; i<squares.length; i++) {
            for (int j=0; j<squares.length; j++) {
                Square square = squares[i][j];
                int val = 0;
                if (!square.isActive()) val = -1;
                else {
                    if (square.isOccupied()) {
                        val = square.getOccupier().getValue();
                    }
                }                
                simpleBoard[i][j] = val;
            }
        }
        return simpleBoard;
    }
    
    
    /**
     * sets the occupier of the square at the given coordinates. Returns true 
     * if the operation is successful.  
     * Replaces the current occupier if any. Fails and returns false if the square
     * is inactive.
     * 
     * Does not set
     * the values of the piece (this should happen in the calling class).
     * @param row the row of the square
     * @param column the column of the square
     * @param piece the piece to be placed.
     * @return true if the operation is successful, and false otherwise.
     */
    public boolean placePiece(int row, int column, Piece piece) {
//        for (int [] r: getIntBoard()) 
//            System.out.println(Arrays.toString(r));
        System.out.println();
        Square square = squares[row][column];
        if(!square.isActive()) return false;
        square.setOccupier(piece);
        return true;
    }

    @Override
    public Iterator<Square> iterator() {
        return new Iterator(){
            private int i =0,  j = 0;
            @Override
            public boolean hasNext() {
                return (i < squares.length || j < squares.length);
            }
            @Override
            public Object next() {
                if(j < squares.length - 1) j++;
                else if (i < squares.length - 1)i++;
                else throw new NoSuchElementException();
                return squares[i][j];
            }           
        };
    }
    
    
}
