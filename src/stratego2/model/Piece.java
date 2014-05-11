
package stratego2.model;

/**
 *
 * @author roussew
 */
public class Piece {
    private int  row, column;
    private Rank rank;
    private final Color color;
    
    public Piece(Rank rank, Color color) {
        this.rank = rank;
        this.color = color;
    }
    
    public Piece(Piece original) {
        this(original.getRank(), original.getColor());
        row = original.getRow();
        column = original.getColumn();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
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
