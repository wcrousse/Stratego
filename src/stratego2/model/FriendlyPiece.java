

package stratego2.model;

/**
 *
 * @author roussew
 */
public class FriendlyPiece extends Piece{

    private final Rank rank;
    
    public FriendlyPiece(Color color, int row, int column, Rank rank) {
        super(color, row, column);
        this.rank = rank;
    }
    
    public FriendlyPiece(FriendlyPiece piece) {
        super(piece);
        rank = piece.getRank();
    }
    
    public Rank getRank() {
        return rank;
    }
    
    @Override
    public Piece setLocation(int row, int column) {
        return new FriendlyPiece(getColor(), row, column, rank);
    }

    @Override
    public String toString() {
        return "FriendlyPiece{" + ", color=" + getColor() + ", row=" + getRow()
                + ", column=" + getColumn() + ", rank=" + rank + '}';
    }

    @Override
    public Piece copy() {
        return new FriendlyPiece(this);
    }

    public int getValue() {
        return rank.getValue();
    }
    
    

}
