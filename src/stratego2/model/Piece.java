
package stratego2.model;

import java.util.Objects;

/**
 * A data structure to represent Stratego game pieces. Is and should remain 
 * immutable.
 * @author roussew
 */
public abstract class Piece {
    private final int  row, column;
//    private final Rank rank;
    private final Color color;
   
    public Piece(Color color, int row, int column) {
        this.color = color;
        this.row = row;
        this.column = column;
    }
    
    public Piece(Piece original) {
        this(original.getColor(),
                original.getRow(), original.getColumn());
    }
    
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    /**
     * returns a new piece that has identical rank and color to the first but
     * has the location give.
     * @param row the row index of the piece
     * @param column the column index of the piece.
     * @return Piece a new piece with new rows and columns
     */
    public abstract Piece setLocation(int row, int column);

    public Color getColor() {
        return color;
    }
    
    @Override
    public String toString() {
        if(color == Color.BLUE) {
            return "b" + ",  x="+row + ", y="+column + " ";
        }else return "r"+", x="+row + ", y="+column + " ";
     
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.row;
        hash = 53 * hash + this.column;
        hash = 53 * hash + Objects.hashCode(this.color);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Piece other = (Piece) obj;
        if (this.row != other.row) {
            return false;
        }
        if (this.column != other.column) {
            return false;
        }

        if (this.color != other.color) {
            return false;
        }
        return true;
    }

    public abstract Piece copy();
    
}
