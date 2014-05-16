
package stratego2.model;

/**
 * A data structure to represent Stratego game pieces. Is and should remain 
 * immutable.
 * @author roussew
 */
public class Piece {
    private final int  row, column;
    private final Rank rank;
    private final Color color;
    
    public Piece(Rank rank, Color color, int row, int column) {
        this.rank = rank;
        this.color = color;
        this.row = row;
        this.column = column;
    }
    
    public Piece(Piece original) {
        this(original.getRank(), original.getColor(),
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
     */
    public Piece setLocation(int row, int column) {
        Piece newPiece = new Piece(this.rank, this.color, row, column);
        return newPiece;
    }

    public Color getColor() {
        return color;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue() {
        return rank.getValue();
    }
    @Override
    public String toString() {
        if(color == Color.BLUE) {
            return "b" + ", " + rank.getValue()+ " x="+row + ", y="+column + " ";
        }else return "r"+", "+rank.getValue() + " x="+row + ", y="+column + " ";
     
    }
    

}
