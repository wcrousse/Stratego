

package stratego2.model;

/**
 *
 * @author roussew
 */
public class HumanEnemyPiece extends Piece{

    public HumanEnemyPiece(Color color, int row, int column) {
        super(color, row, column);
    }

    @Override
    public Piece setLocation(int row, int column) {
        return new HumanEnemyPiece(getColor(), row, column);
    }

    @Override
    public Piece copy() {
        return new HumanEnemyPiece(getColor(), getRow(), getColumn());
    }
    
}
