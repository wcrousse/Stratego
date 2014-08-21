

package stratego2.model;

/**
 *
 * @author roussew
 */
public class FriendlyPiece extends Piece{

    private final Rank rank;
    private boolean isKnown;
    
    public FriendlyPiece(Color color, int row, int column, Rank rank, boolean isKnown) {
        super(color, row, column);
        this.rank = rank;
        this.isKnown = isKnown;
    }
    
    public FriendlyPiece(Color color, int row, int column, Rank rank) {
        this(color, row, column, rank, false);
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
        return new FriendlyPiece(getColor(), row, column, rank, isKnown);
    }

    public Piece makeKnown() {
        return new FriendlyPiece(getColor(), getRow(), getColumn(), rank, true);
    }

    public boolean getIsKnown() {
        return isKnown;
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
